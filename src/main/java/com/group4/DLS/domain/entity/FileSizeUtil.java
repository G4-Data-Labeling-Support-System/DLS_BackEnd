package com.group4.DLS.domain.entity;

public class FileSizeUtil {
    public static double bytesToGB(long bytes) {
        return Math.round(bytes / 1024.0 / 1024 / 1024 * 100.0) / 100.0;
    }
}
