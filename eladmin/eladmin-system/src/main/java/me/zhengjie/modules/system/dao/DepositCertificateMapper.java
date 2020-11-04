package me.zhengjie.modules.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.shop.domin.DepositCertificateShopRq;
import me.zhengjie.modules.system.domain.DepositCertificate;
import me.zhengjie.modules.system.domain.vo.AuditOnChainVo;
import me.zhengjie.modules.system.domain.vo.DepositCertificateSearchRq;
import me.zhengjie.modules.system.domain.vo.DepositCertificateVo;
import me.zhengjie.modules.system.eth.dto.CompanyDepositCertificateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Mapper
public interface DepositCertificateMapper extends BaseMapper<DepositCertificate> {
    List<DepositCertificateVo> certificatePage(Page<DepositCertificateVo> page,
                                               @Param("userId") Long userId,
                                               @Param("depositCertificateSearchRq") DepositCertificateSearchRq depositCertificateSearchRq);

    AuditOnChainVo auditChain(Long id);

    CompanyDepositCertificateDto GetDepositCertificateService(Long id);

    List<DepositCertificateVo> certificateShopPage(Page<DepositCertificateVo> page,
                                                   @Param("depositCertificateShopRq") DepositCertificateShopRq depositCertificateShopRq);
}
