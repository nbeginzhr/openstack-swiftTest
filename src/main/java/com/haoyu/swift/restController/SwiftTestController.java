package com.haoyu.swift.restController;

import com.haoyu.swift.configuration.OS3Config;
import com.haoyu.swift.manage.SwiftBiz;
import org.apache.commons.io.FilenameUtils;
import org.openstack4j.model.storage.object.SwiftObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by haoyu on 2019/1/11.
 */
@RestController
public class SwiftTestController {

    @Autowired
    private SwiftBiz swiftBiz;

    @GetMapping(value = "/api/list")
    public ResponseEntity<Object> list(){
        List<? extends SwiftObject> objects = swiftBiz.listAll(OS3Config.getEmlContainer(), null);
        return ResponseEntity.ok(objects);
    }

    @PostMapping(value = "/api/save")
    public ResponseEntity<Object> save(MultipartFile file) throws IOException {
        String save = swiftBiz.save(OS3Config.getEmlContainer(),
                file.getOriginalFilename(), file.getInputStream(), new HashMap<>());
        System.out.println("objectName-->" + file.getOriginalFilename());
        return ResponseEntity.ok(save);
    }

    @GetMapping(value = "/api/fetch")
    public ResponseEntity<Object> fetch(@RequestParam String fileName){
        SwiftObject object = swiftBiz.findObject(OS3Config.getEmlContainer(), fileName);
        return ResponseEntity.ok(object);
    }

    @GetMapping(value = "/api/download")
    public ResponseEntity<byte[]> download(HttpServletRequest request, HttpServletResponse response,
                                           @RequestParam String fileName) throws Exception {

        String fileType = FilenameUtils.getExtension(fileName);
        fileName = swiftBiz.encodeChineseDownloadFileName(request, fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        if (fileType != null && fileType.equals("pdf")) {
            response.setContentType("application/pdf");
        }

        return new ResponseEntity<>(swiftBiz.readFileToByte(OS3Config.getEmlContainer(), fileName), headers, HttpStatus.OK);
    }


}
