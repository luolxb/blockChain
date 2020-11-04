package me.zhengjie.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.LocalStorage;
import me.zhengjie.enums.AttachmentTypeEnum;
import me.zhengjie.enums.RelaTypeEnum;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.dao.DepositCertificateTemplateMapper;
import me.zhengjie.modules.system.domain.AttachmentRela;
import me.zhengjie.modules.system.domain.DepositCertificateTemplate;
import me.zhengjie.modules.system.domain.vo.DepositCertificateTemplateRq;
import me.zhengjie.modules.system.domain.vo.DepositCertificateTemplateVo;
import me.zhengjie.modules.system.service.AttachmentRelaService;
import me.zhengjie.modules.system.service.DepositCertificateTemplateServie;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.repository.LocalStorageRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static me.zhengjie.enums.RelaTypeEnum.RELA_TYPE_TEMPLATE;


@Service
@Slf4j
@RequiredArgsConstructor
public class DepositCertificateTemplateServierImpl extends ServiceImpl<DepositCertificateTemplateMapper, DepositCertificateTemplate> implements DepositCertificateTemplateServie {

    @Autowired
    private AttachmentRelaService attachmentRelaService;

    private final LocalStorageRepository localStorageRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(DepositCertificateTemplateRq depositCertificateTemplateRq, UserDto userDto) {
        DepositCertificateTemplate depositCertificateTemplate = new DepositCertificateTemplate();
        Date date = new Date();
        BeanUtils.copyProperties(depositCertificateTemplateRq, depositCertificateTemplate);
        depositCertificateTemplate.setCreateBy(userDto.getUsername());
        depositCertificateTemplate.setCreateTime(date);
        boolean save = this.save(depositCertificateTemplate);
        if (!save) {
            throw new BadRequestException("新增存证模板失败");
        }
        addAttachmentRela(depositCertificateTemplateRq, userDto, depositCertificateTemplate, date);

    }

    /**
     * 保存附件关联
     *
     * @param depositCertificateTemplateRq
     * @param userDto
     * @param depositCertificateTemplate
     * @param date
     */
    private void addAttachmentRela(DepositCertificateTemplateRq depositCertificateTemplateRq, UserDto userDto, DepositCertificateTemplate depositCertificateTemplate, Date date) {
        // 保存产品图片
        List<AttachmentRela> attachmentRelaListProduct = new ArrayList<>();
        depositCertificateTemplateRq.getProductIds().forEach(productId -> {
            AttachmentRela attachmentRela = new AttachmentRela();
            attachmentRela.setAttachmentId(productId);
            attachmentRela.setRelaId(depositCertificateTemplate.getId());
            attachmentRela.setAttachmentType(Long.parseLong(AttachmentTypeEnum.ATTACHMENT_TYPE_PIC.getCode() + ""));
            attachmentRela.setRelaType(RELA_TYPE_TEMPLATE.getCode());
            attachmentRela.setCreateBy(userDto.getUsername());
            attachmentRela.setCreateTime(date);
            attachmentRelaListProduct.add(attachmentRela);

        });
        // 保存采购发票
        List<AttachmentRela> attachmentRelaListInvoice = new ArrayList<>();
        depositCertificateTemplateRq.getInvoiceIds().forEach(productId -> {
            AttachmentRela attachmentRela = new AttachmentRela();
            attachmentRela.setAttachmentId(productId);
            attachmentRela.setRelaId(depositCertificateTemplate.getId());
            attachmentRela.setAttachmentType(Long.parseLong(AttachmentTypeEnum.ATTACHMENT_TYPE_INVOICE.getCode() + ""));
            attachmentRela.setRelaType(RelaTypeEnum.RELA_TYPE_TEMPLATE.getCode());
            attachmentRela.setCreateBy(userDto.getUsername());
            attachmentRela.setCreateTime(date);
            attachmentRelaListInvoice.add(attachmentRela);

        });
        attachmentRelaService.saveBatch(attachmentRelaListProduct);
        attachmentRelaService.saveBatch(attachmentRelaListInvoice);
    }

    @Override
    public Page<DepositCertificateTemplateVo> templatePage(Pageable pageable) {
        Page<DepositCertificateTemplateVo> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        List<DepositCertificateTemplateVo> templatePage = this.baseMapper.templatePage(page);
        templatePage.forEach(template -> {
            // 附件
            List<LocalStorage> productList = attachmentRelaService.getLocalStorage(template.getId(), RelaTypeEnum.RELA_TYPE_TEMPLATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_PIC.getCode());
            List<LocalStorage> invoiceList = attachmentRelaService.getLocalStorage(template.getId(), RelaTypeEnum.RELA_TYPE_TEMPLATE.getCode(), AttachmentTypeEnum.ATTACHMENT_TYPE_INVOICE.getCode());
            template.setProductList(productList);
            template.setInvoiceList(invoiceList);

            LocalStorage certificateLogoPic = localStorageRepository.findById(template.getCertificateLogo()).orElseGet(LocalStorage::new);
            template.setCertificateLogoPic(certificateLogoPic);

        });
        page.setRecords(templatePage);
        return page;
    }

    /**
     * 修改存证模板
     *
     * @param depositCertificateTemplateRq
     * @param userDto
     */

    @Override
    public void edit(DepositCertificateTemplateRq depositCertificateTemplateRq, UserDto userDto) {
        DepositCertificateTemplate depositCertificateTemplate = new DepositCertificateTemplate();
        BeanUtils.copyProperties(depositCertificateTemplateRq, depositCertificateTemplate);
        Date date = new Date();
        depositCertificateTemplate.setUpdateBy(userDto.getUsername());
        depositCertificateTemplate.setUpdateTime(date);
        boolean b = this.updateById(depositCertificateTemplate);
        if (!b) {
            throw new BadRequestException("修改存证模板失败");
        }
        QueryWrapper<DepositCertificateTemplate> wrapper = new QueryWrapper<DepositCertificateTemplate>()
                .eq("rela_id", depositCertificateTemplateRq.getId())
                .eq("attachment_type", AttachmentTypeEnum.ATTACHMENT_TYPE_PIC.getCode())
                .eq("rela_type", RelaTypeEnum.RELA_TYPE_TEMPLATE.getCode());

        QueryWrapper<DepositCertificateTemplate> wrapper2 = new QueryWrapper<DepositCertificateTemplate>()
                .eq("rela_id", depositCertificateTemplateRq.getId())
                .eq("attachment_type", AttachmentTypeEnum.ATTACHMENT_TYPE_PIC.getCode())
                .eq("rela_type", RelaTypeEnum.RELA_TYPE_TEMPLATE.getCode());
        // 删除附件关联
        boolean remove = this.remove(wrapper);
        if (!remove) {
            throw new BadRequestException("修改存证模板失败");
        }
        boolean remove1 = this.remove(wrapper2);
        if (!remove1) {
            throw new BadRequestException("修改存证模板失败");
        }

        // 新增附件关联
        addAttachmentRela(depositCertificateTemplateRq, userDto, depositCertificateTemplate, date);
    }

    /**
     * 删除模板
     *
     * @param id
     * @param userDto
     */
    @Override
    public void del(Long id, UserDto userDto) {
        boolean b = this.removeById(id);
        if (!b) {
            throw new BadRequestException("删除模板失败");
        }

    }
}
