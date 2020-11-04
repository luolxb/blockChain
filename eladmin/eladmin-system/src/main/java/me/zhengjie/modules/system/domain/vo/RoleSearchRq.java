package me.zhengjie.modules.system.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class RoleSearchRq  implements Serializable {

    @ApiModelProperty("角色名称")
    @Query(blurry = "name")
    private String blurry;

    @ApiModelProperty(value = "开始时间，结束时间",hidden = true)
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;


    @ApiModelProperty("开始时间")
    private Long startTime;

    @ApiModelProperty("结束时间")
    private Long endTime;

    @ApiModelProperty("当前页")
    private Integer page = 1;

    @ApiModelProperty("每页大小")
    private Integer size = 10;
}
