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

    /**
     * Tải ảnh từ một URL về máy và lưu vào đường dẫn target.
     * @param imageUrl: URL của ảnh
     * @param target: Path muốn lưu ảnh
     */
    public static void downloadImage(String imageUrl, Path target) {
        try (InputStream in = new URL(imageUrl).openStream()) { // Mở stream từ URL
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING); // Copy dữ liệu từ stream sang file, ghi đè nếu file tồn tại
        } catch (Exception e) {
            System.out.println("Lỗi tải ảnh: " + imageUrl); // Nếu lỗi, in ra URL lỗi
        }
    }

    /**
     * Tạo file classes.txt chứa danh sách nhãn (label) theo thứ tự index.
     * @param baseDir: thư mục lưu file
     * @param labelMap: Map label -> index
     * @throws IOException
     */
    public static void writeClassesFile(File baseDir, Map<String, Integer> labelMap) throws IOException {

        File classesFile = new File(baseDir, "classes.txt"); // Tạo file classes.txt trong baseDir
        BufferedWriter writer = new BufferedWriter(new FileWriter(classesFile)); // BufferedWriter để ghi nhanh

        // Sắp xếp label theo index (giá trị) rồi ghi từng dòng
        labelMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()) // sort theo value (index)
                .forEach(e -> {
                    try {
                        writer.write(e.getKey()); // ghi tên label
                        writer.newLine();        // xuống dòng
                    } catch (IOException ex) {
                        throw new RuntimeException(ex); // nếu lỗi ghi file, ném RuntimeException
                    }
                });

        writer.close(); // đóng writer sau khi ghi xong
    }

    /**
     * Nén một thư mục thành file zip
     * @param sourceFolder: thư mục cần nén
     * @param zipFile: file zip đầu ra
     * @throws IOException
     */
    public static void zipFolder(File sourceFolder, File zipFile) throws IOException {

        try (FileOutputStream fos = new FileOutputStream(zipFile); // mở FileOutputStream để ghi zip
             ZipOutputStream zos = new ZipOutputStream(fos)) {      // ZipOutputStream để tạo zip

            zipRecursive(sourceFolder, sourceFolder.getName(), zos); // gọi recursive function để thêm tất cả file
        }
    }

    /**
     * Hàm đệ quy nén từng file/thư mục vào ZipOutputStream
     * @param fileToZip: file hoặc thư mục hiện tại
     * @param fileName: tên file trong zip
     * @param zos: ZipOutputStream
     * @throws IOException
     */
    private static void zipRecursive(File fileToZip, String fileName, ZipOutputStream zos)
            throws IOException {

        if (fileToZip.isHidden()) return; // bỏ qua file ẩn

        if (fileToZip.isDirectory()) { // nếu là thư mục
            if (!fileName.endsWith("/")) fileName += "/"; // đảm bảo tên thư mục kết thúc bằng "/"
            zos.putNextEntry(new ZipEntry(fileName));    // tạo entry thư mục trong zip
            zos.closeEntry();                             // đóng entry

            // duyệt tất cả file con
            for (File child : fileToZip.listFiles()) {
                zipRecursive(child, fileName + child.getName(), zos); // gọi đệ quy với file con
            }
            return;
        }

        // nếu là file, ghi nội dung file vào zip
        FileInputStream fis = new FileInputStream(fileToZip); // mở file để đọc
        zos.putNextEntry(new ZipEntry(fileName));            // tạo entry trong zip

        byte[] bytes = new byte[1024];                       // buffer 1KB
        int length;
        while ((length = fis.read(bytes)) >= 0) {            // đọc từng chunk
            zos.write(bytes, 0, length);                     // ghi vào zip
        }

        fis.close(); // đóng file input stream
    }
}