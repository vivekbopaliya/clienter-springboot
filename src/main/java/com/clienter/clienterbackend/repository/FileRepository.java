package com.clienter.clienterbackend.repository;

import com.clienter.clienterbackend.entity.FileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends MongoRepository<FileEntity, String> {
//    @Query("SELECT f FROM File f WHERE f.folder IS NULL AND f.user = :user")
//    List<FileEntity> findFilesOnHomepage(@Param("user") UserEntity user);

    @Query(value = " { 'folderId': 'root', 'userId' : ?0 }")
    List<FileEntity> findFilesOnRoot(String userId);

    @Query(value = "{ 'folderId' : ?0, 'userId' : ?1 }")
    List<FileEntity> findFilesByFolderId(String folderId, String userId);

}
