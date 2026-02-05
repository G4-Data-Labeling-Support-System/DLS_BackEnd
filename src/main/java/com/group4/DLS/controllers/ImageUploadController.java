package com.group4.DLS.controllers;

import com.group4.DLS.services.SeaweedFilerUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageUploadController {

    @Autowired
    private SeaweedFilerUploadService uploadfilerUploadService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file) throws Exception {

        String imageUrl = uploadfilerUploadService.uploadImage(file, "test");

        // tại đây bạn lưu imageUrl vào DB
        return ResponseEntity.ok(imageUrl);
    }
}
