package me.zhengjie.modules.system.domain.vo;

import lombok.Data;
import me.zhengjie.modules.system.domain.DepositCertificate;

@Data
public class AuditOnChainVo extends DepositCertificate {

    private String walletAddress;
}
