package me.zhengjie.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.system.domain.DepositCertificateTemplate;
import me.zhengjie.modules.system.domain.vo.DepositCertificateTemplateRq;
import me.zhengjie.modules.system.domain.vo.DepositCertificateTemplateVo;
import me.zhengjie.modules.system.service.dto.UserDto;
import org.springframework.data.domain.Pageable;

public interface DepositCertificateTemplateServie extends IService<DepositCertificateTemplate> {
    void create(DepositCertificateTemplateRq depositCertificateTemplateRq, UserDto userDto);

    Page<DepositCertificateTemplateVo> templatePage(Pageable pageable);

    void edit(DepositCertificateTemplateRq depositCertificateTemplateRq, UserDto userDto);

    void del(Long id, UserDto userDto);

}
