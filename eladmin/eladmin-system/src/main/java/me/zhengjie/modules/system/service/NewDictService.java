package me.zhengjie.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.system.domain.NewDict;

import java.util.List;


public interface NewDictService extends IService<NewDict> {

    List<NewDict> newDict(Long pId);
}
