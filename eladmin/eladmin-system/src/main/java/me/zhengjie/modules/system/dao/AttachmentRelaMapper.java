package me.zhengjie.modules.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.domain.LocalStorage;
import me.zhengjie.modules.system.domain.AttachmentRela;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AttachmentRelaMapper extends BaseMapper<AttachmentRela> {

    List<LocalStorage> getLocalStorage(@Param("relaId") Long relaId,
                                       @Param("relaType") String relaType,
                                       @Param("attachmentType") Integer attachmentType);
}
