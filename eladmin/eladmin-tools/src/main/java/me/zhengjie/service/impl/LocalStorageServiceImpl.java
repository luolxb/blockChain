/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.config.FileProperties;
import me.zhengjie.domain.LocalStorage;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.repository.LocalStorageRepository;
import me.zhengjie.service.LocalStorageService;
import me.zhengjie.service.dto.LocalStorageDto;
import me.zhengjie.service.dto.LocalStorageQueryCriteria;
import me.zhengjie.service.mapstruct.LocalStorageMapper;
import me.zhengjie.utils.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @author Zheng Jie
 * @date 2019-09-05
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LocalStorageServiceImpl implements LocalStorageService {

    private final LocalStorageRepository localStorageRepository;
    private final LocalStorageMapper localStorageMapper;
    private final FileProperties properties;
    @Value("${server.port}")
    private Integer serverPort;
    @Value("${file.ip}")
    private String ip;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Object queryAll(LocalStorageQueryCriteria criteria, Pageable pageable) {
        Page<LocalStorage> page = localStorageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(localStorageMapper::toDto));
    }

    @Override
    public List<LocalStorageDto> queryAll(LocalStorageQueryCriteria criteria) {
        return localStorageMapper.toDto(localStorageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public LocalStorageDto findById(Long id) {
        LocalStorage localStorage = localStorageRepository.findById(id).orElseGet(LocalStorage::new);
        ValidationUtil.isNull(localStorage.getId(), "LocalStorage", "id", id);
        return localStorageMapper.toDto(localStorage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> create(String name, MultipartFile multipartFile) {
        if ( multipartFile.getSize() <= 0) {
            throw new BadRequestException("上传失败,请选择上传文件");
        }
        FileUtil.checkSize(properties.getMaxSize(), multipartFile.getSize());
        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        String type = FileUtil.getFileType(suffix);
        File file = FileUtil.upload(multipartFile, properties.getPath().getPath() + type + File.separator);
        if (ObjectUtil.isNull(file)) {
            throw new BadRequestException("上传失败");
        }
        String cIp = getIp();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(cIp)) {
            ip = cIp;
        }
        try {
            name = StringUtils.isBlank(name) ? FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename()) : name;
            LocalStorage localStorage = new LocalStorage(
                    file.getName(),
                    name,
                    suffix,
                    file.getPath(),
                    type,
                    FileUtil.getSize(multipartFile.getSize()),
                    getRequestPath(ip, properties.getPath().getRequestpath(), type, file.getName())
            );

            LocalStorage localStorage1 = localStorageRepository.save(localStorage);
            Map<String, Object> map = new HashMap<>(2);
            map.put("storageId", localStorage1.getId());
            map.put("requestPath", localStorage1.getRequestPath());
            return map;
        } catch (Exception e) {
            FileUtil.del(file);
            throw e;
        }
    }

    private String getRequestPath(String ip, String path, String type, String name) {
        return "http://" + ip + ":" + serverPort + (org.apache.commons.lang3.StringUtils.isBlank(contextPath) ? "/" : contextPath) + path + type + "/" + name;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(LocalStorage resources) {
        LocalStorage localStorage = localStorageRepository.findById(resources.getId()).orElseGet(LocalStorage::new);
        ValidationUtil.isNull(localStorage.getId(), "LocalStorage", "id", resources.getId());
        localStorage.copy(resources);
        localStorageRepository.save(localStorage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            LocalStorage storage = localStorageRepository.findById(id).orElseGet(LocalStorage::new);
            FileUtil.del(storage.getPath());
            localStorageRepository.delete(storage);
        }
    }

    @Override
    public void download(List<LocalStorageDto> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LocalStorageDto localStorageDTO : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("文件名", localStorageDTO.getRealName());
            map.put("备注名", localStorageDTO.getName());
            map.put("文件类型", localStorageDTO.getType());
            map.put("文件大小", localStorageDTO.getSize());
            map.put("创建者", localStorageDTO.getCreateBy());
            map.put("创建日期", localStorageDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response, "test");
    }

    @Override
    public Map<String, Object> createBase64(String imageBase64) {
        JSONObject jsonObject = JSON.parseObject(imageBase64);
        String imageBase64Str = (String) jsonObject.get("imageBase64");
//        imageBase64 = imageBase64.replace("data:image/jpeg;base64", "");

        int v = base64fileSize(imageBase64Str);
        if (v / 1024 > 200) {
            throw new BadRequestException("上传文件不能超过200KB");
        }
        String cIp = getIp();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(cIp)) {
            ip = cIp;
        }

        String path = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + System.currentTimeMillis() + ".jpg";
        String imagePath = properties.getPath().getPath() + "/image/" + path;

        LocalStorage localStorage = new LocalStorage(
                "",
                "name",
                "",
                imagePath,
                "jpg",
                FileUtil.getSize(Long.parseLong(v + "")),
                getRequestPath(ip, properties.getPath().getRequestpath(), "image", path)
        );

        LocalStorage localStorage1 = localStorageRepository.save(localStorage);
        base64ChangeImage(imageBase64Str, imagePath);
        Map<String, Object> map = new HashMap<>(2);
        map.put("storageId", localStorage1.getId());
        map.put("requestPath", localStorage1.getRequestPath());
        return map;
    }

    /**
     * BASE转图片
     *
     * @param baseStr   base64字符串
     * @param imagePath 生成的图片
     * @return
     */
    public void base64ChangeImage(String baseStr, String imagePath) {
        if (baseStr == null) {
            return;
        }
        // 如果base码 有， 说明没有去掉前缀
        if (baseStr.contains(",")) {
            baseStr = baseStr.substring(baseStr.indexOf(",") + 1);
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
//            // 解密  可能会导致PNG格式的图片变红
//            byte[] b = decoder.decodeBuffer(baseStr);
//            ByteArrayInputStream bais = new ByteArrayInputStream(b);
//            BufferedImage bi1 = ImageIO.read(bais);
//            File f1 = new File(imagePath);
//            ImageIO.write(bi1, "jpg", f1);

            //Base64解码
            byte[] b = decoder.decodeBuffer(baseStr);
            for (int i = 0; i < b.length; ++i) {
                //调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            OutputStream out = new FileOutputStream(imagePath);
            out.write(b);
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("e", e);
            throw new BadRequestException("上传图片发生异常");
        }
    }

    int base64fileSize(String base64String) {
        //1.获取base64字符串长度(不含data:audio/wav;base64,文件头)
        int size0 = base64String.length();
        //2.获取字符串的尾巴的最后10个字符，用于判断尾巴是否有等号，正常生成的base64文件'等号'不会超过4个
        String tail = base64String.substring(size0 - 10);
        //3.找到等号，把等号也去掉,(等号其实是空的意思,不能算在文件大小里面)
        int equalIndex = tail.indexOf("=");
        if (equalIndex > 0) {
            size0 = size0 - (10 - equalIndex);
        }
        //4.计算后得到的文件流大小，单位为字节
        return size0 - (size0 / 8) * 2;
    }

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


}
