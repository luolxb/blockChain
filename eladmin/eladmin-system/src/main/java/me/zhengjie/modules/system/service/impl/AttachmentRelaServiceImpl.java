package me.zhengjie.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.LocalStorage;
import me.zhengjie.modules.system.dao.AttachmentRelaMapper;
import me.zhengjie.modules.system.domain.AttachmentRela;
import me.zhengjie.modules.system.service.AttachmentRelaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AttachmentRelaServiceImpl extends ServiceImpl<AttachmentRelaMapper, AttachmentRela> implements AttachmentRelaService {
    @Override
    public List<LocalStorage> getLocalStorage(Long relaId, String relaType, Integer attachmentType) {
        return this.baseMapper.getLocalStorage(relaId, relaType, attachmentType);
    }
}
