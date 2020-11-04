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
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.vo.RoleSearchRq;
import me.zhengjie.modules.system.service.RoleService;
import me.zhengjie.utils.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

/**
 * @author Zheng Jie
 * @date 2018-12-03
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "10.系统：角色管理【服务端】")
@RequestMapping("/api/s/roles")
public class RoleServerController {

    private static final String ENTITY_NAME = "role";
    private final RoleService roleService;

    @Log("角色分页")
    @ApiOperation("角色分页")
    @PostMapping(value = "/page")
    @PreAuthorize("@el.check('roles:list')")
    public RestResponse query(@RequestBody RoleSearchRq roleSearchRq) {
        return RestResponse.success(roleService.rolePage(roleSearchRq));
    }

    @Log("新增角色")
    @ApiOperation("新增角色")
    @PostMapping
    @PreAuthorize("@el.check('roles:add')")
    public RestResponse create(@Valid @RequestBody Role resources, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getFieldError().getDefaultMessage());
        }
        if (resources.getId() != null) {
            throw new BadRequestException("角色ID错误");
        }
//        getLevels(resources.getLevel());
        roleService.create(resources);
        return RestResponse.success();
    }

    //
    @Log("修改角色")
    @ApiOperation("修改角色")
    @PostMapping("/update")
    @PreAuthorize("@el.check('roles:edit')")
    public RestResponse update(@RequestBody Role resources) {
//        getLevels(resources.getLevel());
        roleService.update(resources);
        return RestResponse.success();
    }


    @Log("删除角色")
    @ApiOperation("删除角色")
    @PostMapping("/delete")
    @PreAuthorize("@el.check('roles:del')")
    public RestResponse delete(@RequestBody String idsStr) {
        if (StringUtils.isBlank(idsStr)) {
            throw new BadRequestException("ID不能为空");
        }
        JSONObject jsonObject = JSONObject.parseObject(idsStr);
        List<Integer> ids = (List<Integer>) jsonObject.get("ids");
        HashSet<Long> set = new HashSet<>();
        ids.forEach(id -> {
            if (id == 1) {
                throw new BadRequestException("超级管理员角色不允许操作");
            }
            set.add(Long.parseLong(id + ""));
        });
        // 验证是否被用户关联
        roleService.verification(set);
        roleService.delete(set);
        return RestResponse.success();
    }

}
