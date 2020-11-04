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
package me.zhengjie.modules.security.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.config.bean.LoginProperties;
import me.zhengjie.modules.security.config.bean.SecurityProperties;
import me.zhengjie.modules.security.security.TokenProvider;
import me.zhengjie.modules.security.service.OnlineUserService;
import me.zhengjie.modules.security.service.UserDetailsServiceImpl;
import me.zhengjie.modules.security.service.dto.AuthUserDto;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.domain.vo.UserCodeRq;
import me.zhengjie.modules.system.domain.vo.UserLoginCodeRq;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.RedisKey;
import me.zhengjie.utils.RedisUtils;
import me.zhengjie.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags = "0.系统：系统授权接口")
public class AuthorizationController {
    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    @Resource
    private LoginProperties loginProperties;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsServiceImpl userDetailsService;


    @Log("验证码注册【客户端】")
    @ApiOperation("验证码注册【客户端】")
    @AnonymousPostMapping("/code/register")
    public RestResponse createCode(@Valid @RequestBody UserCodeRq userCodeRq,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getFieldError().getDefaultMessage());
        }
        userService.createCode(userCodeRq);
        return RestResponse.success();
    }

    @Log("验证码登录【客户端】")
    @ApiOperation("验证码登录【客户端】")
    @AnonymousPostMapping("/code/login")
    public RestResponse codeLogin(@Valid @RequestBody UserLoginCodeRq userCodeRq,
                                  BindingResult bindingResult,
                                  HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getFieldError().getDefaultMessage());
        }

        // 验证验证码
        // 验证验证码是否正确
        String str = (String) redisUtils.get(RedisKey.SMS_SEND + userCodeRq.getMobile());
        if (str == null) {
            throw new BadRequestException("验证码不正确");
        } else if (!org.apache.commons.lang3.StringUtils.equals(userCodeRq.getValidCode(), str)) {
            throw new BadRequestException("验证码不正确");
        }

        // 判断用户名是否存在
        UserDto byPhone = userService.findByPhone(userCodeRq.getMobile());
        JwtUserDto jwtUserDto = userDetailsService.loadUserByUsername(byPhone.getUsername());

        // 生成令牌
        String token = tokenProvider.createToken2(jwtUserDto);
//        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        // 客户端不允许管理端登录
        if (jwtUserDto.getUser().getIsAdmin()) {
            throw new BadRequestException("登录失败,请确认登录信息");
        }
        // 保存在线信息
        onlineUserService.save(jwtUserDto, token, request);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUserDto);
        }};
        if (loginProperties.isSingleLogin()) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(userCodeRq.getMobile(), token);
        }

        redisUtils.del(RedisKey.SMS_SEND + userCodeRq.getMobile());

        // 修改最后登录时间
        userService.updateByLastLogin(jwtUserDto);
        return RestResponse.success(authInfo);
    }

    @Log("用户登录【客户端】")
    @ApiOperation("登录授权【客户端】")
    @AnonymousPostMapping(value = "/login")
    public RestResponse login(@Valid @RequestBody AuthUserDto authUser,
                              BindingResult bindingResult,
                              HttpServletRequest request) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getFieldError().getDefaultMessage());
        }
        String password = authUser.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        String token = tokenProvider.createToken(authentication);
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        // 客户端不允许管理端登录
        if (jwtUserDto.getUser().getIsAdmin()) {
            throw new BadRequestException("登录失败,请确认登录信息");
        }
        // 保存在线信息
        onlineUserService.save(jwtUserDto, token, request);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUserDto);
        }};
        if (loginProperties.isSingleLogin()) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(authUser.getUsername(), token);
        }

        // 修改最后登录时间
        userService.updateByLastLogin(jwtUserDto);
        return RestResponse.success(authInfo);
    }

//    @ApiOperation("获取用户信息")
//    @GetMapping(value = "/info")
//    public ResponseEntity<Object> getUserInfo() {
//        return ResponseEntity.ok(SecurityUtils.getCurrentUser());
//    }
//
//    @ApiOperation("获取验证码")
//    @AnonymousGetMapping(value = "/code")
//    public ResponseEntity<Object> getCode() {
//        // 获取运算的结果
//        Captcha captcha = loginProperties.getCaptcha();
//        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
//        // 保存
//        redisUtils.set(uuid, captcha.text(), loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
//        // 验证码信息
//        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
//            put("img", captcha.toBase64());
//            put("uuid", uuid);
//        }};
//        return ResponseEntity.ok(imgResult);
//    }

    @ApiOperation("退出登录")
    @AnonymousPostMapping(value = "/logout")
    public RestResponse logout(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
        return RestResponse.success();
    }

    @Log("用户登录【服务端】")
    @ApiOperation("登录授权【服务端】")
    @AnonymousPostMapping(value = "/s/login")
    public RestResponse loginServer(@Valid @RequestBody AuthUserDto authUser,
                                    BindingResult bindingResult,
                                    HttpServletRequest request) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getFieldError().getDefaultMessage());
        }
        String password = authUser.getPassword();
        // 密码解密
//        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
//        // 查询验证码
//        String code = (String) redisUtils.get(authUser.getUuid());
//        // 清除验证码
//        redisUtils.del(authUser.getUuid());
//        if (StringUtils.isBlank(code)) {
//            throw new BadRequestException("验证码不存在或已过期");
//        }
//        if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
//            throw new BadRequestException("验证码错误");
//        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        String token = tokenProvider.createToken(authentication);
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        if (!jwtUserDto.getUser().getIsAdmin()) {
            throw new BadRequestException("登录失败,请确认登录信息");
        }
        // 保存在线信息
        onlineUserService.save(jwtUserDto, token, request);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUserDto);
        }};
        if (loginProperties.isSingleLogin()) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(authUser.getUsername(), token);
        }

        // 修改最后登录时间
        userService.updateByLastLogin(jwtUserDto);
        return RestResponse.success(authInfo);
    }

}
