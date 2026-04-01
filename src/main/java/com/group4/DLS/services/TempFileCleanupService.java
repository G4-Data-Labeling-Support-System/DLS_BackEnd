package com.group4.DLS.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class TempFileCleanupService {

    private static final long MAX_AGE = 60 * 1000; // 1 phút

    // chạy mỗi 5 phút
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void cleanTempFiles() {

        File tempDir = new File(System.getProperty("java.io.tmpdir"));

        if (!tempDir.exists()) {
            log.warn("Temp folder không tồn tại");
            return;
        }

        File[] files = tempDir.listFiles();
        if (files == null) {
            log.warn("Temp folder rỗng");
            return;
        }

        for (File file : files) {

            // chỉ xóa file export của bạn
            if (!isExportFile(file)) continue;

            long age = System.currentTimeMillis() - file.lastModified();

            if (age > MAX_AGE) {
                boolean deleted = deleteRecursively(file);

                if (deleted) {
                    log.info("🗑 Deleted: {}", file.getAbsolutePath());
                } else {
                    log.error(" Failed to delete: {}", file.getAbsolutePath());
                }
            }
        }
    }

    // chỉ match yolo + coco
    private boolean isExportFile(File file) {
        String name = file.getName();
        return name.startsWith("yolo_") || name.startsWith("coco_");
    }

    // xóa cả folder + file
    private boolean deleteRecursively(File file) {

        if (!file.exists()) return false;

        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursively(child);
                }
            }
        }

        return file.delete();
    }
}