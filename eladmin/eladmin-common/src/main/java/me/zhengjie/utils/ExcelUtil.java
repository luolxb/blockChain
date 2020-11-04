package me.zhengjie.utils;


import cn.hutool.poi.excel.BigExcelWriter;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.config.FileProperties;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExcelUtil {

    @Value("${server.port}")
    private Integer serverPort;
    @Value("${file.ip}")
    private String ip;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final FileProperties properties;

    @Autowired
    private RestTemplate restTemplate;


    public String getIp() {
        try {
            JSONObject forObject = restTemplate.getForObject("https://httpbin.org/ip", JSONObject.class);
            log.info("获取外网ip==>{}",forObject);
            return (String) forObject.get("origin");
        } catch (Exception e) {
            log.error("获取外网地址失败：", e);
            return null;
        }
    }

    /**
     * 导出excel
     */
    public String downloadExcel(List<Map<String, Object>> list, String name) throws IOException {

        String fileName = name + "_" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmssS") + ".xlsx";
        String tempPath = properties.getPath().getPath() + "excel" + File.separator + "/" + fileName;

        Path path = Paths.get(tempPath);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        File file = new File(tempPath);
        BigExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getBigWriter(file);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);

//        FileOutputStream out = new FileOutputStream(tempPath);
//        //此处记得关闭输出Servlet流
//        writer.flush(out,true);
        writer.flush();

        String cIp = getIp();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(cIp)) {
            ip = cIp;
        }

        return "http://" + ip + ":" + serverPort + (org.apache.commons.lang3.StringUtils.isBlank(contextPath) ? "/" : contextPath) + properties.getPath().getRequestpath() + "excel" + "/" + fileName;
    }
}
