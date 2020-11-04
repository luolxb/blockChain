package me.zhengjie.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.domain.LocalStorage;
import me.zhengjie.modules.system.domain.AttachmentRela;

import java.util.List;

public interface AttachmentRelaService extends IService<AttachmentRela> {

    /**
     * 根据关联ID 获取附件
     *
     * @param relaId
     * @return
     */
    List<LocalStorage> getLocalStorage(Long relaId, String relaType, Integer attachmentType);
}
