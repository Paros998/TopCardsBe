package com.cards.repository;

import com.cards.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID>, JpaSpecificationExecutor<File> {
    boolean existsByFileName(String concatedFileName);

    File getByFileName(String concatedFileName);
}
