package com.myoffice.app.controller;

import com.myoffice.app.common.R;
import com.myoffice.app.constant.Constants;
import com.myoffice.app.utils.RandomUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;

@RestController
public class FileController {

    @PostMapping("/api/file/upload")
    public R upload(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String fileId = RandomUtils.code();

            File filesDirectory = new File(Constants.FILE_DIR + fileId);
            if (!filesDirectory.exists()) {
                filesDirectory.mkdir();
            }

            File savedFile = new File(filesDirectory + "/" + fileName);
            file.transferTo(savedFile);
            return R.success("success", fileId);

        } catch (IOException e) {

            return R.error("failed to upload file");
        }
    }

    @GetMapping("/api/file/download")
    public ResponseEntity<Resource> download(String fileId) throws FileNotFoundException, UnsupportedEncodingException {
        File dir = new File(Constants.FILE_DIR + fileId);
        File file = dir.listFiles()[0];
        String fileName = URLEncoder.encode(file.getName(), "UTF-8");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(file.length())
                .body(new InputStreamResource(new FileInputStream(file)));
    }
}
