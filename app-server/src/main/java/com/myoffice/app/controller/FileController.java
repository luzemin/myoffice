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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("file")
public class FileController {

    @PostMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            String filesDir = Constants.FILE_DIR  + RandomUtils.code();
            File filesDirectory = new File(filesDir);
            if (!filesDirectory.exists()) {
                filesDirectory.mkdir();
            }

            File savedFile = new File(filesDirectory + "/" + fileName);
            file.transferTo(savedFile);
        } catch (IOException e) {

            return R.error("failed to upload file");
        }

        return R.success("success", fileName);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(String fileId) throws FileNotFoundException {
        File dir = new File(Constants.FILE_DIR + fileId);
        File file = dir.listFiles()[0];
        String fileName = file.getName();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(file.length())
                .body(new InputStreamResource(new FileInputStream(file)));
    }
}
