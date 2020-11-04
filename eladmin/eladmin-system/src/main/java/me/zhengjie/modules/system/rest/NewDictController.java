package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.system.domain.NewDict;
import me.zhengjie.modules.system.service.NewDictService;
import me.zhengjie.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "1.数据字典管理")
@RestController
@RequestMapping("/api")
@Slf4j
public class NewDictController extends JwtBaseController {

    @Autowired
    private NewDictService newDictService;

    @Log("数据字典信息")
    @ApiOperation("数据字典信息")
    @GetMapping("/newDict/{pId}")
    public RestResponse newDict(@PathVariable Long pId) {
        List<NewDict> newDictList = newDictService.newDict(pId);
        return RestResponse.success(newDictList);
    }

    @Log("全部数据字典信息")
    @ApiOperation("全部数据字典信息")
    @GetMapping("/newDict")
    public RestResponse newDictAll() {
        List<NewDict> newDictList = newDictService.newDict(null);
        return RestResponse.success(newDictList);
    }
}
