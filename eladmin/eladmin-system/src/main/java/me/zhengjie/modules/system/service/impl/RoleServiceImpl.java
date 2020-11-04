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

import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.UserCacheClean;
import me.zhengjie.modules.system.domain.Menu;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.vo.RoleSearchRq;
import me.zhengjie.modules.system.repository.RoleRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.RoleService;
import me.zhengjie.modules.system.service.SysRoleMenusService;
import me.zhengjie.modules.system.service.dto.*;
import me.zhengjie.modules.system.service.mapstruct.RoleMapper;
import me.zhengjie.modules.system.service.mapstruct.RoleSmallMapper;
import me.zhengjie.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zheng Jie
 * @date 2018-12-03
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "role")
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final RoleSmallMapper roleSmallMapper;
    private final RedisUtils redisUtils;
    private final UserRepository userRepository;
    private final UserCacheClean userCacheClean;

    @Autowired
    private SysRoleMenusService sysRoleMenusService;

    @Override
    public List<RoleDto> queryAll() {
        Sort sort = new Sort(Sort.Direction.ASC, "level");
        return roleMapper.toDto(roleRepository.findAll(sort));
    }

    @Override
    public List<RoleDto> queryAll(RoleQueryCriteria criteria) {
        return roleMapper.toDto(roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public Object queryAll(RoleQueryCriteria criteria, Pageable pageable) {
        Page<Role> page = roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(roleMapper::toDto));
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    @Transactional(rollbackFor = Exception.class)
    public RoleDto findById(long id) {
        Role role = roleRepository.findById(id).orElseGet(Role::new);
        ValidationUtil.isNull(role.getId(), "Role", "id", id);
        return roleMapper.toDto(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Role resources) {
        if (roleRepository.findByName(resources.getName()) != null) {
            throw new BadRequestException("角色名称已经存在：" + resources.getName());
        }
        roleRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Role resources) {
        if (null == resources.getId()) {
            throw new BadRequestException("角色ID不能为空");
        }
        if (resources.getId() == 1) {
            throw new BadRequestException("超级管理员角色不允许操作");
        }
        Role role = roleRepository.findById(resources.getId()).orElseGet(Role::new);
        if (null == role) {
            throw new BadRequestException("角色不存在");
        }
        Role role1 = roleRepository.findByName(resources.getName());

        if (role1 != null && !role1.getId().equals(role.getId())) {
            throw new BadRequestException("角色名称已经存在:" + resources.getName());
        }
        // 删除关联 角色菜单
        roleRepository.untiedMenu2(resources.getId());

//        role.setDescription(resources.getDescription());
//        role.setDataScope(resources.getDataScope());
//        role.setDepts(resources.getDepts());
//        role.setLevel(resources.getLevel());
        roleRepository.save(resources);
        // 更新相关缓存
        delCaches(role.getId(), null);
    }

    @Override
    public void updateMenu(Role resources, RoleDto roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        List<User> users = userRepository.findByRoleId(role.getId());
        // 更新菜单
        role.setMenus(resources.getMenus());
        delCaches(resources.getId(), users);
        roleRepository.save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void untiedMenu(Long menuId) {
        // 更新菜单
        roleRepository.untiedMenu(menuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            // 更新相关缓存
            delCaches(id, null);
        }
        roleRepository.deleteAllByIdIn(ids);
    }

    @Override
    public List<RoleSmallDto> findByUsersId(Long id) {
        return roleSmallMapper.toDto(new ArrayList<>(roleRepository.findByUserId(id)));
    }

    @Override
    public Integer findByRoles(Set<Role> roles) {
        Set<RoleDto> roleDtos = new HashSet<>();
        for (Role role : roles) {
            roleDtos.add(findById(role.getId()));
        }
        return Collections.min(roleDtos.stream().map(RoleDto::getLevel).collect(Collectors.toList()));
    }

    @Override
    @Cacheable(key = "'auth:' + #p0.id")
    public List<GrantedAuthority> mapToGrantedAuthorities(UserDto user) {
        Set<String> permissions = new HashSet<>();
        // 如果是管理员直接返回 user.getIsAdmin()
        if (org.apache.commons.lang3.StringUtils.equals(user.getUsername(), "admin")) {
            permissions.add("admin");
            return permissions.stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        Set<Role> roles = roleRepository.findByUserId(user.getId());
        permissions = roles.stream().flatMap(role -> role.getMenus().stream())
                .filter(menu -> StringUtils.isNotBlank(menu.getPermission()))
                .map(Menu::getPermission).collect(Collectors.toSet());
        return permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public void download(List<RoleDto> roles, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RoleDto role : roles) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("角色名称", role.getName());
            map.put("角色级别", role.getLevel());
            map.put("描述", role.getDescription());
            map.put("创建日期", role.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response, "test");
    }

    @Override
    public void verification(Set<Long> ids) {
        if (userRepository.countByRoles(ids) > 0) {
            throw new BadRequestException("所选角色存在用户关联，请解除关联再试！");
        }
    }

    @Override
    public List<Role> findInMenuId(List<Long> menuIds) {
        return roleRepository.findInMenuId(menuIds);
    }

    /**
     * 角色分页
     *
     * @param roleSearchRq
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Object rolePage(RoleSearchRq roleSearchRq) {
        Integer size = roleSearchRq.getSize();
        Integer pageNumber = roleSearchRq.getPage();
        Sort sort = Sort.by(Sort.Order.desc("createTime"));
        Pageable pageable = PageRequest.of(pageNumber - 1, size, sort);

        List<Timestamp> timestamps = new ArrayList<>();
        if (roleSearchRq.getStartTime() != null) {
            timestamps.add(new Timestamp(roleSearchRq.getStartTime()));

        }
        if (roleSearchRq.getEndTime() != null) {
            timestamps.add(new Timestamp(roleSearchRq.getEndTime()));
        }

        if (!CollectionUtils.isEmpty(timestamps)) {
            roleSearchRq.setCreateTime(timestamps);

        }

        Page<Role> page = roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, roleSearchRq, criteriaBuilder), pageable);
        Map<String, Object> map = PageUtil.toPage2(page.map(roleMapper::toDto));
        List<RoleDto> records = (List<RoleDto>) map.get("records");

        // 返回该角色的菜单树
        records.forEach(record -> {
                    Long id = record.getId();
                    List<MenuDto> menuDtoList = sysRoleMenusService.findMenuByRoleId(id);

                    // 获取根节点
                    List<MenuDto> dtoRootList = menuDtoList
                            .stream()
                            .filter(menuDto ->
                                    !menuDtoList.stream().map(MenuDto::getId).collect(Collectors.toList()).contains(menuDto.getPid()))
                            .collect(Collectors.toList());

                    getChild(menuDtoList, dtoRootList);
                    record.setMenuTree(dtoRootList);

                    List<String> roleSetting = new ArrayList<>();
                    List<String> string = new ArrayList<>();

                    roleStrings(record.getMenuTree(), roleSetting, string);

                    record.setRoleSetting(roleSetting);
                }
        );
        return map;
    }

    /**
     * 构建 roleStrings
     *
     * @param dtoRootList
     * @param roleSetting
     */
    private void roleStrings(List<MenuDto> dtoRootList, List<String> roleSetting, List<String> string) {

        List<List<String>> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(dtoRootList)) {
            return;
        }
        for (MenuDto dto : dtoRootList) {
            string.add(dto.getTitle());
            roleStrings2(dto.getChildren(), string);
            list.add(string);
            list.forEach(ls -> {
                String s = "";
                for (String l : ls) {
                    s += l + "/";
                }
                roleSetting.add(s);
            });

            string.clear();
            list.clear();
        }
    }

    private void roleStrings2(List<MenuDto> dtoRootList, List<String> string) {
        if (CollectionUtils.isEmpty(dtoRootList)) {
            return;
        }
        for (MenuDto dto : dtoRootList) {
            string.add(dto.getTitle());
            roleStrings2(dto.getChildren(), string);
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

    /**
     * 清理缓存
     *
     * @param id /
     */
    public void delCaches(Long id, List<User> users) {
        users = CollectionUtil.isEmpty(users) ? userRepository.findByRoleId(id) : users;
        if (CollectionUtil.isNotEmpty(users)) {
            users.forEach(item -> userCacheClean.cleanUserCache(item.getUsername()));
            Set<Long> userIds = users.stream().map(User::getId).collect(Collectors.toSet());
            redisUtils.delByKeys(CacheKey.DATE_USER, userIds);
            redisUtils.delByKeys(CacheKey.MENU_USER, userIds);
            redisUtils.delByKeys(CacheKey.ROLE_AUTH, userIds);
            redisUtils.del(CacheKey.ROLE_ID + id);
        }

    }
}
