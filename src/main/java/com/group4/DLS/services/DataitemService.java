package com.group4.DLS.services;

import com.group4.DLS.domain.dto.response.DataItemResponse;
import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.enums.DataType;
import com.group4.DLS.domain.entity.enums.FileFormat;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.DataItemMapper;
import com.group4.DLS.repositories.DataItemRepository;
import com.group4.DLS.repositories.DatasetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DataitemService {
    private final DatasetRepository datasetRepository;

    private final DataItemRepository dataitemRepository;

    private final SeaweedFilerUploadService seaweedFilerUploadService;
    private final DataItemMapper dataItemMapper;


    //get dataitem by id
    public DataItemResponse getDataitemById(String dataitemId) {
        Dataitem dataitem = dataitemRepository.findById(dataitemId).orElseThrow(() -> new AppException(ErrorCode.DATAITEM_NOT_FOUND));
        return dataItemMapper.toDataItemResponse(dataitem);
    }

    //get all dataitem for dataset
    public List<DataItemResponse> getAllDataitemForDataset(String datasetId) {
        //check dataset exist
        Dataset dataset = datasetRepository.findById(datasetId).orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

        List<Dataitem> dataitems = dataitemRepository.findByDataset_DatasetId(datasetId);

        return dataItemMapper.toDataItemResponse(dataitems);
    }

    //create dataitem for dataset
    @Transactional
    public int createDataitem(String datasetId, List<MultipartFile> files) throws IOException {
        //check dataset exist
        Dataset dataset = datasetRepository.findById(datasetId).orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

        int count = 0;
        List<Dataitem> items = new ArrayList<>();
        for (MultipartFile file : files) {

            // đọc ảnh
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image == null){
                throw new AppException(ErrorCode.INVALID_IMAGE_FILE);
            }

            int width = image.getWidth();
            int height = image.getHeight();

            //lấy file format
            String filename = file.getOriginalFilename();

            if(filename == null || !filename.contains(".")){
                throw new AppException(ErrorCode.INVALID_FILE_FORMAT);
            }

            String fileFormat = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();


            // 1 upload file lên SeaweedFS
            String fileUrl = seaweedFilerUploadService.uploadImage(file, "dataset-" + datasetId);

            // 2 tạo Dataitem
            Dataitem item = new Dataitem();
            item.setFileName(UUID.randomUUID()+"-"+file.getOriginalFilename());
            item.setUrl(fileUrl);
            item.setFileSize((int) file.getSize());
            item.setWidth(width);
            item.setHeight(height);
            item.setFileFormat(FileFormat.valueOf(fileFormat));
            item.setDataType(DataType.IMAGE);
            item.setDataset(dataset);
            count++;
            items.add(item);
        }

        dataitemRepository.saveAll(items);
        return count;
    }
}
