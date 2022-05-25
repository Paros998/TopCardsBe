package com.cards.serviceInterface;

import com.cards.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IFileService {

    UUID uploadFile(MultipartFile file);

    String getFileUrl(UUID fileId);

    File getFileById(UUID fileId);

    List<String> getFilesUrls(List<File> files);

    void deleteFile(UUID fileId);
}
