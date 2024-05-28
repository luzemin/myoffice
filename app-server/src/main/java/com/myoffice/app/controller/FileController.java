/**
 * (c) Copyright Ascensio System SIA 2024
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myoffice.app.controller;

import com.myoffice.app.common.R;
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
            String filesDir = getFileDir() + RandomUtils.code();
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
        File dir = new File(getFileDir() + fileId);
        File file = dir.listFiles()[0];
        String fileName = file.getName();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(file.length())
                .body(new InputStreamResource(new FileInputStream(file)));
    }

    private String getFileDir() {
        return System.getProperty("user.dir") + "/files/";
    }
}
