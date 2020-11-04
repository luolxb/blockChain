package me.zhengjie.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.LocalStorage;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.shop.service.ShopService;
import me.zhengjie.modules.system.dao.CompanyMapper;
import me.zhengjie.modules.system.domain.Company;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.UserCompany;
import me.zhengjie.modules.system.domain.vo.*;
import me.zhengjie.modules.system.eth.entity.EntityBlock;
import me.zhengjie.modules.system.eth.service.BlockServiceImpl;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.CompanyService;
import me.zhengjie.modules.system.service.UserCompanyService;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.repository.LocalStorageRepository;
import me.zhengjie.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author Administrator
 */
@Service
@CacheConfig(cacheNames = "company")
@Slf4j
@RequiredArgsConstructor
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

    private final LocalStorageRepository localStorageRepository;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RedisUtils redisUtils;

    @Autowired
    private UserCompanyService userCompanyService;
    @Autowired
    private BlockServiceImpl blockService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ExcelUtil excelUtil;

    /**
     * 新增企业
     *
     * @param companyRq
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CompanyRq companyRq, UserDto userDto) {
        // 判断存证是否已经存证了
        checkCreateParam(companyRq);
        Company company = new Company();
        // 对象拷贝
        BeanUtils.copyProperties(companyRq, company);
        company.setCreateBy(userDto.getUsername());
        company.setCreateTime(new Date());
        company.setCode(userDto.getCode());
        // 保存到数据库
        boolean save = this.save(company);
        if (!save) {
            throw new BadRequestException("新增企业失败");
        }
        // 新增用户企业关联
        UserCompany userCompany = new UserCompany().setCompanyId(company.getId()).setUserId(userDto.getId());
        boolean save1 = userCompanyService.save(userCompany);
        if (!save1) {
            throw new BadRequestException("新增企业失败");
        }
    }

    /**
     * 新增企业 服务端
     *
     * @param companyRq
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createServer(CompanyRq companyRq, UserDto userDto) {
        // 判断存证是否已经存证了
        checkCreateParam(companyRq);
        // 新增用户 手机号  密码默认 123456
        User user = new User();
        user.setUsername(companyRq.getCompanyPhone());
        String pass = passwordEncoder.encode(companyRq.getCompanyPhone());
        user.setPassword(pass);
//        user.setEmail(companyRq.getCompanyPhone() + "@163.com");
        user.setNickName(companyRq.getCompanyPhone());
        user.setEnabled(true);
        user.setPhone(companyRq.getCompanyPhone());

        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new BadRequestException("手机号已被使用");
        }
        if (userRepository.findByPhone(user.getPhone()) != null) {
            throw new BadRequestException("手机号已被使用");
        }
//        if (userRepository.findByEmail(user.getEmail()) != null) {
//            throw new EntityExistException(User.class, "email", user.getEmail());
//        }
        User user1 = userRepository.save(user);

        Company company = new Company();
        // 对象拷贝
        BeanUtils.copyProperties(companyRq, company);
        company.setAuditStatus(2);
        company.setCreateBy(userDto.getUsername());
        company.setCreateTime(new Date());
        // 保存到数据库
        boolean save = this.save(company);
        if (!save) {
            throw new BadRequestException("新增企业失败");
        }
        // 新增用户企业关联
        UserCompany userCompany = new UserCompany().setCompanyId(company.getId()).setUserId(user1.getId());
        boolean save1 = userCompanyService.save(userCompany);
        if (!save1) {
            throw new BadRequestException("新增企业失败");
        }
        String md5Pass = Md5Util.md5Encode("TPSHOP", companyRq.getCompanyPhone());
        User user2 = new User();
        BeanUtils.copyProperties(user1,user2);
        user2.setPassword(md5Pass);

        shopService.sendUserCompany2Shop(user2, company);
    }


    /**
     * 删除企业信息
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new BadRequestException("ID不能为空");
        }
        String[] split = ids.split(",");
        if (split.length <= 0) {
            throw new BadRequestException("ID不能为空");
        }
        List<String> list = Arrays.asList(split);
        // 检查删除是否可以删除企业
        checkDelete(list);
        int i = this.baseMapper.deleteBatchIds(list);
        if (i <= 0) {
            throw new BadRequestException("删除企业失败");
        }


        // 删除用户企业关联
        list.forEach(id -> {
                    boolean company_id = userCompanyService.remove(new QueryWrapper<UserCompany>().eq("company_id", id));
                    if (!company_id) {
                        throw new BadRequestException("删除企业失败");
                    }
                }
        );
    }

    /**
     * 检查删除是否可以删除企业
     *
     * @param list
     */
    private void checkDelete(List<String> list) {
        list.forEach(id -> {
            // 与用户关联
            QueryWrapper<UserCompany> companyQueryWrapper = new QueryWrapper<UserCompany>().eq("company_id", id);
            List<UserCompany> userCompanyList = userCompanyService.list(companyQueryWrapper);
            if (!CollectionUtils.isEmpty(userCompanyList)) {
                throw new BadRequestException("删除失败,该企业有用户存在");
            }
        });
    }

    /**
     * 企业信息分页
     *
     * @param pageable
     * @param companySearchRq
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CompanyVo> companyPage(Pageable pageable, CompanySearchRq companySearchRq) {
        Integer size = companySearchRq.getSize();
        Integer pageNumber = companySearchRq.getPage();
        Page<CompanyVo> page = new Page<>(pageNumber, size);

        companySearchRq.setStartTimeStr(DateUtil.getFormatDate(companySearchRq.getStartTime(), "yyyy-MM-dd"));
        companySearchRq.setEndTimeStr(DateUtil.getFormatDate(companySearchRq.getEndTime(), "yyyy-MM-dd"));
        List<CompanyVo> companyList = this.baseMapper.companyPage(page, companySearchRq);
        companyList.forEach(company -> {
            // 获取图片信息
            LocalStorage logoPic = localStorageRepository.findById(company.getLogo()).orElseGet(LocalStorage::new);
            company.setLogoPic(logoPic);
            LocalStorage businessLicensePic = localStorageRepository.findById(company.getBusinessLicense()).orElseGet(LocalStorage::new);
            company.setBusinessLicensePic(businessLicensePic);
        });
        page.setRecords(companyList);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCompany(CompanyRq companyRq, UserDto userDto) {
        // 校验修改参数
        checkUpdateParam(companyRq);
        Company company = new Company();
        BeanUtils.copyProperties(companyRq, company);
        company.setUpdateBy(userDto.getUsername());
        company.setCreateTime(new Date());
        company.setCode(userDto.getCode());

//        UserCompany userCompany = userCompanyService.getOne(new QueryWrapper<UserCompany>().eq("company_id", companyRq.getId()));
//        if (userCompany == null) {
//            throw new BadRequestException("企业信息不存在");
//        }
//        User user = userRepository.findById(userCompany.getUserId()).orElseGet(User::new);
//        if (user == null) {
//            throw new BadRequestException("企业用户不存在");
//        }
//        User username = userRepository.findByUsername(companyRq.getCompanyPhone());
//        if ( username != null && !StringUtils.equals(username.getUsername(),user.getUsername())) {
//            throw new BadRequestException("手机号已被使用");
//        }
//
//        User phone = userRepository.findByPhone(companyRq.getCompanyPhone());
//        if ( phone != null && !StringUtils.equals(phone.getPhone(),user.getPhone())) {
//            throw new BadRequestException("手机号已被使用");
//        }
//        // 修改用户
//        User user1 = new User();
//        user1.setId(userCompany.getUserId());
//        user1.setPhone(companyRq.getCompanyPhone());
//        user1.setUsername(companyRq.getCompanyPhone());
//        user1.setNickName(companyRq.getCompanyPhone());
//        userRepository.save(user1);

        this.baseMapper.updateCompany(company);
    }


    /**
     * 审核商户信息
     *
     * @param companyAuditRq
     * @param userDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(CompanyAuditRq companyAuditRq, UserDto userDto) {
        if (CollectionUtils.isEmpty(companyAuditRq.getIds())) {
            throw new BadRequestException("商户ID不能为空");
        }
        Date date = new Date();
        companyAuditRq.getIds().forEach(id -> {

            Company byId = this.getById(id);
            if (byId.getAuditStatus() == 2 || byId.getStatus() == 2) {
                throw new BadRequestException("商户状态错误");
            }
            Company company = new Company();
            company.setAuditStatus(companyAuditRq.getAuditStatus());
            company.setAuditTime(date);
            company.setId(id);
            company.setUpdateBy(userDto.getUsername());
            company.setUpdateTime(date);
            company.setAuditBy(userDto.getUsername());

            this.baseMapper.updateById(company);

            // 如果审批通过就发送到商城
            if (companyAuditRq.getAuditStatus() == 2) {
                log.info("----------------开始发送到商城---------------------------");
                UserCompanyVo userCompanyVo = userCompanyService.getUserCompanyVoByCompanyId(id);
                // 从缓存获取发送到商城的用户信息
                User user = (User) redisUtils.get(RedisKey.SEND_SHOP_USER_INFO + userCompanyVo.getUsername());
                shopService.sendUserCompany2Shop(user, company);
                redisUtils.del(RedisKey.SEND_SHOP_USER_INFO + userCompanyVo.getUsername());
            }
        });
    }

    /**
     * 账户企业信息
     *
     * @param userDto
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public UserCompanyVo userCompany(UserDto userDto) {
        UserCompanyVo userCompanyVo = this.baseMapper.userCompany(userDto.getId());

        // 获取图片信息
        if (userCompanyVo.getLogo() != null) {
            LocalStorage logoPic = localStorageRepository.findById(userCompanyVo.getLogo()).orElseGet(LocalStorage::new);
            userCompanyVo.setLogoPic(logoPic);
            LocalStorage businessLicensePic = localStorageRepository.findById(userCompanyVo.getBusinessLicense()).orElseGet(LocalStorage::new);
            userCompanyVo.setBusinessLicensePic(businessLicensePic);
        }
        return userCompanyVo;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompanyTokenSearchVo> companyTokenPage(Pageable pageable, CompanyTokenSearchRq companyTokenSearchRq, UserDto userDto) {
        Integer size = companyTokenSearchRq.getSize();
        Integer pageNumber = companyTokenSearchRq.getPage();
        Page<CompanyTokenSearchVo> page = new Page<>(pageNumber, size);
        List<CompanyTokenSearchVo> companyTokenSearchVoList = this.baseMapper.companyTokenPage(page, companyTokenSearchRq, null);
        page.setRecords(companyTokenSearchVoList);

        return page;
    }

    /**
     * 获取钱包地址
     *
     * @param userDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String walletAddress(UserDto userDto) throws Exception {
        // 判断用户企业钱包是否存在，如果存在直接返回，如果不存在就从区块链获取钱包
        boolean flag = false;
        UserCompany one = userCompanyService.getOne(new QueryWrapper<UserCompany>().eq("user_id", userDto.getId()));
        if (null == one) {
            flag = true;
        } else {
            Company company = this.getById(one.getCompanyId());
            if (null == company || StringUtils.isBlank(company.getWalletAddress())) {
                flag = true;
            } else if (StringUtils.isNotBlank(company.getWalletAddress())) {
                return company.getWalletAddress();
            }
        }
        String address = null;
        if (flag) {
            // 从区块链获取钱包
            EntityBlock entityBlock = blockService.getWalletAddress();
            address = entityBlock.getAddress();
        }
        return address;
    }

    /**
     * 根据钱包地址获取私钥
     *
     * @param userDto
     * @param companyId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String privateKey(UserDto userDto, Long companyId) throws Exception {
        Company company = this.getById(companyId);
        if (null == company) {
            throw new BadRequestException("企业信息不存在");
        }
        // 如果数据库已经存在了私钥 直接返回
        if (StringUtils.isNotBlank(company.getPrivateKey())) {
            return company.getPrivateKey();
        }
        // 如果不存在调用区块链服务生产
        // 根据钱包地址
        EntityBlock entityBlock = blockService.getPrivateKey(company.getWalletAddress());

        // 修改企业信息 新增私钥
        Company company1 = new Company();
        company1.setId(companyId);
        company1.setPrivateKey(entityBlock.getPrivateKey());
        boolean b = this.updateById(company1);
        if (!b) {
            throw new BadRequestException("获取私钥失败");
        }
        return entityBlock.getPrivateKey();
    }

    /**
     * 判断用户企业信息是否完善
     *
     * @param userDto
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkCompany(UserDto userDto) {
        // 根据用户ID 获取用户企业关联
        UserCompany one = userCompanyService.getOne(new QueryWrapper<UserCompany>().eq("user_id", userDto.getId()));
        if (null == one) {
            log.error("获取用户企业关联 不存在");
            return false;
        }
        Company company = this.getById(one.getCompanyId());
        if (null == company) {
            log.error("获取用户企业 不存在");
            return false;
        }
//
//        if (StringUtils.isBlank(company.getCompanyName())|| StringUtils.isBlank(company.getCompanyType())) {
//            log.error("用户企业信息不完整");
//            return false;
//        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyVo detail(Long id) {
        CompanySearchRq companySearchRq = new CompanySearchRq();
        companySearchRq.setId(id);
        List<CompanyVo> companyList = this.baseMapper.companyPage(null, companySearchRq);
        companyList.forEach(company -> {
            // 获取图片信息
            LocalStorage logoPic = localStorageRepository.findById(company.getLogo()).orElseGet(LocalStorage::new);
            company.setLogoPic(logoPic);
            LocalStorage businessLicensePic = localStorageRepository.findById(company.getBusinessLicense()).orElseGet(LocalStorage::new);
            company.setBusinessLicensePic(businessLicensePic);
        });
        return companyList.get(0);

    }


    /**
     * 校验修改参数
     *
     * @param companyRq
     */
    private void checkUpdateParam(CompanyRq companyRq) {
        if (null == companyRq.getId()) {
            throw new BadRequestException("商户ID不能为空");
        }
        Company company = this.baseMapper.selectById(companyRq.getId());
        if (company == null) {
            throw new BadRequestException("商户信息不存在");
        }
        Company companyName = this.baseMapper.selectOne(new QueryWrapper<Company>().eq("company_name", companyRq.getCompanyName()));
        if (companyName != null && !StringUtils.equals(company.getCompanyName(), companyName.getCompanyName())) {
            throw new BadRequestException("商户名称已经存在");
        }

        Company socialCreditCode = this.baseMapper.selectOne(new QueryWrapper<Company>().eq("social_credit_code", companyRq.getSocialCreditCode()));
        if (socialCreditCode != null && !StringUtils.equals(company.getSocialCreditCode(), socialCreditCode.getSocialCreditCode())) {
            throw new BadRequestException("信用代码已经存在");
        }

        Company address = this.baseMapper.selectOne(new QueryWrapper<Company>().eq("address", companyRq.getAddress()));
        if (address != null && !StringUtils.equals(company.getAddress(), address.getAddress())) {
            throw new BadRequestException("企业地址已经存在");
        }

        Company phone = this.baseMapper.selectOne(new QueryWrapper<Company>().eq("company_phone", companyRq.getCompanyPhone()));
        if (phone != null && !StringUtils.equals(company.getCompanyPhone(), phone.getCompanyPhone())) {
            throw new BadRequestException("企业电话已经存在");
        }

        Company walletAddress = this.baseMapper.selectOne(new QueryWrapper<Company>().eq("wallet_address", companyRq.getWalletAddress()));
        if (walletAddress != null && !StringUtils.equals(company.getWalletAddress(), walletAddress.getWalletAddress())) {
            throw new BadRequestException("钱包地址已经存在");
        }
        boolean phone1 = NtsUtil.isPhone(companyRq.getCompanyPhone());
        if (!phone1) {
            throw new BadRequestException("手机格式不正确");
        }

    }

    /**
     * 判断存证是否已经企业
     *
     * @param companyRq
     */
    private void checkCreateParam(CompanyRq companyRq) {
        Company companyName = this.baseMapper.selectOne(new QueryWrapper<Company>().eq("company_name", companyRq.getCompanyName()));
        if (null != companyName) {
            throw new BadRequestException("企业名称已经存在");
        }
        Company socialCreditCode = this.baseMapper.selectOne(new QueryWrapper<Company>().eq("social_credit_code", companyRq.getSocialCreditCode()));
        if (null != socialCreditCode) {
            throw new BadRequestException("社会信用代码已经存在");
        }
        Company address = this.baseMapper.selectOne(new QueryWrapper<Company>().eq("address", companyRq.getAddress()));
        if (null != address) {
            throw new BadRequestException("企业地址已经存在");
        }
        Company phone = this.baseMapper.selectOne(new QueryWrapper<Company>().eq("company_phone", companyRq.getCompanyPhone()));
        if (null != phone) {
            throw new BadRequestException("企业电话已经存在");
        }
        Company walletAddress = this.baseMapper.selectOne(new QueryWrapper<Company>().eq("wallet_address", companyRq.getWalletAddress()));
        if (null != walletAddress) {
            throw new BadRequestException("钱包地址已经存在");
        }
        boolean phone1 = NtsUtil.isPhone(companyRq.getCompanyPhone());
        if (!phone1) {
            throw new BadRequestException("手机格式不正确");
        }
    }

    @Override
    public String download(List<CompanyVo> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CompanyVo companyVo : queryAll) {
            if (companyVo.getAuditStatus() == 1) {
                companyVo.setAuditStatusStr("待审核");
            } else if (companyVo.getAuditStatus() == 2) {
                companyVo.setAuditStatusStr("已审核");
            }
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("LOGO", companyVo.getLogoPic().getRequestPath());
            map.put("企业名称", companyVo.getCompanyName());
            map.put("品牌名称", companyVo.getBrandName());
            map.put("联系方式", companyVo.getCompanyPhone());
            map.put("商户类型", companyVo.getCompanyType());
            map.put("地址", companyVo.getAddress());
            map.put("审核状态", companyVo.getAuditStatusStr());
            map.put("创建时间", DateFormatUtils.format(companyVo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            list.add(map);
        }
        return excelUtil.downloadExcel(list, "company");

    }

    @Override
    public List<CompanyVo> companyAll(CompanySearchRq companySearchRq) {

        companySearchRq.setStartTimeStr(DateUtil.getFormatDate(companySearchRq.getStartTime(), "yyyy-MM-dd"));
        companySearchRq.setEndTimeStr(DateUtil.getFormatDate(companySearchRq.getEndTime(), "yyyy-MM-dd"));
        List<CompanyVo> companyList = this.baseMapper.companyPage(null, companySearchRq);
        companyList.forEach(company -> {
            // 获取图片信息
            LocalStorage logoPic = localStorageRepository.findById(company.getLogo()).orElseGet(LocalStorage::new);
            company.setLogoPic(logoPic);
            LocalStorage businessLicensePic = localStorageRepository.findById(company.getBusinessLicense()).orElseGet(LocalStorage::new);
            company.setBusinessLicensePic(businessLicensePic);
        });
        return companyList;
    }

    @Override
    public List<CompanyTokenSearchVo> companyTokenAll(CompanyTokenSearchRq companyTokenSearchRq) {
        return this.baseMapper.companyTokenPage(null, companyTokenSearchRq, null);
    }

    @Override
    public String downloadCompanyToken(List<CompanyTokenSearchVo> companyTokenAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CompanyTokenSearchVo tokenVo : companyTokenAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("商户名称", tokenVo.getCompanyName());
            map.put("钱包地址", tokenVo.getWalletAddress());
            map.put("酒证数量", tokenVo.getDepositCertificateNumber());
            list.add(map);
        }
        return excelUtil.downloadExcel(list, "token");
    }

    @Override
    public CompanyVo getCompanyByCreateBy(String createBy) {

        return this.baseMapper.getCompanyByCreateBy(createBy);
    }


}
