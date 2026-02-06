package com.group4.DLS.services;


import com.group4.DLS.domain.dto.response.SeaweedClusterStatusResponse;
import com.group4.DLS.domain.entity.SeaweedFSProperties;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SeaweedMonitorService {
    SeaweedFSProperties seaweedFSProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    public SeaweedClusterStatusResponse getClusterStatus() {
        String url = seaweedFSProperties.getMaster().getUrl() + "/cluster/status";
        return restTemplate.getForObject(url, SeaweedClusterStatusResponse.class);
    }
}
