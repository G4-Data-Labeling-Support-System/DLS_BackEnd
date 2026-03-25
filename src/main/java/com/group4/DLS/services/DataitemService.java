package com.group4.DLS.services;

import com.group4.DLS.aop.LogActivity;
import com.group4.DLS.domain.dto.response.DataItemResponse;
import com.group4.DLS.domain.entity.*;
import com.group4.DLS.domain.enums.*;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.DataItemMapper;
import com.group4.DLS.repositories.*;
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
    private final TaskDataItemRepository taskDataItemRepository;

    private final TaskRepository taskRepository;
    private final AssignmentRepository assignmentRepository;
    private final SeaweedFilerUploadService seaweedFilerUploadService;
    private final DataItemMapper dataItemMapper;

    // ================= GET DATAITEM BY DATAITEM ID =================
    public DataItemResponse getDataitemById(String dataitemId) {
        Dataitem dataitem = dataitemRepository.findById(dataitemId).orElseThrow(() -> new AppException(ErrorCode.DATAITEM_NOT_FOUND));
        return dataItemMapper.toDataItemResponse(dataitem);
    }

    // ================= GET DATAITEM BY DATASET ID =================
    public List<DataItemResponse> getAllDataitemForDataset(String datasetId) {
        //check dataset exist
        if(datasetRepository.findById(datasetId).isEmpty()){
            throw new AppException(ErrorCode.DATASET_NOT_FOUND);
        }

        List<Dataitem> dataitems = dataitemRepository.findByDataset_DatasetId(datasetId).stream()
                .filter(dataitem -> dataitem.getDataItemStatus() == DataItemStatus.ACTIVE).toList();

        return dataItemMapper.toDataItemResponse(dataitems);
    }

    // ================= CREATE NEW DATAITEM BY DATASET ID =================
    @Transactional
    @LogActivity(
        action = "CREATE",
        entity = "Dataitem",
        description = "Create dataitem by datasetId",
        entityIdField = "DatasetId"
    )
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
            item.setFileName(datasetId+"-"+file.getOriginalFilename());
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

    // ================= ASSIGN NEW DATAITEM =================
    @Transactional
    @LogActivity(
        action = "CREATE",
        entity = "Dataitem",
        description = "Assign new dataitems by datasetid",
        entityIdField = "datasetId"
    )
    public void assignNewDataItems(String datasetId) {

        Assignment assignment = assignmentRepository.findByDatasetDatasetId(datasetId);

        //tìm các item mới thêm và chưa đươc assign vào taskitem
        List<Dataitem> newItems = dataitemRepository.findUnassignedDataItems(datasetId);

        if (newItems.isEmpty()) {
            return;
        }

        int maxItemsPerTask = 20;

        //list task theo thời gian tạo
        List<Task> tasks = taskRepository
                .findByAssignmentAssignmentIdOrderByCreatedAtAsc(assignment.getAssignmentId());

        // lấy task cúi
        Task currentTask = tasks.get(tasks.size() - 1);

        //đếm số iteam trong task cúi
        int currentCount = taskDataItemRepository.countByTaskTaskId(currentTask.getTaskId());

        List<TaskDataItem> taskItems = new ArrayList<>();

        for (Dataitem item : newItems) {

            // so sánh xem thử xem task cúi còn thiếu item không
            if (currentCount >= maxItemsPerTask) {

                int taskNumber = tasks.size() + 1;
                // tạo task mới
                currentTask = new Task();
                currentTask.setAssignment(assignment);
                currentTask.setTaskStatus(TaskStatus.NOT_STARTED);
                currentTask.setTaskType(TaskType.BATCH);
                currentTask.setTaskName("TASK-" + String.format("%02d", taskNumber));

                taskRepository.save(currentTask);

                tasks.add(currentTask);
                currentCount = 0;
            }

            TaskDataItem taskItem = new TaskDataItem();
            taskItem.setTask(currentTask);
            taskItem.setDataitem(item);
            taskItem.setItemIndex(currentCount + 1);


            taskItems.add(taskItem);

            currentCount++;
        }

        taskDataItemRepository.saveAll(taskItems);
    }

    // ================= REMOVE DATAITEM =================
    @Transactional
    @LogActivity(
        action = "DELETE",
        entity = "Dataitem",
        description = "Delete dataitem",
        entityIdField = "dataitemId"
    )
    public void deleteDataitem(String dataitemId) {

        Dataitem dataitem = dataitemRepository.findById(dataitemId)
                .orElseThrow(() -> new AppException(ErrorCode.DATAITEM_NOT_FOUND));

        dataitem.setDataItemStatus(DataItemStatus.INACTIVE);
        //  xóa dataitem
        dataitemRepository.save(dataitem);
    }

    @LogActivity(
            action = "DELETE",
            entity = "Dataitem",
            description = "Delete Dataitems",
            entityIdParam = "dataitemId"
    )
    public void deleteDataitems(List<String> dataitemIds) {
        List<Dataitem> dataitems = new ArrayList<>();
        for(String dataitemId: dataitemIds) {
            Dataitem dataitem = dataitemRepository.findById(dataitemId)
                    .orElseThrow(() -> new AppException(ErrorCode.DATAITEM_NOT_FOUND));

            dataitem.setDataItemStatus(DataItemStatus.INACTIVE);
            dataitems.add(dataitem);
        }
        //  xóa dataitem
        dataitemRepository.saveAll(dataitems);
    }

    public void deleteDataitemsByDatasetId(String datasetId) {
        List<Dataitem> dataitems = dataitemRepository.findByDataset_DatasetId(datasetId);
        for (Dataitem dataitem : dataitems) {
            deleteDataitem(dataitem.getItemId());
        }
    }
}
