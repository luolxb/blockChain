/*******************************************************************************
 * @(#)JwtBaseController.java 2019-05-12
 *
 * Copyright 2019 Liaoning RNS Technology Co., Ltd.All rights reserved.
 * RNS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *******************************************************************************/
package me.zhengjie.modules.system.rest;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.security.security.TokenProvider;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * </p>
 */
@Log
@Slf4j
public class JwtBaseController {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserService userService;


    @ModelAttribute("userDto")
    public UserDto common(HttpServletRequest request, HttpServletResponse response) {
        String token = tokenProvider.getToken(request);
        String username = null;
        try {
            username = tokenProvider.getUsernameFromToken(token);
        } catch (Exception e) {
            log.info(e.getMessage());

        }
        return userService.findByName(username);
    }

}
