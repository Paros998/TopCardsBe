package com.cards.serviceImplementation;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.cards.config.AmazonConfig;

import com.cards.entity.File;
import com.cards.repository.FileRepository;
import com.cards.serviceInterface.IFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileService implements IFileService {
    private final AmazonS3 s3client;
    private final AmazonConfig amazonConfig;
    private final FileRepository fileRepository;
    private final static Integer OneMB = 1_050_000;

    private String NOT_FOUND(UUID fileId) {
        return "File with id " + fileId + " doesn't exists in database";
    }

    private String TO_BIG() {
        return "File is to big";
    }

    public UUID uploadFile(MultipartFile file) {

        if (file.getSize() > OneMB) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, TO_BIG());
        }

        String extension = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
        String fileName = UUID.randomUUID().toString();
        String concatedFileName = fileName + "." + extension;

        if(fileRepository.existsByFileName(concatedFileName))
            return fileRepository.getByFileName(concatedFileName).getFileId();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            s3client.putObject(amazonConfig.bucketName, concatedFileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File newFile = new File();
        newFile.setFileName(concatedFileName);

        fileRepository.save(newFile);

        return newFile.getFileId();
    }

    public String getFileUrl(UUID fileId) {

        File file = fileRepository.findById(fileId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND(fileId))
        );

        return "https://top-cards-bucket.s3.eu-central-1.amazonaws.com/" + file.getFileName();

    }

    public List<String> getFilesUrls(List<File> files) {
        return files
                .stream()
                .map(file -> "https://top-cards-bucket.s3.eu-central-1.amazonaws.com/" + file.getFileName())
                .collect(Collectors.toList());

    }

    public void deleteFile(UUID fileId) {

        File file = getFileById(fileId);

        s3client.deleteObject(amazonConfig.bucketName, file.getFileName());

        fileRepository.deleteById(fileId);
    }

    public File getFileById(UUID fileId) {
        return fileRepository.findById(fileId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND(fileId))
        );
    }
}
