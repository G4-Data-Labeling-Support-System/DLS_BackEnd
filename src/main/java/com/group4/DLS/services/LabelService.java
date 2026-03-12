package com.group4.DLS.services;

import com.group4.DLS.domain.dto.request.LabelCreationRequest;
import com.group4.DLS.domain.dto.request.LabelUpdateRequest;
import com.group4.DLS.domain.dto.response.LabelResponse;
import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.Label;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.LabelMapper;
import com.group4.DLS.repositories.DatasetRepository;
import com.group4.DLS.repositories.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LabelService {

    private final LabelRepository labelRepository;
    private final DatasetRepository datasetRepository;
    private final LabelMapper labelMapper;

    /*
     * ======================
     * CREATE
     * ======================
     */
    public LabelResponse create(String datasetId, LabelCreationRequest request) {

        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

        // check duplicate label name in same dataset
        if (labelRepository.existsByLabelNameAndDataset_DatasetId(
                request.getLabelName(),
                datasetId)) {
            throw new AppException(ErrorCode.LABEL_ALREADY_EXISTS);
        }

        Label label = labelMapper.toLabel(request);
        label.setDataset(dataset);

        Label saved = labelRepository.save(label);
        // setLabels for dataset to update label count
        dataset.setLabels(labelRepository.findByDataset_DatasetId(datasetId));
        datasetRepository.save(dataset);

        return labelMapper.toLabelResponse(saved);
    }
    /*
     * ======================
     * GET ALL LABEL KHONG THEO GI
     * ======================
     */

    @Transactional(readOnly = true)
    public List<LabelResponse> getAllLabels() {

        return labelRepository.findAll()
                .stream()
                .map(labelMapper::toLabelResponse)
                .toList();
    }

    /*
     * ======================
     * GET ALL LABEL BY DATASET
     * ======================
     */
    @Transactional(readOnly = true)
    public List<LabelResponse> getAllByDataset(String datasetId) {

        if (!datasetRepository.existsById(datasetId)) {
            throw new AppException(ErrorCode.DATASET_NOT_FOUND);
        }

        return labelRepository.findByDataset_DatasetId(datasetId)
                .stream()
                .map(labelMapper::toLabelResponse)
                .toList();
    }

    /*
     * ======================
     * GET BY ID
     * ======================
     */
    @Transactional(readOnly = true)
    public LabelResponse getById(String labelId) {

        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new AppException(ErrorCode.LABEL_NOT_FOUND));

        return labelMapper.toLabelResponse(label);
    }

    /*
     * ======================
     * UPDATE
     * ======================
     */
    public LabelResponse update(String labelId, LabelUpdateRequest request) {

        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new AppException(ErrorCode.LABEL_NOT_FOUND));

        // check duplicate only if name changed
        if (request.getLabelName() != null &&
                !request.getLabelName().equals(label.getLabelName()) &&
                labelRepository.existsByLabelNameAndDataset_DatasetId(
                        request.getLabelName(),
                        label.getDataset().getDatasetId())) {

            throw new AppException(ErrorCode.LABEL_ALREADY_EXISTS);
        }

        labelMapper.updateLabel(label, request);

        Label updated = labelRepository.save(label);

        return labelMapper.toLabelResponse(updated);
    }

    /*
     * ======================
     * DELETE
     * ======================
     */
    public void delete(String labelId) {

        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new AppException(ErrorCode.LABEL_NOT_FOUND));

        labelRepository.delete(label);
    }
}