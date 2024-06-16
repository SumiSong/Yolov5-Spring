package com.example.yolov5Project.draw.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class DrawFileStorageService {
    private final Path fileStorageLocation;

    public DrawFileStorageService(@Value("${file.upload-dir-canvas}") String uploadDir) {
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
            // Check if the file's name contains invalid characters
            if (normalizedFileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + normalizedFileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(normalizedFileName);
            Files.copy(new ByteArrayInputStream(fileBytes), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/canvas/" + normalizedFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + normalizedFileName + ". Please try again!", ex);
        }
    }
}
