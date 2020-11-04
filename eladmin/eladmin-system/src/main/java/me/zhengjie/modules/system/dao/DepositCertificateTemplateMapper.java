package me.zhengjie.modules.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.system.domain.DepositCertificateTemplate;
import me.zhengjie.modules.system.domain.vo.DepositCertificateTemplateVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DepositCertificateTemplateMapper extends BaseMapper<DepositCertificateTemplate> {
    List<DepositCertificateTemplateVo> templatePage(Page<DepositCertificateTemplateVo> page);
}
