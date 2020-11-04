package me.zhengjie.utils.block;

import java.util.HashMap;
import java.util.Map;

/**
 * 密钥常量
 */
public class SecretKeyConstant {

    /**
     * 公钥/私钥
     */
    public static final Map<String, String> RSA = new HashMap<String, String>(4) {{
        put(RsaUtils.PUBLIC_KEY, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCHejluRseUziSoGQZInkMQCBZX6kajOozygA5ZNN/Ad9MVY12gdxfswfoxy/7QliLAbKoGK1918YMNiRGkCDx5RvixI9LEjgr8AN2G4FOgvnYwGQCOS2HCX/8Iwx0BHzCXYOFNjTz9UhXqy8OcRQaDd4Df4qS0zJ/6fT0gJ6/5TQIDAQAB");
        put(RsaUtils.PRIVATE_KEY, "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAId6OW5Gx5TOJKgZBkieQxAIFlfqRqM6jPKADlk038B30xVjXaB3F+zB+jHL/tCWIsBsqgYrX3Xxgw2JEaQIPHlG+LEj0sSOCvwA3YbgU6C+djAZAI5LYcJf/wjDHQEfMJdg4U2NPP1SFerLw5xFBoN3gN/ipLTMn/p9PSAnr/lNAgMBAAECgYBEmpUojbkVVopjgUCYV2Q4YxAFdwJMOCMVzdj2PXss+MCwbQAFeSlpYBU1UEr6D30LmjtDIoWVsTYfDZEcUntkZeXtCA01KMEGn3b7iF0yExRZdlCUcB60tSP9u4RnP1rbq08Y5y5QRqkaDFoAB5AN2muzzu9m7jZUBA1esO0VFQJBAL+0slr4p5mm1OPyPYYmCFcgrG+p7ldQsB4GQiO7Ue/vhH0zULs5aR2dHeBZ9SjUbfdlJO24pLTy5TKDlsaF9cMCQQC06evHHVKHGYqYSjqR39Id3SqHxmiIFj+nrHauDWOXMKKmv+e+cy/ytVFm1YsshHzsm3Rmm4AT3wVeF168KpOvAkB44cs5W+PrGmzRk5VB+sd1kS/KdZ/JxSJYvF+GQn8HScQEYpA8jpX+y2zkZDUX2uPAhTHFCJIrW6I0uQ8g/aWbAkB5RJvxOmyJdst6Tbd49MkJj6jknlCPeCpvIKHlBcCOJeHBWL3k4Z1wWa4Y+mdyDdUXXRTnerCdoU8dxRiRlRCtAkBq0sSxgA4lc32Fc2NgR21DGZEp2B6slqHc8U0FUImZNRKT2L0uOh+p1ysTXcq/58ETIifXR5chLw/T3EI4ee4H");
    }};
}
