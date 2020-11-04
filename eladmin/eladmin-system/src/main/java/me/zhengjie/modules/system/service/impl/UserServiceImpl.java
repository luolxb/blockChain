/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.config.FileProperties;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.exception.EntityNotFoundException;
import me.zhengjie.modules.security.service.UserCacheClean;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.SysInvitationCode;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.vo.UserCodeRq;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.MenuService;
import me.zhengjie.modules.system.service.SysInvitationCodeService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.*;
import me.zhengjie.modules.system.service.mapstruct.MenuMapper;
import me.zhengjie.modules.system.service.mapstruct.UserMapper;
import me.zhengjie.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "user")
@Slf4j
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FileProperties properties;
    private final RedisUtils redisUtils;
    private final UserCacheClean userCacheClean;
    private final MenuMapper menuMapper;
    private final MenuService menuService;


    @Autowired
    private SysInvitationCodeService sysInvitationCodeService;

    @Override
    public Object queryAll(UserQueryCriteria criteria, Pageable pageable) {
        List<Timestamp> timestamps = new ArrayList<>();
        if (criteria.getStartTime() != null) {
            timestamps.add(new Timestamp(criteria.getStartTime()));

        }
        if (criteria.getEndTime() != null) {
            timestamps.add(new Timestamp(criteria.getEndTime()));
        }

        if (!CollectionUtils.isEmpty(timestamps)) {
            criteria.setCreateTime(timestamps);
        }
        criteria.setIsAdmin(true);
        Page<User> page = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage2(page.map(userMapper::toDto));
    }

    @Override
    public List<UserDto> queryAll(UserQueryCriteria criteria) {
        List<User> users = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return userMapper.toDto(users);
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    @Transactional(rollbackFor = Exception.class)
    public UserDto findById(long id) {
        User user = userRepository.findById(id).orElseGet(User::new);
        ValidationUtil.isNull(user.getId(), "User", "id", id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(User resources) {
        if (userRepository.findByUsername(resources.getUsername()) != null) {
            throw new EntityExistException(User.class, "username", resources.getUsername());
        }
        Set<Role> roles = resources.getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            throw new BadRequestException("角色不能为空");
        }
        roles.forEach(role -> {
            // 不可能新增用户为超管
            if (role.getId() == 1) {
                throw new BadRequestException("不能新增超级管理员用户");
            }
        });
//        if (userRepository.findByEmail(resources.getEmail()) != null) {
//            throw new EntityExistException(User.class, "email", resources.getEmail());
//        }
        userRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User resources) {
        if (resources.getId() == null) {
            throw new BadRequestException("用户ID不能为空");
        }
        User user = userRepository.findById(resources.getId()).orElseGet(User::new);
        if (user == null) {
            throw new BadRequestException("用户ID不存在");
        }
        User user1 = userRepository.findByUsername(resources.getUsername());
//        User user2 = userRepository.findByEmail(resources.getEmail());

        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new BadRequestException("用户名称已存在，" + resources.getUsername());
        }

        Set<Role> roles = resources.getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            throw new BadRequestException("角色不能为空");
        }

        if (org.apache.commons.lang3.StringUtils.equals("admin", resources.getUsername()) &&
                org.apache.commons.lang3.StringUtils.equals("admin", SecurityUtils.getCurrentUsername())) {
            roles.forEach(role -> {
                // 不可能新增用户为超管
                if (role.getId() != 1) {
                    throw new BadRequestException("不能新增超级管理员用户");
                }
            });
        }

//        if (user2 != null && !user.getId().equals(user2.getId())) {
//            throw new BadRequestException( "", resources.getEmail());
//        }
        // 如果用户的角色改变
        if (!resources.getRoles().equals(user.getRoles())) {
            redisUtils.del(CacheKey.DATE_USER + resources.getId());
            redisUtils.del(CacheKey.MENU_USER + resources.getId());
            redisUtils.del(CacheKey.ROLE_AUTH + resources.getId());
        }
        // 如果用户名称修改
        if (resources.getUsername() != null &&
                !org.apache.commons.lang3.StringUtils.equals(resources.getUsername(), user.getUsername())) {
            redisUtils.del("user::username:" + user.getUsername());
        }
        user.setUsername(resources.getUsername());
//        user.setEmail(resources.getEmail());
        user.setEnabled(resources.getEnabled());
        user.setRoles(resources.getRoles());
//        user.setDept(resources.getDept());
//        user.setJobs(resources.getJobs());
        if (org.apache.commons.lang3.StringUtils.isNotBlank(resources.getPassword())) {
            user.setPassword(passwordEncoder.encode(resources.getPassword()));
        }
        user.setPhone(resources.getPhone());
        user.setNickName(resources.getNickName());
        user.setGender(org.apache.commons.lang3.StringUtils.isBlank(resources.getGender()) ? "" : resources.getGender());
        userRepository.save(user);
        // 清除缓存
        delCaches(user.getId(), user.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCenter(User resources) {
        User user = userRepository.findById(resources.getId()).orElseGet(User::new);
        user.setNickName(resources.getNickName());
        user.setPhone(resources.getPhone());
        user.setGender(resources.getGender());
        userRepository.save(user);
        // 清理缓存
        delCaches(user.getId(), user.getUsername());
    }

    /**
     * 验证码注册
     *
     * @param userCodeRq
     */
    @Override
    public void createCode(UserCodeRq userCodeRq) {
        if (org.apache.commons.lang3.StringUtils.isBlank(userCodeRq.getInvitationCode())) {
            throw new BadRequestException("邀请码不能为空");
        }
        // 验证手机号是否正确
        if (!NtsUtil.isPhone(userCodeRq.getMobile())) {
            throw new BadRequestException("手机号格式不正确");
        }
        // 判断邀请码是否正确
        SysInvitationCode code = sysInvitationCodeService.getOne(new QueryWrapper<SysInvitationCode>().eq("code", userCodeRq.getInvitationCode()));
        if (code == null) {
            throw new BadRequestException("邀请码不正确");
        } else if (code.getIsUse() == 2) {
            throw new BadRequestException("邀请码已经使用");
        }
        // 验证验证码是否正确
        String str = (String) redisUtils.get(RedisKey.SMS_SEND + userCodeRq.mobile);
        if (str == null) {
            throw new BadRequestException("验证码不正确");
        } else if (!org.apache.commons.lang3.StringUtils.equals(userCodeRq.getValidCode(), str)) {
            throw new BadRequestException("验证码不正确");
        }

        // 判断手机号是否已注册
        if (userRepository.findByUsername(userCodeRq.getMobile()) != null) {
            throw new BadRequestException("手机号已注册");
        }

        if (userRepository.findByPhone(userCodeRq.getMobile()) != null) {
            throw new BadRequestException("手机号已注册");
        }
        if (!org.apache.commons.lang3.StringUtils.equals(userCodeRq.getPassword(), userCodeRq.getPasswordc())) {
            throw new BadRequestException("两次密码不一致");
        }
        // 判断用户是否已存在
        User user1 = userRepository.findByUsername(userCodeRq.getUserName());
        if (user1 != null) {
            throw new BadRequestException("用户名已存在");
        }

        User user = new User();
        user.setPhone(userCodeRq.getMobile());
        String encode = passwordEncoder.encode(userCodeRq.getPassword());
        user.setPassword(encode);
        user.setUsername(userCodeRq.getUserName());
        user.setGender("");
        user.setCode(userCodeRq.getInvitationCode());
//        user.setEmail(userCodeRq.getMobile()+"@163.com");
        user.setNickName(userCodeRq.getUserName());
        user.setEnabled(true);

        User save = userRepository.save(user);
        // 修改邀请码为已使用
        SysInvitationCode sysInvitationCode = new SysInvitationCode();
        sysInvitationCode.setIsUse(2);
        sysInvitationCodeService.update(sysInvitationCode, new QueryWrapper<SysInvitationCode>().eq("code", userCodeRq.getInvitationCode()));

        // 删除换成记录
        redisUtils.del(RedisKey.SMS_SEND + userCodeRq.mobile);

        // 将用户信息保存缓存 密码MD5加密，在新增企业信息后同步到商城
        String md5Pass = Md5Util.md5Encode("TPSHOP",userCodeRq.getPassword());
        save.setPassword(md5Pass);

        redisUtils.set(RedisKey.SEND_SHOP_USER_INFO + save.getUsername(),save);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            // 清理缓存
            UserDto user = findById(id);
            delCaches(user.getId(), user.getUsername());
        }
        userRepository.deleteAllByIdIn(ids);
    }

    @Override
    @Cacheable(key = "'username:' + #p0")
    public UserDto findByName(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new EntityNotFoundException(User.class, "name", userName);
        } else {
            UserDto userDto = userMapper.toDto(user);
            List<MenuDto> dtoArrayList = new ArrayList<>();
            // 如果是admin 就查询获取所有的菜单
            if (org.apache.commons.lang3.StringUtils.equals("admin", user.getUsername())) {
                List<MenuDto> menuDtoList = menuService.menuTree();
                userDto.setMenuTree(menuDtoList);
            } else if (!CollectionUtils.isEmpty(user.getRoles())) {
                user.getRoles().forEach(role -> {
                    role.getMenus().forEach(menu -> {
                        MenuDto menuDto = menuMapper.toDto(menu);
                        dtoArrayList.add(menuDto);
                    });
                });

                List<MenuDto> dtoRootList = dtoArrayList
                        .stream()
                        .filter(menuDto ->
                                !dtoArrayList.stream().map(MenuDto::getId).collect(Collectors.toList()).contains(menuDto.getPid()))
                        .collect(Collectors.toList());

                getChild(dtoArrayList, dtoRootList);
                userDto.setMenuTree(dtoRootList);
            }
            return userDto;
        }
    }

    /**
     * 递归获取子集
     *
     * @param menuDtoList
     * @param dtoRootList
     */
    private void getChild(List<MenuDto> menuDtoList, List<MenuDto> dtoRootList) {
        dtoRootList.forEach(dtoRoot -> {
            List<MenuDto> list = menuDtoList
                    .stream()
                    .filter(menuDto ->
                            menuDto.getPid().equals(dtoRoot.getId()))
                    .collect(Collectors.toList());
            if (list.size() > 0) {
                dtoRoot.setChildren(list);
                getChild(menuDtoList, list);
            }
        });
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePass(String username, String pass) {
        userRepository.updatePass(username, pass, new Date());
        redisUtils.del("user::username:" + username);
        flushCache(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> updateAvatar(MultipartFile multipartFile) {
        User user = userRepository.findByUsername(SecurityUtils.getCurrentUsername());
        String oldPath = user.getAvatarPath();
        File file = FileUtil.upload(multipartFile, properties.getPath().getAvatar());
        user.setAvatarPath(Objects.requireNonNull(file).getPath());
        user.setAvatarName(file.getName());
        userRepository.save(user);
        if (StringUtils.isNotBlank(oldPath)) {
            FileUtil.del(oldPath);
        }
        @NotBlank String username = user.getUsername();
        redisUtils.del(CacheKey.USER_NAME + username);
        flushCache(username);
        return new HashMap<String, String>(1) {{
            put("avatar", file.getName());
        }};
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String username, String email) {
        userRepository.updateEmail(username, email);
        redisUtils.del(CacheKey.USER_NAME + username);
        flushCache(username);
    }

    @Override
    public void download(List<UserDto> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserDto userDTO : queryAll) {
            List<String> roles = userDTO.getRoles().stream().map(RoleSmallDto::getName).collect(Collectors.toList());
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", userDTO.getUsername());
            map.put("角色", roles);
            map.put("部门", userDTO.getDept().getName());
            map.put("岗位", userDTO.getJobs().stream().map(JobSmallDto::getName).collect(Collectors.toList()));
            map.put("邮箱", userDTO.getEmail());
            map.put("状态", userDTO.getEnabled() ? "启用" : "禁用");
            map.put("手机号码", userDTO.getPhone());
            map.put("修改密码的时间", userDTO.getPwdResetTime());
            map.put("创建日期", userDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response, "test");
    }

    /**
     * 清理缓存
     *
     * @param id /
     */
    public void delCaches(Long id, String username) {
        redisUtils.del(CacheKey.USER_ID + id);
        redisUtils.del(CacheKey.USER_NAME + username);
        flushCache(username);
    }

    /**
     * 清理 登陆时 用户缓存信息
     *
     * @param username /
     */
    private void flushCache(String username) {
        userCacheClean.cleanUserCache(username);
    }

    @Override
    public UserDto findByPhone(String phone) {
        User user = userRepository.findByPhone(phone);
        if (user == null) {
            throw new BadRequestException("用户不存在");
        } else {
            return userMapper.toDto(user);
        }
    }

    @Override
    public User findByPhone2(String phone) {
        return userRepository.findByPhone(phone);

    }

    /**
     * 修改最后登录时间
     *
     * @param jwtUserDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByLastLogin(JwtUserDto jwtUserDto) {
        userRepository.updateByLastLogin(jwtUserDto.getUser().getId(), new Date());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePhone(Long id, Date pwdResetTime, String phone, String name) {
        userRepository.updatePhone(id, pwdResetTime, phone);
        redisUtils.del(CacheKey.USER_NAME + name);
        flushCache(name);

    }
}
