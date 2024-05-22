package com.clienter.clienterbackend.repository;

import com.clienter.clienterbackend.entity.FolderEntity;
import com.clienter.clienterbackend.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepositroy extends MongoRepository <FolderEntity, String>{
    List<FolderEntity> findByUserId(String userId);

    // Query annotation to find root folders for a specific user
    @Query("{'userId': ?0, 'folderId': 'root'}")
    List<FolderEntity> findRootFolders(String userId);

    // Method to find folders by folder ID
    @Query("{ 'folderId': ?0 }")
    List<FolderEntity> findByFolderId(String folderId);

}
