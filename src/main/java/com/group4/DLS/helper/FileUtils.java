package com.group4.DLS.helper;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    public static void downloadImage(String imageUrl, Path target) {
        try (InputStream in = new URL(imageUrl).openStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println("Lỗi tải ảnh: " + imageUrl);
        }
    }

    public static void writeClassesFile(File baseDir, Map<String, Integer> labelMap) throws IOException {

        File classesFile = new File(baseDir, "classes.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(classesFile));

        labelMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(e -> {
                    try {
                        writer.write(e.getKey());
                        writer.newLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

        writer.close();
    }

    public static void zipFolder(File sourceFolder, File zipFile) throws IOException {

        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            zipRecursive(sourceFolder, sourceFolder.getName(), zos);
        }
    }

    private static void zipRecursive(File fileToZip, String fileName, ZipOutputStream zos)
            throws IOException {

        if (fileToZip.isHidden()) return;

        if (fileToZip.isDirectory()) {
            if (!fileName.endsWith("/")) fileName += "/";
            zos.putNextEntry(new ZipEntry(fileName));
            zos.closeEntry();

            for (File child : fileToZip.listFiles()) {
                zipRecursive(child, fileName + child.getName(), zos);
            }
            return;
        }

        FileInputStream fis = new FileInputStream(fileToZip);
        zos.putNextEntry(new ZipEntry(fileName));

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        fis.close();
    }
}
