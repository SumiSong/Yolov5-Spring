package com.example.yolov5Project.study.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

        private final Path fileStorageLocation;

        public FileStorageService(@Value("${file.upload-dir-webcam}") String uploadDir) {
            this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
            }
        }

        public String storeFile(byte[] fileBytes, String fileName) {
            // Normalize file name
            String normalizedFileName = StringUtils.cleanPath(fileName);

            try {
                if (normalizedFileName.contains("..")) {
                    throw new RuntimeException("Sorry! Filename contains invalid path sequence " + normalizedFileName);
                }
                Path targetLocation = this.fileStorageLocation.resolve(normalizedFileName);
                Files.copy(new ByteArrayInputStream(fileBytes), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                return "/uploads/webcam/" + normalizedFileName;
            } catch (IOException ex) {
                throw new RuntimeException("Could not store file " + normalizedFileName + ". Please try again!", ex);
            }
        }

}
