package me.zhengjie.modules.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.config.FileProperties;
import me.zhengjie.domain.LocalStorage;
import me.zhengjie.enums.AttachmentTypeEnum;
import me.zhengjie.enums.AuditStatusEnum;
import me.zhengjie.enums.OnChainStatusEnum;
import me.zhengjie.enums.RelaTypeEnum;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.shop.domin.DepositCertificateShopRq;
import me.zhengjie.modules.system.dao.DepositCertificateMapper;
import me.zhengjie.modules.system.domain.AttachmentRela;
import me.zhengjie.modules.system.domain.Company;
import me.zhengjie.modules.system.domain.DepositCertificate;
import me.zhengjie.modules.system.domain.UserCompany;
import me.zhengjie.modules.system.domain.vo.*;
import me.zhengjie.modules.system.eth.dto.CompanyDepositCertificateDto;
import me.zhengjie.modules.system.eth.entity.EntityCallBack;
import me.zhengjie.modules.system.eth.service.BlockServiceImpl;
import me.zhengjie.modules.system.service.AttachmentRelaService;
import me.zhengjie.modules.system.service.CompanyService;
import me.zhengjie.modules.system.service.DepositCertificateService;
import me.zhengjie.modules.system.service.UserCompanyService;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.repository.LocalStorageRepository;
import me.zhengjie.utils.BlockRestResponse;
import me.zhengjie.utils.DateUtil;
import me.zhengjie.utils.ExcelUtil;
import me.zhengjie.utils.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

import static me.zhengjie.enums.RelaTypeEnum.RELA_TYPE_CERTIFICATE;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepositCertificateServiceImpl extends ServiceImpl<DepositCertificateMapper, DepositCertificate> implements DepositCertificateService {


    @Value("${shop.ip}")
    private String shopIp;

    @Value("${shop.port}")
    private String shopPort;

    @Value("${server.port}")
    private Integer serverPort;
    @Value("${file.ip}")
    private String ip;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExcelUtil excelUtil;


    private final LocalStorageRepository localStorageRepository;

    private final FileProperties properties;
    @Autowired
    private AttachmentRelaService attachmentRelaService;


    @Autowired
    private BlockServiceImpl blockService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserCompanyService userCompanyService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(DepositCertificateRq depositCertificateRq, UserDto userDto) {
        // 校验新增参数
        checkCreateParam(depositCertificateRq);

        // 根据ID 查询企业的状态
        UserCompany userCompany = userCompanyService.getOne(new QueryWrapper<UserCompany>().eq("user_id", userDto.getId()));
        if (null == userCompany) {
            throw new BadRequestException("用户企业不存在，请完善企业信息");
        }
        Company company = companyService.getById(userCompany.getCompanyId());
        if (company.getAuditStatus() != 2 || company.getStatus() != 1) {
            throw new BadRequestException("存证企业状态不正确");
        }

        Date date = new Date();
        DepositCertificate depositCertificate = new DepositCertificate();
        BeanUtils.copyProperties(depositCertificateRq, depositCertificate);
        depositCertificate.setUserId(userDto.getId());
        depositCertificate.setHasdValue("待上链");
        depositCertificate.setBlockHeight("待上链");
        depositCertificate.setCreateTime(new Date());
        depositCertificate.setCreateBy(userDto.getUsername());
        boolean save = this.save(depositCertificate);
        if (!save) {
            throw new BadRequestException("新增存证失败");
        }
        // 保存附件关联
        addAttachmentRela(depositCertificateRq, userDto, depositCertificate, date);
    }

    /**
     * 存证分页
     *
     * @param pageable
     * @param userDto
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DepositCertificateVo> certificatePage(Pageable pageable, UserDto userDto, DepositCertificateSearchRq depositCertificateSearchRq) {
        Integer size = depositCertificateSearchRq.getSize();
        Integer pageNumber = depositCertificateSearchRq.getPage();
        Page<DepositCertificateVo> page = new Page<>(pageNumber, size);
        if (userDto.getIsAdmin()) {
            userDto.setId(null);
        }
        if (StringUtils.isNotBlank(depositCertificateSearchRq.getOnChainStatus())) {
            String[] split = depositCertificateSearchRq.getOnChainStatus().split(",");
            depositCertificateSearchRq.setOnChainStatus3(Integer.parseInt(split[0]));
            if (split.length > 1) {
                depositCertificateSearchRq.setOnChainStatus4(Integer.parseInt(split[1]));
            }
        }
        depositCertificateSearchRq.setStartTimeStr(DateUtil.getFormatDate(depositCertificateSearchRq.getStartTime(), "yyyy-MM-dd"));
        depositCertificateSearchRq.setEndTimeStr(DateUtil.getFormatDate(depositCertificateSearchRq.getEndTime(), "yyyy-MM-dd"));
        List<DepositCertificateVo> depositCertificateVoList = this.baseMapper.certificatePage(page, userDto.getId(), depositCertificateSearchRq);
        depositCertificateVoList.forEach(depositCertificateVo -> {
            // 存证状态为已上链 显示保险单，确认书，评估书
            boolean flag = false;
            if (!userDto.getIsAdmin() &&
                    (depositCertificateVo.getOnChainStatus().equals(OnChainStatusEnum.ON_CHAIN_STATUS_YES.getCode()) ||
                            depositCertificateVo.getOnChainStatus().equals(OnChainStatusEnum.ON_CHAIN_STATUS_ING.getCode()) ||
                            depositCertificateVo.getOnChainStatus().equals(OnChainStatusEnum.ON_CHAIN_STATUS_FAIL.getCode()))) {
                flag = true;
            } else if (userDto.getIsAdmin()) {
                flag = true;
            }
            if (flag) {
                List<LocalStorage> policyList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_POLICY.getCode());
                List<LocalStorage> confirmationList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_CONFIRMATION.getCode());
                List<LocalStorage> evaluationCertificateList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_EVALUATION_CERTIFICATE.getCode());

                depositCertificateVo.setPolicy(policyList);
                depositCertificateVo.setConfirmation(confirmationList);
                depositCertificateVo.setEvaluationCertificate(evaluationCertificateList);
            }

            // 附件
            List<LocalStorage> productList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_PIC.getCode());
            List<LocalStorage> invoiceList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_INVOICE.getCode());
            depositCertificateVo.setProductList(productList);
            depositCertificateVo.setInvoiceList(invoiceList);

            LocalStorage logoPic = localStorageRepository.findById(depositCertificateVo.getCertificateLogo()).orElseGet(LocalStorage::new);
            depositCertificateVo.setCertificateLogoPic(Arrays.asList(logoPic));

            LocalStorage comlogoPic = localStorageRepository.findById(depositCertificateVo.getCompanyLogo()).orElseGet(LocalStorage::new);
            depositCertificateVo.setCompanyLogoPic(Arrays.asList(comlogoPic));

        });
        page.setRecords(depositCertificateVoList);
        return page;
    }

    /**
     * 修改存证
     *
     * @param depositCertificateRq
     * @param userDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(DepositCertificateRq depositCertificateRq, UserDto userDto) {
        // 校验修改参数
        checkEditParam(depositCertificateRq);

        DepositCertificate depositCertificate = new DepositCertificate();
        BeanUtils.copyProperties(depositCertificateRq, depositCertificate);
        Date date = new Date();
        depositCertificate.setUpdateBy(userDto.getUsername());
        depositCertificate.setUpdateTime(date);
        boolean b = this.updateById(depositCertificate);
        if (!b) {
            throw new BadRequestException("修改存证失败");
        }
        // 修改附件(删除之前的关联，新增新的关联)
        //  删除之前的关联
        delAttachmentRela(depositCertificateRq.getId(), RELA_TYPE_CERTIFICATE.getCode(), Long.parseLong(AttachmentTypeEnum.ATTACHMENT_TYPE_PIC.getCode() + ""));
        delAttachmentRela(depositCertificateRq.getId(), RELA_TYPE_CERTIFICATE.getCode(), Long.parseLong(AttachmentTypeEnum.ATTACHMENT_TYPE_INVOICE.getCode() + ""));
        // 新增新的关联
        addAttachmentRela(depositCertificateRq, userDto, depositCertificate, date);

    }


    /**
     * 服务端修改
     *
     * @param depositCertificateServerRq
     * @param userDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editS(DepositCertificateServerRq depositCertificateServerRq, UserDto userDto) {

        delAttachmentRela(depositCertificateServerRq.getId(), RELA_TYPE_CERTIFICATE.getCode(), Long.parseLong(AttachmentTypeEnum.ATTACHMENT_TYPE_POLICY.getCode() + ""));
        delAttachmentRela(depositCertificateServerRq.getId(), RELA_TYPE_CERTIFICATE.getCode(), Long.parseLong(AttachmentTypeEnum.ATTACHMENT_TYPE_CONFIRMATION.getCode() + ""));
        delAttachmentRela(depositCertificateServerRq.getId(), RELA_TYPE_CERTIFICATE.getCode(), Long.parseLong(AttachmentTypeEnum.ATTACHMENT_TYPE_EVALUATION_CERTIFICATE.getCode() + ""));
        // 根据ID 查询企业的状态
        UserCompany userCompany = userCompanyService.getOne(new QueryWrapper<UserCompany>().eq("user_id", depositCertificateServerRq.getUserId()));
        Company company = companyService.getById(userCompany.getCompanyId());
        if (company.getStatus() != 1) {
            throw new BadRequestException("存证企业状态不正确");
        }

        // 修改存证 保险单 确认书  评估证书
        Date date = new Date();
        AttachmentRela attachmentRela = new AttachmentRela();
        attachmentRela.setCreateBy(userDto.getUsername());
        attachmentRela.setCreateTime(date);
        attachmentRela.setAttachmentId(depositCertificateServerRq.getPolicy());
        attachmentRela.setRelaId(depositCertificateServerRq.getId());
        attachmentRela.setRelaType(RELA_TYPE_CERTIFICATE.getCode());
        attachmentRela.setAttachmentType(Long.parseLong(AttachmentTypeEnum.ATTACHMENT_TYPE_POLICY.getCode() + ""));
        boolean save = attachmentRelaService.save(attachmentRela);
        if (!save) {
            throw new BadRequestException("保存附件失败");
        }

        AttachmentRela attachmentRela1 = new AttachmentRela();
        attachmentRela1.setCreateBy(userDto.getUsername());
        attachmentRela1.setCreateTime(date);
        attachmentRela1.setAttachmentId(depositCertificateServerRq.getConfirmation());
        attachmentRela1.setRelaId(depositCertificateServerRq.getId());
        attachmentRela1.setRelaType(RELA_TYPE_CERTIFICATE.getCode());
        attachmentRela1.setAttachmentType(Long.parseLong(AttachmentTypeEnum.ATTACHMENT_TYPE_CONFIRMATION.getCode() + ""));
        boolean save1 = attachmentRelaService.save(attachmentRela1);
        if (!save1) {
            throw new BadRequestException("保存附件失败");
        }

        AttachmentRela attachmentRela2 = new AttachmentRela();
        attachmentRela2.setCreateBy(userDto.getUsername());
        attachmentRela2.setCreateTime(date);
        attachmentRela2.setAttachmentId(depositCertificateServerRq.getEvaluationCertificate());
        attachmentRela2.setRelaId(depositCertificateServerRq.getId());
        attachmentRela2.setRelaType(RELA_TYPE_CERTIFICATE.getCode());
        attachmentRela2.setAttachmentType(Long.parseLong(AttachmentTypeEnum.ATTACHMENT_TYPE_EVALUATION_CERTIFICATE.getCode() + ""));
        boolean save2 = attachmentRelaService.save(attachmentRela2);
        if (!save2) {
            throw new BadRequestException("保存附件失败");
        }
    }

    /**
     * 申请上链
     *
     * @param depositCertificateRq
     * @param userDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void appliChain(DepositCertificateRq depositCertificateRq, UserDto userDto) {
        Date date = new Date();
        DepositCertificate depositCertificate = new DepositCertificate();
        depositCertificate.setId(depositCertificateRq.getId());
        depositCertificate.setUpdateTime(date);
        depositCertificate.setUpdateBy(userDto.getUsername());
        depositCertificate.setAppliTime(date);
        depositCertificate.setOnChainStatus(OnChainStatusEnum.ON_CHAIN_STATUS_APPLI.getCode());
        boolean b = this.updateById(depositCertificate);
        if (!b) {
            throw new BadRequestException("申请上链失败");
        }
    }

    /**
     * 审核
     *
     * @param depositCertificateAuditRq
     * @param userDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(DepositCertificateAuditRq depositCertificateAuditRq, UserDto userDto) {
        Date date = new Date();
        depositCertificateAuditRq.getId().forEach(id -> {
            // 根据存证的ID 获取存证的状态是否是申请上链
            DepositCertificate certificate = this.getById(id);
            if (certificate == null) {
                throw new BadRequestException("存证信息不存在");
            }
            if (!OnChainStatusEnum.ON_CHAIN_STATUS_APPLI.getCode().equals(certificate.getOnChainStatus())) {
                throw new BadRequestException("存证状态错误");
            }

            // 判断存证资料是否完全
            checkAudit(id);
            DepositCertificate depositCertificate = new DepositCertificate();
            depositCertificate.setUpdateBy(userDto.getUsername());
            depositCertificate.setUpdateTime(date);
            depositCertificate.setAuditTime(date);
            depositCertificate.setAuditStatus(depositCertificateAuditRq.getAuditStatus());
            depositCertificate.setAuditRemark(depositCertificateAuditRq.getAuditRemark());
            depositCertificate.setAuditBy(userDto.getUsername());
            depositCertificate.setId(id);

            // 如果通过审核 调用上链API
            if (AuditStatusEnum.AUDIT_STATUS_AGREE.getCode().equals(depositCertificateAuditRq.getAuditStatus())) {
                depositCertificate.setOnChainStatus(OnChainStatusEnum.ON_CHAIN_STATUS_ING.getCode());
            } else if (AuditStatusEnum.AUDIT_STATUS_REFUSE.getCode().equals(depositCertificateAuditRq.getAuditStatus())) {
                //  TODO 1.0 版本不考虑上链拒绝
                throw new BadRequestException("上链审核状态错误");
            }
            boolean b = this.updateById(depositCertificate);
            if (!b) {
                throw new BadRequestException("审核失败");
            }
            // 如果通过审核 调用上链API
            if (AuditStatusEnum.AUDIT_STATUS_AGREE.getCode().equals(depositCertificateAuditRq.getAuditStatus())) {
                blockService.auditChain(depositCertificate.getId());
            }
        });
    }

    /**
     * 判断存证资料是否完全
     *
     * @param id
     */
    private void checkAudit(Long id) {
        List<LocalStorage> productList = attachmentRelaService.getLocalStorage(id, RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_PIC.getCode());
        List<LocalStorage> invoiceList = attachmentRelaService.getLocalStorage(id, RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_INVOICE.getCode());
        List<LocalStorage> policyList = attachmentRelaService.getLocalStorage(id, RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_POLICY.getCode());
        List<LocalStorage> confirmationList = attachmentRelaService.getLocalStorage(id, RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_CONFIRMATION.getCode());
        List<LocalStorage> evaluationCertificateList = attachmentRelaService.getLocalStorage(id, RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_EVALUATION_CERTIFICATE.getCode());
        if (CollectionUtils.isEmpty(productList) ||
                CollectionUtils.isEmpty(invoiceList) ||
                CollectionUtils.isEmpty(policyList) ||
                CollectionUtils.isEmpty(confirmationList) ||
                CollectionUtils.isEmpty(evaluationCertificateList)) {
            throw new BadRequestException("存证附件不完整");
        }
    }


    /**
     * 上链 结果回调方法，用于修改上链状态
     *
     * @param blockRestResponse
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onChainCallBack(BlockRestResponse blockRestResponse) {
        // 成功返回码 0
        if (blockRestResponse.getCode() != 0) {
            return;
        }
        // 构建存证实体类 根据ID修改
        EntityCallBack data = JSON.parseObject(String.valueOf(blockRestResponse.getData()), EntityCallBack.class);
        DepositCertificate one = this.getById(Long.parseLong(data.getId() + ""));
        if (one == null) {
            return;
        }
        // 如果上链状态是已上链 就不进行后面操作了
        if (one.getOnChainStatus().equals(OnChainStatusEnum.ON_CHAIN_STATUS_YES.getCode())) {
            return;
        }
        DepositCertificate depositCertificate = new DepositCertificate();
        depositCertificate.setId(Long.parseLong(data.getId() + ""));
        depositCertificate.setHasdValue(data.getHash());
        depositCertificate.setBlockHeight(String.valueOf(data.getBlockNumber()));
        depositCertificate.setOnChainStatus(StringUtils.equals(data.getIsValid(), "2") ? OnChainStatusEnum.ON_CHAIN_STATUS_YES.getCode() : OnChainStatusEnum.ON_CHAIN_STATUS_FAIL.getCode());
        depositCertificate.setOnChainTime(new Date());

        one.setHasdValue(data.getHash());
        one.setOnChainTime(depositCertificate.getOnChainTime());
        depositCertificate.setCertificate(addCertificate(one));
        this.updateById(depositCertificate);
    }


    /**
     * 新增证书
     *
     * @param one
     * @return
     */
    private String addCertificate(DepositCertificate one) {
        String outPutPath = properties.getPath().getPath() + "certificate" + File.separator;

        List<String> stringList = new ArrayList<>();
        stringList.add("创建者：" + companyService.getCompanyByCreateBy(one.getCreateBy()).getCompanyName());
        stringList.add("上链时间：" + DateFormatUtils.format(one.getOnChainTime(), "yyyy-MM-dd HH:mm:ss"));
        stringList.add("创建时间：" + DateFormatUtils.format(one.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        stringList.add("区块链ID：" + one.getHasdValue());
        stringList.add("商品名称：" + one.getProductName());
        stringList.add("香型：" + (StringUtils.isBlank(one.getFragrance()) ? "" : one.getFragrance()));
        stringList.add("度数：" + (StringUtils.isBlank(one.getDegree()) ? "" : one.getDegree()));
        stringList.add("规格：" + one.getSpecification());
        stringList.add("数量：" + (one.getAmount() == null ? "" : one.getAmount()));

        List<LocalStorage> productList = attachmentRelaService.getLocalStorage(one.getId(), RelaTypeEnum.RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_PIC.getCode());

        if (CollectionUtils.isEmpty(productList)) {
            return null;
        }
        return overlapImage(productList.get(0).getPath(), stringList, outPutPath);
    }

    /**
     * 构建证书图片
     *
     * @param qrCodePath
     * @param stringList
     * @param outPutPath
     * @return
     */
    private String overlapImage(String qrCodePath, List<String> stringList, String outPutPath) {
//        ClassPathResource classPathResource = new ClassPathResource("template/image/template.png");
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/image/template.png");
        File file = null;
        try {
            file = FileUtil.inputStreamToFile(inputStream, "template.png");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String path1 = file.getPath();
        log.info("证书模板path1 ==>{}", path1);

// 获得File对象，当然也可以获取输入流对象
//        File file =;
//        try {
//            file = classPathResource.getFile();
//        } catch (IOException e) {
//            log.error("e", e);
//            log.info("证书模板path ==>{}", resource);
//            return null;
//
//        }
        if (org.apache.commons.lang.StringUtils.isBlank(qrCodePath)) {
            log.error("产品图片为空{}", qrCodePath);
            return null;
        }

        try {
            //设置图片大小
            BufferedImage background = resizeImage(600, 843, ImageIO.read(file));
            BufferedImage qrCode = resizeImage(110, 120, ImageIO.read(new File(qrCodePath)));
            Graphics2D g = background.createGraphics();
            g.setColor(Color.black);
            g.setFont(new Font("Light", Font.PLAIN, 14));
            int space = 0;
            int space1 = 0;
            // 文字排序
            for (int i = 0; i < stringList.size(); i++) {
                if (i == 3) {
                    String substring = stringList.get(i).substring(0, 50);
                    g.drawString(substring, 100, 365 - space);
                    g.drawString(stringList.get(i).substring(50), 169, 365 - space + 22);
                    continue;
                }

                if (i == 4) {
                    space -= 60;
                }
                if (i == 6 || i == 8) {
                    g.drawString(stringList.get(i), 300, space1);
                } else {
                    g.drawString(stringList.get(i), 100, 365 - space);
                    space1 = 365 - space;
                    space -= 40;
                }
            }
            //在背景图片上添加二维码图片
            g.drawImage(qrCode, 240, 200, qrCode.getWidth(), qrCode.getHeight(), null);
            g.dispose();
            //保存到对应文件夹下对应当天日期文件夹
//            String year = Calendar.getInstance().get(Calendar.YEAR) + "";
//            String monthWithDay = Calendar.getInstance().get(Calendar.MONTH) + 1 + "-" + Calendar.getInstance().get(Calendar.DATE);
            String name = new Random().nextInt(10000) + System.currentTimeMillis() + ".png";

//            String name =  "/" + year + "/" + monthWithDay + "/" + randomFileName;
            outPutPath = outPutPath + "/" + name;

            Path path = Paths.get(outPutPath);
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            log.info("证书保存地址：==>{}", outPutPath);
            ImageIO.write(background, "png", new File(outPutPath));

            String cIp = getIp();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(cIp)) {
                ip = cIp;
            }
            // 保存到数据库
            LocalStorage localStorage = new LocalStorage(
                    name,
                    name,
                    "png",
                    outPutPath,
                    "certificate",
                    "177KB",
                    getRequestPath(ip, properties.getPath().getRequestpath(), "certificate", name)
            );

            LocalStorage localStorage1 = localStorageRepository.save(localStorage);
            return localStorage1.getRequestPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getRequestPath(String ip, String path, String type, String name) {
        return "http://" + ip + ":" + serverPort + (org.apache.commons.lang3.StringUtils.isBlank(contextPath) ? "/" : contextPath) + path + type + "/" + name;
    }

    private static BufferedImage resizeImage(int x, int y, BufferedImage bfi) {
        BufferedImage bufferedImage = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(
                bfi.getScaledInstance(x, y, Image.SCALE_SMOOTH), 0, 0, null);
        return bufferedImage;
    }


    @Override
    @Transactional(readOnly = true)
    public AuditOnChainVo auditChain(Long id) {
        return this.baseMapper.auditChain(id);
    }

    /**
     * 根据存证ID 获取上链信息
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public CompanyDepositCertificateDto GetDepositCertificateService(Long id) {
        return this.baseMapper.GetDepositCertificateService(id);
    }

    /**
     * 定时查询上链中/上链失败的存证，重新发生上链请求
     */
    @Override
    public void pushChainTask() {
        // 查询数据库上链状态为上链中的状态
        QueryWrapper<DepositCertificate> wrapper = new QueryWrapper<>();
        wrapper.eq("on_chain_status", OnChainStatusEnum.ON_CHAIN_STATUS_ING.getCode());
        wrapper.or().eq("on_chain_status", OnChainStatusEnum.ON_CHAIN_STATUS_FAIL.getCode());
        List<DepositCertificate> depositCertificateList = this.list(wrapper);
        depositCertificateList.forEach(depositCertificate -> {
            try {
                blockService.auditChain(depositCertificate.getId());
            } catch (Exception e) {
                log.error("定时查询上链中的存证，重新发生上链请求异常", e);
            }
        });
    }

    /**
     * 查看详情
     *
     * @param id
     * @return
     */
    @Override
    public DepositCertificateVo detail(Long id, UserDto userDto) {
        DepositCertificateSearchRq depositCertificateSearchRq = new DepositCertificateSearchRq();
        depositCertificateSearchRq.setId(id);
        List<DepositCertificateVo> depositCertificateVoList = this.baseMapper.certificatePage(null, null, depositCertificateSearchRq);
        if (CollectionUtils.isEmpty(depositCertificateVoList)) {
            throw new BadRequestException("没有查询到存证");
        }

        depositCertificateVoList.forEach(depositCertificateVo -> {
            // 存证状态为已上链 显示保险单，确认书，评估书
            boolean flag = false;
            if (!userDto.getIsAdmin() &&
                    (depositCertificateVo.getOnChainStatus().equals(OnChainStatusEnum.ON_CHAIN_STATUS_YES.getCode()) ||
                            depositCertificateVo.getOnChainStatus().equals(OnChainStatusEnum.ON_CHAIN_STATUS_ING.getCode()) ||
                            depositCertificateVo.getOnChainStatus().equals(OnChainStatusEnum.ON_CHAIN_STATUS_FAIL.getCode()))) {
                flag = true;
            } else if (userDto.getIsAdmin()) {
                flag = true;
            }
            if (flag) {
                List<LocalStorage> policyList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_POLICY.getCode());
                List<LocalStorage> confirmationList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_CONFIRMATION.getCode());
                List<LocalStorage> evaluationCertificateList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_EVALUATION_CERTIFICATE.getCode());

                depositCertificateVo.setPolicy(policyList);
                depositCertificateVo.setConfirmation(confirmationList);
                depositCertificateVo.setEvaluationCertificate(evaluationCertificateList);
            }
            // 附件
            List<LocalStorage> productList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_PIC.getCode());
            List<LocalStorage> invoiceList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_INVOICE.getCode());

            depositCertificateVo.setProductList(productList);
            depositCertificateVo.setInvoiceList(invoiceList);

            LocalStorage logoPic = localStorageRepository.findById(depositCertificateVo.getCertificateLogo()).orElseGet(LocalStorage::new);
            depositCertificateVo.setCertificateLogoPic(Arrays.asList(logoPic));
            LocalStorage compLogoPic = localStorageRepository.findById(depositCertificateVo.getCompanyLogo()).orElseGet(LocalStorage::new);
            depositCertificateVo.setCertificateLogoPic(Arrays.asList(compLogoPic));
        });

        return depositCertificateVoList.get(0);
    }

    @Override
    public List<DepositCertificateVo> depositCertificateAll(DepositCertificateSearchRq depositCertificateSearchRq) {
        if (StringUtils.isNotBlank(depositCertificateSearchRq.getOnChainStatus())) {
            String[] split = depositCertificateSearchRq.getOnChainStatus().split(",");
            depositCertificateSearchRq.setOnChainStatus3(Integer.parseInt(split[0]));
            if (split.length > 1) {
                depositCertificateSearchRq.setOnChainStatus4(Integer.parseInt(split[1]));
            }
        }
        depositCertificateSearchRq.setStartTimeStr(DateUtil.getFormatDate(depositCertificateSearchRq.getStartTime(), "yyyy-MM-dd"));
        depositCertificateSearchRq.setEndTimeStr(DateUtil.getFormatDate(depositCertificateSearchRq.getEndTime(), "yyyy-MM-dd"));
        List<DepositCertificateVo> depositCertificateVoList = this.baseMapper.certificatePage(null, null, depositCertificateSearchRq);
        depositCertificateVoList.forEach(depositCertificateVo -> {
            // 附件
            List<LocalStorage> productList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_PIC.getCode());
            List<LocalStorage> invoiceList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_INVOICE.getCode());
            List<LocalStorage> policyList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_POLICY.getCode());
            List<LocalStorage> confirmationList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_CONFIRMATION.getCode());
            List<LocalStorage> evaluationCertificateList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_EVALUATION_CERTIFICATE.getCode());

            depositCertificateVo.setProductList(productList);
            depositCertificateVo.setInvoiceList(invoiceList);
            depositCertificateVo.setPolicy(policyList);
            depositCertificateVo.setConfirmation(confirmationList);
            depositCertificateVo.setEvaluationCertificate(evaluationCertificateList);

            LocalStorage logoPic = localStorageRepository.findById(depositCertificateVo.getCertificateLogo()).orElseGet(LocalStorage::new);
            depositCertificateVo.setCertificateLogoPic(Arrays.asList(logoPic));

        });
        return depositCertificateVoList;
    }

    @Override
    public String download(List<DepositCertificateVo> depositCertificateAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DepositCertificateVo depositCertificateVo : depositCertificateAll) {
            //  上链转态1:未上链  2：已上链 3：上链中 4：上链失败 5:申请上链
            if (depositCertificateVo.getOnChainStatus().equals(OnChainStatusEnum.ON_CHAIN_STATUS_No.getCode())) {
                depositCertificateVo.setOnChainStatusStr(OnChainStatusEnum.ON_CHAIN_STATUS_No.getDescription());
            } else if (depositCertificateVo.getOnChainStatus().equals(OnChainStatusEnum.ON_CHAIN_STATUS_YES.getCode())) {
                depositCertificateVo.setOnChainStatusStr(OnChainStatusEnum.ON_CHAIN_STATUS_YES.getDescription());
            } else if (depositCertificateVo.getOnChainStatus().equals(OnChainStatusEnum.ON_CHAIN_STATUS_ING.getCode())) {
                depositCertificateVo.setOnChainStatusStr(OnChainStatusEnum.ON_CHAIN_STATUS_ING.getDescription());
            } else if (depositCertificateVo.getOnChainStatus().equals(OnChainStatusEnum.ON_CHAIN_STATUS_FAIL.getCode())) {
                depositCertificateVo.setOnChainStatusStr(OnChainStatusEnum.ON_CHAIN_STATUS_FAIL.getDescription());
            } else if (depositCertificateVo.getOnChainStatus().equals(OnChainStatusEnum.ON_CHAIN_STATUS_APPLI.getCode())) {
                depositCertificateVo.setOnChainStatusStr(OnChainStatusEnum.ON_CHAIN_STATUS_APPLI.getDescription());
            }
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("logo缩略图", depositCertificateVo.getCertificateLogoPic().get(0).getRequestPath());
            map.put("存证名称", depositCertificateVo.getCertificateName());
            map.put("所属商户", depositCertificateVo.getCompanyName());
            map.put("申请时间", depositCertificateVo.getAppliTime() == null ? "" : DateFormatUtils.format(depositCertificateVo.getAppliTime(), "yyyy-MM-dd HH:mm:ss"));
            map.put("上链状态", depositCertificateVo.getOnChainStatusStr());
            map.put("审核时间", depositCertificateVo.getAuditTime() == null ? "" : DateFormatUtils.format(depositCertificateVo.getAuditTime(), "yyyy-MM-dd HH:mm:ss"));
            map.put("备注", depositCertificateVo.getAuditRemark());
            list.add(map);
        }
        return excelUtil.downloadExcel(list, "deposit_certificate");

    }

    /**
     * 商城获取存证 分页
     *
     * @param depositCertificateShopRq
     * @return
     */
    @Override
    public Page<DepositCertificateVo> certificateShopPage(DepositCertificateShopRq depositCertificateShopRq) {
        Integer size = depositCertificateShopRq.getSize();
        Integer pageNumber = depositCertificateShopRq.getPage();
        Page<DepositCertificateVo> page = new Page<>(pageNumber, size);

        List<DepositCertificateVo> depositCertificateVoList = this.baseMapper.certificateShopPage(page, depositCertificateShopRq);
        getDepositCertificateVoList(depositCertificateVoList);
        page.setRecords(depositCertificateVoList);
        return page;
    }

    private void getDepositCertificateVoList(List<DepositCertificateVo> depositCertificateVoList) {
        depositCertificateVoList.forEach(depositCertificateVo -> {
            List<LocalStorage> policyList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_POLICY.getCode());
            List<LocalStorage> confirmationList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_CONFIRMATION.getCode());
            List<LocalStorage> evaluationCertificateList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_EVALUATION_CERTIFICATE.getCode());

            depositCertificateVo.setPolicy(policyList);
            depositCertificateVo.setConfirmation(confirmationList);
            depositCertificateVo.setEvaluationCertificate(evaluationCertificateList);

            // 附件
            List<LocalStorage> productList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_PIC.getCode());
            List<LocalStorage> invoiceList = attachmentRelaService.getLocalStorage(depositCertificateVo.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_INVOICE.getCode());
            depositCertificateVo.setProductList(productList);
            depositCertificateVo.setInvoiceList(invoiceList);

            LocalStorage logoPic = localStorageRepository.findById(depositCertificateVo.getCertificateLogo()).orElseGet(LocalStorage::new);
            depositCertificateVo.setCertificateLogoPic(Arrays.asList(logoPic));

            LocalStorage comlogoPic = localStorageRepository.findById(depositCertificateVo.getCompanyLogo()).orElseGet(LocalStorage::new);
            depositCertificateVo.setCompanyLogoPic(Arrays.asList(comlogoPic));

        });
    }

    /**
     * 商城查看详情
     *
     * @param id
     * @return
     */
    @Override
    public DepositCertificateVo certificateShopDetail(Long id) {
        DepositCertificateShopRq depositCertificateShopRq = new DepositCertificateShopRq();
        depositCertificateShopRq.setId(id);
        List<DepositCertificateVo> depositCertificateVoList = this.baseMapper.certificateShopPage(null, depositCertificateShopRq);
        if (CollectionUtils.isEmpty(depositCertificateVoList)) {
            throw new BadRequestException("没有查询到存证");
        }

        getDepositCertificateVoList(depositCertificateVoList);

        return depositCertificateVoList.get(0);

    }


    /**
     * 删除附件
     *
     * @param relaId
     * @param relaType
     * @param attachmentType
     */
    private void delAttachmentRela(Long relaId, String relaType, Long attachmentType) {
        QueryWrapper<AttachmentRela> wrapper = new QueryWrapper<>();
        wrapper.eq("rela_id", relaId);
        wrapper.eq("rela_type", relaType);
        wrapper.eq("attachment_type", attachmentType);

        boolean remove = attachmentRelaService.remove(wrapper);
        if (!remove) {
            throw new BadRequestException("修改存证失败");
        }
    }


    /**
     * 校验修改参数
     * TODO
     *
     * @param depositCertificateRq
     */
    private void checkEditParam(DepositCertificateRq depositCertificateRq) {
        if (depositCertificateRq.getId() == null) {
            throw new BadRequestException("存证ID不能为空");
        }
    }

    /**
     * 校验新增参数
     * TODO
     *
     * @param depositCertificateRq
     */
    private void checkCreateParam(DepositCertificateRq depositCertificateRq) {

    }

    /**
     * 保存附件关联
     *
     * @param depositCertificateRq
     * @param userDto
     * @param depositCertificate
     * @param date
     */
    private void addAttachmentRela(DepositCertificateRq depositCertificateRq, UserDto userDto, DepositCertificate depositCertificate, Date date) {
        // 保存产品图片
        List<AttachmentRela> attachmentRelaListProduct = new ArrayList<>();
        depositCertificateRq.getProductIds().forEach(productId -> {
            AttachmentRela attachmentRela = new AttachmentRela();
            attachmentRela.setAttachmentId(productId);
            attachmentRela.setRelaId(depositCertificate.getId());
            attachmentRela.setAttachmentType(Long.parseLong(AttachmentTypeEnum.ATTACHMENT_TYPE_PIC.getCode() + ""));
            attachmentRela.setRelaType(RELA_TYPE_CERTIFICATE.getCode());
            attachmentRela.setCreateBy(userDto.getUsername());
            attachmentRela.setCreateTime(date);
            attachmentRelaListProduct.add(attachmentRela);

        });
        // 保存采购发票
        List<AttachmentRela> attachmentRelaListInvoice = new ArrayList<>();
        depositCertificateRq.getInvoiceIds().forEach(invoiceId -> {
            AttachmentRela attachmentRela = new AttachmentRela();
            attachmentRela.setAttachmentId(invoiceId);
            attachmentRela.setRelaId(depositCertificate.getId());
            attachmentRela.setAttachmentType(Long.parseLong(AttachmentTypeEnum.ATTACHMENT_TYPE_INVOICE.getCode() + ""));
            attachmentRela.setRelaType(RELA_TYPE_CERTIFICATE.getCode());
            attachmentRela.setCreateBy(userDto.getUsername());
            attachmentRela.setCreateTime(date);
            attachmentRelaListInvoice.add(attachmentRela);

        });
        boolean b = attachmentRelaService.saveBatch(attachmentRelaListProduct);
        if (!b) {
            throw new BadRequestException("保存附件失败");
        }
        boolean b1 = attachmentRelaService.saveBatch(attachmentRelaListInvoice);
        if (!b1) {
            throw new BadRequestException("保存附件失败");
        }
    }

    public String getIp() {
        try {
            JSONObject forObject = restTemplate.getForObject("https://httpbin.org/ip", JSONObject.class);
            log.info("获取外网ip==>{}", forObject);
            return (String) forObject.get("origin");
        } catch (Exception e) {
            log.error("获取外网地址失败：", e);
            return null;
        }
    }
}
