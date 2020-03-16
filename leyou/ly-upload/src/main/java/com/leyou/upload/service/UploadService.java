package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.upload.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2019/12/5 19:10
 */
@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)//这个就是为了使用将配置文件中的配置，配置被加载到实体类当中去了，
// 这边就引用实体类那边的东西，但是还需要加上一个autowired注入那个类。
//百度上说这个注解的作用就是开启在Config包下的UploadProperties里面的ConfigurationProperties这个注解。
public class UploadService {

    @Autowired
    private UploadProperties prop;

    @Autowired
    private FastFileStorageClient storageClient;


    //    Arrays.asList()：将元素转换成集合。这个地方由于引入了配置文件所以不需要这一步了。
//    private static final List<String> ALLOW_TYPES = Arrays.asList("image/jepg", "image/png", "image/bmp");

    public String uploadImage(MultipartFile file) {
        try {
//            校验文件类型
            String contentType = file.getContentType();
//            if (!ALLOW_TYPES.contains(contentType)) {
            if (!prop.getAllowTypes().contains(contentType)) {
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }
//            校验文件内容，如果是图片就返回一个BufferedImage如果不是图片就会返回一个空或者异常。
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }
//        这边还可以校验图片的宽啊高啊，等等，我感觉可以吧校验图片的文件类型，和内容都放在common工具类里面
//        保存文件到本地
//        本地方法：目标路径，前面是目录，后面是文件的名字
//            File dest = new File("D:\\leyouuploadImage", file.getOriginalFilename());
////        本地方法。保存文件到本地
//            file.transferTo(dest);

//            保存文件到FDFS
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
//         本地返回路径
//            return "http://image.leyou.com/" + file.getOriginalFilename();
//            FDFS返回路径
            return prop.getBaseUrl() + storePath.getFullPath();
        } catch (IOException e) {
//            上传失败应该记录日志返回结果
            log.error("【文件上传】上传文件失败" + e);
            throw new LyException(ExceptionEnum.UPLOAD_FILE_ERRO);
        }

    }
}
