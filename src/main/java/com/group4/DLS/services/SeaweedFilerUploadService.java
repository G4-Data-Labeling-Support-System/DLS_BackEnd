package com.group4.DLS.services;

import com.group4.DLS.domain.entity.SeaweedFSProperties;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class SeaweedFilerUploadService {
    private final SeaweedFSProperties seaweedFSProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    public SeaweedFilerUploadService(SeaweedFSProperties seaweedFSProperties) {
        this.seaweedFSProperties = seaweedFSProperties;
    }

    public String uploadImage(MultipartFile file, String folder) throws IOException {

        String filename = UUID.randomUUID() + file.getOriginalFilename().replaceAll("\\s+", "_");;

        // URL upload vào Filer
        String uploadUrl = seaweedFSProperties.getFiler().getUrl()
                + "/" + folder + "/" + filename;

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return filename;
            }
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body, headers);

        restTemplate.postForEntity(uploadUrl, request, String.class);

        // URL public để lưu DB
        return seaweedFSProperties.getFiler().getUrl()
                + "/" + folder + "/" + filename;
    }

    public void deleteImageByUrl(String relativePath) {
        try {
            restTemplate.delete(relativePath);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
