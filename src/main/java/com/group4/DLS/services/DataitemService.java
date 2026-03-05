package com.group4.DLS.services;

import com.group4.DLS.domain.dto.response.DataItemResponse;
import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.DataItemMapper;
import com.group4.DLS.repositories.DataItemRepository;
import com.group4.DLS.repositories.DatasetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataitemService {
    DataItemRepository dataitemRepository;
    DatasetRepository datasetRepository;
    DataItemMapper dataItemMapper;

    //get all dataitem for dataset
    public List<DataItemResponse> getAllDataitemForDataset(String datasetId) {
        //check dataset exist
        Dataset dataset = datasetRepository.findById(datasetId).orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

        List<Dataitem> dataitems = dataitemRepository.findByDataset_DatasetId(datasetId);

        return dataItemMapper.toDataItemResponse(dataitems);
    }
}
