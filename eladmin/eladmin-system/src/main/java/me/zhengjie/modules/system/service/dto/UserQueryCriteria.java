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
package me.zhengjie.modules.system.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Data
public class UserQueryCriteria implements Serializable {

//    @Query
//    private Long id;
//
//    @Query(propName = "id", type = Query.Type.IN, joinName = "dept")
//    private Set<Long> deptIds = new HashSet<>();

    @ApiModelProperty("搜索条件")
    @Query(blurry = "username,nickName,phone")
    private String blurry;

    @Query
    @ApiModelProperty(hidden = true)
    private Boolean isAdmin;

    @ApiModelProperty("开始时间")
    private Long startTime;

    @ApiModelProperty("结束时间")
    private Long endTime;

//    private Long deptId;

    @ApiModelProperty(hidden = true)
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;


    @ApiModelProperty("当前页")
    private Integer page = 1;

    @ApiModelProperty("每页大小")
    private Integer size = 10;
}
