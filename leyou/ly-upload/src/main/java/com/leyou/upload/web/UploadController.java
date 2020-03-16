package com.leyou.upload.web;

import com.leyou.upload.service.UploadService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2019/12/5 18:55
 */
@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    /**
     * 上传图
     * @param file
     * @return
     */
    @PostMapping("image")
//    springmvc会吧这个参数封装到这个MultipartFile这个对象里面来
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file){

        return ResponseEntity.ok(uploadService.uploadImage(file));


    }
}
