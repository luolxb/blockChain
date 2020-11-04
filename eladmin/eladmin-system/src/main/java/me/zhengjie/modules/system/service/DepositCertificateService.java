package me.zhengjie.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.shop.domin.DepositCertificateShopRq;
import me.zhengjie.modules.system.domain.DepositCertificate;
import me.zhengjie.modules.system.domain.vo.*;
import me.zhengjie.modules.system.eth.dto.CompanyDepositCertificateDto;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.BlockRestResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface DepositCertificateService extends IService<DepositCertificate> {
    void create(DepositCertificateRq depositCertificateRq, UserDto userDto);

    Page<DepositCertificateVo> certificatePage(Pageable pageable, UserDto userDto, DepositCertificateSearchRq depositCertificateSearchRq);

    void edit(DepositCertificateRq depositCertificateRq, UserDto userDto);

    void editS(DepositCertificateServerRq depositCertificateServerRq, UserDto userDto);

    void appliChain(DepositCertificateRq depositCertificateRq, UserDto userDto);

    void audit(DepositCertificateAuditRq depositCertificateAuditRq, UserDto userDto);

    void onChainCallBack(BlockRestResponse blockRestResponse);

    AuditOnChainVo auditChain(Long id);

    CompanyDepositCertificateDto GetDepositCertificateService(Long id);

    void pushChainTask();

    DepositCertificateVo detail(Long id,UserDto userDto);

    List<DepositCertificateVo> depositCertificateAll(DepositCertificateSearchRq depositCertificateSearchRq);

    String download(List<DepositCertificateVo> depositCertificateAll, HttpServletResponse response) throws IOException;

    Page<DepositCertificateVo> certificateShopPage(DepositCertificateShopRq depositCertificateShopRq);

    DepositCertificateVo certificateShopDetail(Long id);

}
