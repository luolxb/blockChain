package me.zhengjie.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.system.domain.Company;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.vo.*;
import me.zhengjie.modules.system.service.dto.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface CompanyService extends IService<Company> {

    void create(CompanyRq companyRq, UserDto userDto);

    void createServer(CompanyRq companyRq, UserDto userDto);

    void delete(String ids);

    Page<CompanyVo> companyPage(Pageable pageable, CompanySearchRq companySearchRq);

    void updateCompany(CompanyRq companyRq, UserDto userDto);

    void audit(CompanyAuditRq companyAuditRq, UserDto userDto);

    UserCompanyVo userCompany(UserDto userDto);

    Page<CompanyTokenSearchVo> companyTokenPage(Pageable pageable, CompanyTokenSearchRq companyTokenSearchRq, UserDto userDto);

    String walletAddress(UserDto userDto) throws Exception;

    String privateKey(UserDto userDto, Long companyId) throws Exception;

    boolean checkCompany(UserDto userDto);

    CompanyVo detail(Long id);

    /**
     * 导出数据
     *
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    String download(List<CompanyVo> queryAll, HttpServletResponse response) throws IOException;

    List<CompanyVo> companyAll(CompanySearchRq companySearchRq);

    List<CompanyTokenSearchVo> companyTokenAll(CompanyTokenSearchRq companyTokenSearchRq);

    String downloadCompanyToken(List<CompanyTokenSearchVo> companyTokenAll, HttpServletResponse response) throws IOException;

    CompanyVo getCompanyByCreateBy(String createBy);
}
