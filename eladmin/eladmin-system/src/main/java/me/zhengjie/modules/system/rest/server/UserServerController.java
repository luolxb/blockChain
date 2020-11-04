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
package me.zhengjie.modules.system.rest.server;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.vo.UserPassVo;
import me.zhengjie.modules.system.domain.vo.UserPhoneCodeRq;
import me.zhengjie.modules.system.service.RoleService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.modules.system.service.dto.UserQueryCriteria;
import me.zhengjie.utils.RedisKey;
import me.zhengjie.utils.RedisUtils;
import me.zhengjie.utils.RestResponse;
import me.zhengjie.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Api(tags = "4.系统：用户管理【服务端】")
@RestController
@RequestMapping("/api/s/users")
@RequiredArgsConstructor
public class UserServerController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;
    private final RedisUtils redisUtils;

    @Log("用户分页")
    @ApiOperation("用户分页")
    @PostMapping("/page")
    @PreAuthorize("@el.check('user:list')")
    public RestResponse query(@RequestBody UserQueryCriteria criteria) {
        Sort sort = Sort.by(Sort.Order.desc("createTime"));
        Pageable pageable = PageRequest.of(criteria.getPage() - 1, criteria.getSize(), sort);
        return RestResponse.success(userService.queryAll(criteria, pageable));
    }

    @Log("新增用户")
    @ApiOperation("新增用户")
    @PostMapping
    @PreAuthorize("@el.check('user:add')")
    public RestResponse create(@Valid @RequestBody User resources,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getFieldError().getDefaultMessage());
        }
        String password = StringUtils.isBlank(resources.getPassword()) ? "123456" : resources.getPassword();
        // 默认密码 123456
        resources.setPassword(passwordEncoder.encode(password));
        resources.setIsAdmin(true);
        userService.create(resources);
        return RestResponse.success();
    }


    @Log("修改用户")
    @ApiOperation("修改用户")
    @PostMapping("/update")
    @PreAuthorize("@el.check('user:edit')")
    public RestResponse update(@RequestBody User resources) {
        if (StringUtils.equals("admin", resources.getUsername()) && !StringUtils.equals("admin", SecurityUtils.getCurrentUsername())) {
            throw new BadRequestException("角色权限不足，不能修改超级管理员");
        }
//        checkLevel(resources);
        userService.update(resources);
        return RestResponse.success();
    }


    @Log("删除用户")
    @ApiOperation("删除用户")
    @PostMapping("/delete")
    @PreAuthorize("@el.check('user:del')")
    public RestResponse delete(@RequestBody String idsStr) {

        if (StringUtils.isBlank(idsStr)) {
            throw new BadRequestException("ID不能为空");
        }
        JSONObject jsonObject = JSONObject.parseObject(idsStr);
        List<Integer> ids = (List<Integer>) jsonObject.get("ids");
        HashSet<Long> set = new HashSet<>();
        ids.forEach(id -> {
            Long parseLong = Long.parseLong(id + "");
            set.add(parseLong);

            Long currentUserId = SecurityUtils.getCurrentUserId();
            if (parseLong.equals(currentUserId)) {
                throw new BadRequestException("不能删除当前登录用户");
            }
            UserDto userDto = userService.findById(id);
            if (StringUtils.equals(userDto.getUsername(), "admin")) {
                throw new BadRequestException("不能删除超级管理员");
            }
//            Integer currentLevel = Collections.min(roleService.findByUsersId(SecurityUtils.getCurrentUserId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
//            Integer optLevel = Collections.min(roleService.findByUsersId(id).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
//            if (currentLevel > optLevel) {
//                throw new BadRequestException("角色权限不足，不能删除：" + userService.findById(id).getUsername());
//            }
        });

        userService.delete(set);
        return RestResponse.success();
    }

    @ApiOperation("修改密码")
    @PostMapping(value = "/updatePass")
    @PreAuthorize("@el.check('users:updatePass')")
    public RestResponse updatePass(@Valid @RequestBody UserPassVo passVo,
                                   BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getFieldError().getDefaultMessage());
        }
        String str = (String) redisUtils.get(RedisKey.SMS_SEND + passVo.getMobile());
        if (str == null) {
            throw new BadRequestException("验证码不正确");
        } else if (!org.apache.commons.lang3.StringUtils.equals(passVo.getValidCode(), str)) {
            throw new BadRequestException("验证码不正确");
        }
        UserDto user = userService.findByName(SecurityUtils.getCurrentUsername());
//        if (!passwordEncoder.matches(passVo.getOldPass(), user.getPassword())) {
//            throw new BadRequestException("修改失败，旧密码错误");
//        }


        // 根据旧手机查询用户是否存在
        UserDto byPhone = userService.findByPhone(passVo.getMobile());
        if (byPhone == null) {
            throw new BadRequestException("手机号错误");
        }
        if (!byPhone.getId().equals(user.getId())) {
            throw new BadRequestException("用户手机号输入错误");
        }
        if (passwordEncoder.matches(passVo.getNewPass(), user.getPassword())) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        userService.updatePass(user.getUsername(), passwordEncoder.encode(passVo.getNewPass()));
        return RestResponse.success();
    }


    @ApiOperation("修改手机号")
    @PostMapping(value = "/updatePhone")
    @PreAuthorize("@el.check('users:updatePhone')")
    public RestResponse updatePass(@Valid @RequestBody UserPhoneCodeRq userPhoneCodeRq,
                                   BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getFieldError().getDefaultMessage());
        }

        // 根据旧手机查询用户是否存在
        UserDto byPhone = userService.findByPhone(userPhoneCodeRq.getMobile());

        UserDto user1 = userService.findByName(SecurityUtils.getCurrentUsername());

        if (!user1.getId().equals(byPhone.getId())) {
            throw new BadRequestException("用户旧手机号输入错误");
        }

        // 校验旧验证码
        String str = (String) redisUtils.get(RedisKey.SMS_SEND + userPhoneCodeRq.getMobile());
        if (str == null) {
            throw new BadRequestException(userPhoneCodeRq.getMobile() + "验证码不正确");
        } else if (!org.apache.commons.lang3.StringUtils.equals(userPhoneCodeRq.getValidCode(), str)) {
            throw new BadRequestException(userPhoneCodeRq.getMobile() + "验证码不正确");
        }

        User byPhone2 = userService.findByPhone2(userPhoneCodeRq.getNewmobile());
        if (null != byPhone2) {
            throw new BadRequestException(userPhoneCodeRq.getNewmobile() + "已被使用");
        }

        String str1 = (String) redisUtils.get(RedisKey.SMS_SEND + userPhoneCodeRq.getNewmobile());
        if (str1 == null) {
            throw new BadRequestException(userPhoneCodeRq.getNewmobile() + "验证码不正确");
        } else if (!org.apache.commons.lang3.StringUtils.equals(userPhoneCodeRq.getNewvalidCode(), str1)) {
            throw new BadRequestException(userPhoneCodeRq.getNewmobile() + "验证码不正确");
        }

        User user = new User();
        user.setPhone(userPhoneCodeRq.getNewmobile());
        user.setId(byPhone.getId());
        user.setPwdResetTime(new Date());
        userService.updatePhone(user.getId(), user.getPwdResetTime(), user.getPhone(), byPhone.getUsername());

        redisUtils.del(RedisKey.SMS_SEND + userPhoneCodeRq.getMobile());
        return RestResponse.success();
    }
}
