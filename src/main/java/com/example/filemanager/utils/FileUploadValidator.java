package com.example.filemanager.utils;

import java.util.Arrays;
import java.util.List;

public class FileUploadValidator {
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            "mp3", "wav", "m4a", "ogg", "wma",
            "mp4", "mov", "wmv", "avi", "mkv", "flv", "webm", "3gp", "mpeg"
    );

    public static boolean validateUpload(String extension) {
        boolean isValid = false;

        boolean anyMatch = ALLOWED_EXTENSIONS.stream()
                .anyMatch(ae -> ae.equals(extension));

        if (anyMatch) {
            isValid = true;
        }

        return isValid;
    }
}

