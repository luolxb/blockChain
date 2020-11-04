package me.zhengjie.modules.system.eth.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.config.BlockConfig;
import me.zhengjie.domain.LocalStorage;
import me.zhengjie.enums.AttachmentTypeEnum;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.eth.dto.CompanyDepositCertificateDto;
import me.zhengjie.modules.system.eth.entity.EntityBlock;
import me.zhengjie.modules.system.eth.entity.EntityIssueToken;
import me.zhengjie.modules.system.service.AttachmentRelaService;
import me.zhengjie.modules.system.service.DepositCertificateService;
import me.zhengjie.repository.LocalStorageRepository;
import me.zhengjie.utils.BlockRestResponse;
import me.zhengjie.utils.block.RsaFullUtil;
import me.zhengjie.utils.block.Sha256HashUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static me.zhengjie.enums.RelaTypeEnum.RELA_TYPE_CERTIFICATE;

@Component
@Slf4j
@RequiredArgsConstructor
public class BlockServiceImpl {

    private final LocalStorageRepository localStorageRepository;

    @Value("${server.port}")
    private Integer serverPort;

    @Value("${block.callback.ip}")
    private String ip;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BlockConfig blockConfig;
    @Autowired
    private DepositCertificateService depositCertificateService;

    @Autowired
    private AttachmentRelaService attachmentRelaService;


    public EntityBlock getWalletAddress() throws Exception {
        String url = blockConfig.getUrl() + ":" + blockConfig.getPort() + "/eth/newAccount";
        String body = sendPost(null, url);
        if (StringUtils.isBlank(body)) {
            throw new BadRequestException("获取钱包地址失败");
        }
        return getEntityBlock(body);
    }

    private EntityBlock getEntityBlock(String body) throws Exception {
        // 解析body
        BlockRestResponse blockRestResponse = JSON.parseObject(body, BlockRestResponse.class);
        JSONObject data = (JSONObject) JSONObject.parse(String.valueOf(blockRestResponse.getData()));
        String value = (String) data.get("value");
        String sign = (String) data.get("sign");

        if (StringUtils.isBlank(sign)) {
            log.error("sign 签名为空");
            throw new BadRequestException("获取信息失败");
        }
        if (StringUtils.isBlank(value)) {
            log.error("value为空");
            throw new BadRequestException("获取信息失败");
        }

        // 验证签名
        boolean verify = RsaFullUtil.verify(value, sign);
        if (!verify) {
            log.error("签名错误");
            throw new BadRequestException("获取信息失败");
        }
        // 解析 返回 EntityBlock 对象
        EntityBlock entityBlock = RsaFullUtil.decryptObject(value, EntityBlock.class);
        if (null == entityBlock || StringUtils.isBlank(entityBlock.getPrivateKey())) {
            throw new BadRequestException("获取信息失败");
        }
        return entityBlock;
    }

    /**
     * 审核通过上链
     *
     * @param id
     * @return
     */
    public void auditChain(Long id) {
        // 回调地址
        String calBackUrl = "http://" + ip + ":" + serverPort + (org.apache.commons.lang3.StringUtils.isBlank(contextPath) ? "/" : contextPath) + "callBack/chain";
        // 根据存证的ID 获取 商户信息 +  存证信息 返回 CompanyDepositCertificateDto
        CompanyDepositCertificateDto companyDepositCertificateDto = depositCertificateService.GetDepositCertificateService(id);
        if (companyDepositCertificateDto == null) {
            throw new BadRequestException("没有存证信息");
        }
        // 存证附件
        LocalStorage productLogo = localStorageRepository.findById(companyDepositCertificateDto.getCertificateLogo()).orElseGet(LocalStorage::new);
        List<LocalStorage> productList = attachmentRelaService.getLocalStorage(companyDepositCertificateDto.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_PIC.getCode());
        List<LocalStorage> invoiceList = attachmentRelaService.getLocalStorage(companyDepositCertificateDto.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_INVOICE.getCode());
        List<LocalStorage> policyList = attachmentRelaService.getLocalStorage(companyDepositCertificateDto.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_POLICY.getCode());
        List<LocalStorage> confirmationList = attachmentRelaService.getLocalStorage(companyDepositCertificateDto.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_CONFIRMATION.getCode());
        List<LocalStorage> evaluationCertificateList = attachmentRelaService.getLocalStorage(companyDepositCertificateDto.getId(), RELA_TYPE_CERTIFICATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_EVALUATION_CERTIFICATE.getCode());
//        companyDepositCertificateDto.setProductList(productList);
//        companyDepositCertificateDto.setInvoiceList(invoiceList);
//        companyDepositCertificateDto.setPolicy(policyList);
//        companyDepositCertificateDto.setConfirmation(confirmationList);
//        companyDepositCertificateDto.setEvaluationCertificate(evaluationCertificateList);

        // 获取企业图片信息
//        LocalStorage logoPic = localStorageRepository.findById(companyDepositCertificateDto.getLogo()).orElseGet(LocalStorage::new);
//        companyDepositCertificateDto.setLogoPic(logoPic);
//        LocalStorage businessLicensePic = localStorageRepository.findById(companyDepositCertificateDto.getBusinessLicense()).orElseGet(LocalStorage::new);
//        companyDepositCertificateDto.setBusinessLicensePic(businessLicensePic);

        //   附件地址 (图片拼接;)
//        List<LocalStorage> product = companyDepositCertificateDto.getProductList();
//        List<LocalStorage> policy = companyDepositCertificateDto.getPolicy();
//        List<LocalStorage> confirmation = companyDepositCertificateDto.getConfirmation();
//        List<LocalStorage> evaluationCertificate = companyDepositCertificateDto.getEvaluationCertificate();
//        List<LocalStorage> invoice = companyDepositCertificateDto.getInvoiceList();

        EntityIssueToken entityIssueToken = new EntityIssueToken();
        entityIssueToken.setAddress(companyDepositCertificateDto.getWalletAddress());
        entityIssueToken.setCallUrl(calBackUrl);
        entityIssueToken.setAttorneyCertImages(confirmationList.get(0).getRequestPath());
        entityIssueToken.setEvaluationCertImages(evaluationCertificateList.get(0).getRequestPath());
        entityIssueToken.setInsurancePolicyImages(policyList.get(0).getRequestPath());
        entityIssueToken.setProductImages(productList.get(0).getRequestPath());
        entityIssueToken.setProductLogo(productLogo.getRequestPath());
        entityIssueToken.setProductLot(companyDepositCertificateDto.getBatchNumber());
        entityIssueToken.setProductName(companyDepositCertificateDto.getProductName());
        entityIssueToken.setProductSpec(companyDepositCertificateDto.getSpecification());
        entityIssueToken.setSourcingCompany(companyDepositCertificateDto.getPurchasingCompany());
        entityIssueToken.setPurchaseNoteImages(invoiceList.get(0).getRequestPath());
        entityIssueToken.setId(companyDepositCertificateDto.getId());
        entityIssueToken.setProductNumber(companyDepositCertificateDto.getCaseNumber());
        entityIssueToken.setProductCount(companyDepositCertificateDto.getAmount().intValue());

        Map<String, Object> map = new HashMap<>(2);
        try {
            // 加密对象
            String value = RsaFullUtil.encryptObject(entityIssueToken);
            // 签名
            String sign = RsaFullUtil.sign(value);
            map.put("value", value);
            map.put("sign", sign);

        } catch (Exception e) {
            log.error("e", e);
            throw new BadRequestException(e.getMessage());
        }

        log.info("EntityIssueToken ==>{}", entityIssueToken);
        String url = blockConfig.getUrl() + ":" + blockConfig.getPort() + "/erc721/issueTokenId";
        log.info("url ==>{}", url);
        String body = sendPost(map, url);
        if (body == null) {
            throw new BadRequestException("上链失败");
        }
        JSONObject jsonObject = JSONObject.parseObject(body);
        if ((Integer) jsonObject.get("code") != 200) {
            throw new BadRequestException(jsonObject.get("message").toString());
        }
    }

    private String sendPost(Map<String, Object> map, String url) {
        String body;
        try {
            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, map, String.class);
            log.info("发送到区块链 返回信息==>{}", stringResponseEntity);
            body = stringResponseEntity.getBody();
        } catch (Exception e) {
            log.error("e", e);
            throw new BadRequestException("发送请求异常");
        }
        return body;
    }

    /**
     * 如果不存在调用区块链服务生产
     * 根据钱包地址
     *
     * @param walletAddress
     * @return
     */
    public EntityBlock getPrivateKey(String walletAddress) throws Exception {
        Map<String, Object> map = new HashMap<>(2);
        try {
            // 加密对象
            String value = RsaFullUtil.encryptObject(walletAddress);
            // 签名
            String sign = RsaFullUtil.sign(value);
            map.put("value", value);
            map.put("sign", sign);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        String url = blockConfig.getUrl() + ":" + blockConfig.getPort() + "/eth/getPrivateKey";
        log.info("url ==>{}", url);
        String body = sendPost(map, url);
        if (body == null) {
            throw new BadRequestException("获取私钥失败");
        }
        JSONObject jsonObject = JSONObject.parseObject(body);
        if ((Integer) jsonObject.get("code") != 200) {
            throw new BadRequestException(jsonObject.get("message").toString());
        }
        return getEntityBlock(body);
    }
}
