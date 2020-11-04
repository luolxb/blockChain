package me.zhengjie.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.system.dao.NewDictMapper;
import me.zhengjie.modules.system.domain.NewDict;
import me.zhengjie.modules.system.service.NewDictService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class NewDictServiceImpl extends ServiceImpl<NewDictMapper, NewDict> implements NewDictService {


    /**
     * 查询全部数据字典信息
     *
     * @return
     */
    @Override
    public List<NewDict> newDict(Long pId) {
        QueryWrapper<NewDict> newDictQueryWrapper = new QueryWrapper<>();
        if (null != pId) {
            newDictQueryWrapper
                    .eq("id", pId)
                    .or()
                    .eq("p_id", pId);
        }
        newDictQueryWrapper.eq("enabled", 1);
        newDictQueryWrapper.eq("del_flag", 1);
        List<NewDict> newDicts = this.baseMapper.selectList(newDictQueryWrapper);
        List<NewDict> newDictRootList = newDicts
                .stream()
                .filter(newDict ->
                        !newDicts.stream().map(NewDict::getId).collect(Collectors.toList()).contains(newDict.getPId()))
                .collect(Collectors.toList());

        getChild(newDicts, newDictRootList);
        return newDictRootList;
    }

    private void getChild(List<NewDict> newDicts, List<NewDict> newDictRootList) {
        newDictRootList.forEach(newDictRoot -> {
            List<NewDict> list = newDicts
                    .stream()
                    .filter(newDict ->
                            newDict.getPId().equals(newDictRoot.getId()))
                    .collect(Collectors.toList());
            if (list.size() > 0) {
                newDictRoot.setNewDictChild(list);
                getChild(newDicts, list);
            }
        });
    }
}
