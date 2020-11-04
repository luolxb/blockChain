package me.zhengjie.modules.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.system.domain.Company;
import me.zhengjie.modules.system.domain.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CompanyMapper extends BaseMapper<Company> {

    /**
     * 企业信息分页
     *
     * @param page
     * @param companySearchRq
     * @return
     */
    List<CompanyVo> companyPage(Page<CompanyVo> page, @Param("companySearchRq") CompanySearchRq companySearchRq);

    void updateCompany(Company company);

    UserCompanyVo userCompany(@Param("userId") Long userId);

    List<CompanyTokenSearchVo> companyTokenPage(Page<CompanyTokenSearchVo> page,
                                                @Param("companyTokenSearchRq") CompanyTokenSearchRq companyTokenSearchRq,
                                                @Param("userId") Long userId);

    CompanyVo getCompanyByCreateBy(@Param("createBy") String createBy);
}
