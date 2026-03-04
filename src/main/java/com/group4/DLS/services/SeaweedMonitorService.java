package com.group4.DLS.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.DLS.domain.dto.response.SeaweedClusterStatusResponse;
import com.group4.DLS.domain.entity.SeaweedFSProperties;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SeaweedMonitorService {

    ObjectMapper objectMapper;
    SeaweedFSProperties seaweedFSProperties;
    RestTemplate restTemplate = new RestTemplate();

    public SeaweedClusterStatusResponse getClusterStatus() {
        try {
            String url = seaweedFSProperties.getMaster().getUrl() + "/dir/status";

            ResponseEntity<String> response =
                    restTemplate.getForEntity(url, String.class);

            JsonNode json = objectMapper.readTree(response.getBody());

            int volumeSizeMb = json.path("VolumeSizeLimitMB").asInt(1024);

            long totalMaxVolumes = 0;

            JsonNode dataCenters = json.path("Topology").path("DataCenters");

            for (JsonNode dc : dataCenters) {
                for (JsonNode rack : dc.path("Racks")) {
                    for (JsonNode node : rack.path("DataNodes")) {
                        totalMaxVolumes += node.path("Max").asInt(0);
                    }
                }
            }

            int freeVolumes = json.path("Topology").path("Free").asInt(0);
            int maxVolumes = json.path("Topology").path("Max").asInt(0);
            long totalStorageMb = (long) maxVolumes * volumeSizeMb;
            long freeStorageMb = (long) freeVolumes * volumeSizeMb;
            long usedStorageMb = totalStorageMb - freeStorageMb;

            return SeaweedClusterStatusResponse.builder()
                    .Total(totalStorageMb)     // MB
                    .Used(usedStorageMb)       // MB
                    .Free(freeStorageMb)       // MB
                    .VolumeCount((int) totalMaxVolumes)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Cannot get SeaweedFS status", e);
        }
    }
}
