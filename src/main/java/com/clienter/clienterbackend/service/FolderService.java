package com.clienter.clienterbackend.service;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.clienter.clienterbackend.config.JwtService;
import com.clienter.clienterbackend.config.request.CreateFolderRequest;
import com.clienter.clienterbackend.config.request.RenameFolderRequest;
import com.clienter.clienterbackend.entity.FileEntity;
import com.clienter.clienterbackend.entity.FolderEntity;
import com.clienter.clienterbackend.entity.UserEntity;
import com.clienter.clienterbackend.repository.FileRepository;
import com.clienter.clienterbackend.repository.FolderRepositroy;
import com.clienter.clienterbackend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.*;

@Component
public class FolderService {


    @Autowired
    private FolderRepositroy repositroy;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final JwtService jwtService;

    public FolderService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public ResponseEntity<Map<String, Object>> createFolder(CreateFolderRequest request, HttpServletRequest httpServletRequest) {
        try
        {

        String userId = jwtService.extractUserIdFromRequest(httpServletRequest);
        System.out.println(userId);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error", "Unauthorized!"));

        }


        FolderEntity folder = new FolderEntity();
        folder.setName(request.getName());
        folder.setUserId(userId);
        folder.setFolderId((request.getFolderId() == null || request.getFolderId().isEmpty()) ? "root" : request.getFolderId());

        repositroy.save(folder);

        return ResponseEntity.ok(Map.of("message", "Folder created successfully.", "folder", folder));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("error", "Something went wrong on server side!"));
        }
    }
    public ResponseEntity<Map<String, Object>> renameFolder(RenameFolderRequest request, HttpServletRequest httpServletRequest) throws AccessDeniedException {
        try {
            String userId = jwtService.extractUserIdFromRequest(httpServletRequest);

            if (userId == null){
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized!"));
            }

            if (request.getFolderId() == null || request.getName() == null){
                return ResponseEntity.status(400).body(Map.of("error", "Please provide valid data"));
            }

            Optional<FolderEntity> folder = repositroy.findById(request.getFolderId());

            if (folder.isEmpty()){
                return ResponseEntity.status(404).body(Map.of("error", "Folder not found"));
            }

            FolderEntity folderEntity = folder.get();
            folderEntity.setName(request.getName());

            repositroy.save(folderEntity);

            return ResponseEntity.ok(Map.of("message", "Folder renamed!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    public ResponseEntity<Map<String, String>> deleteFolder(String folderId, HttpServletRequest httpServletRequest)  {

        try {
            String userId = jwtService.extractUserIdFromRequest(httpServletRequest);

            if (userId == null){
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized!"));
            }

            if (folderId == null){
                return ResponseEntity.status(400).body(Map.of("error", "Please enter valid data"));
            }

             repositroy.deleteById(folderId);
            return ResponseEntity.status(200).body(Map.of("error", "Folder deleted!"));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    public ResponseEntity<?> getAllFolders(HttpServletRequest httpServletRequest) {

     try{
         String userId = jwtService.extractUserIdFromRequest(httpServletRequest);

         if (userId == null){
             return ResponseEntity.status(401).body(Map.of("error", "Unauthorized!"));
         }

         List<FolderEntity> folders = repositroy.findByUserId(userId);
         return ResponseEntity.ok(folders);
     }catch (Exception e){
            return  ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
     }

    }

    public ResponseEntity<?> getRootFolders(HttpServletRequest httpServletRequest){
        try{
            String userId = jwtService.extractUserIdFromRequest(httpServletRequest);

            if (userId == null){
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized!"));
            }

            List<FolderEntity> folders = repositroy.findRootFolders(userId);

            return ResponseEntity.ok(folders);
        }
        catch (Exception e){
            return  ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

   public ResponseEntity<?> getFolders(String folderId, HttpServletRequest httpServletRequest){
        try {
            String userId = jwtService.extractUserIdFromRequest(httpServletRequest);

            if (userId == null){
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized!"));
            }

            List<FolderEntity> folders = repositroy.findByFolderId(folderId);


            return ResponseEntity.ok(folders);
        }
        catch (Exception e){
            return  ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
   }
    public ResponseEntity<?> getFolder(String folderId, HttpServletRequest httpServletRequest){
        try {
            String userId = jwtService.extractUserIdFromRequest(httpServletRequest);

            if (userId == null){
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized!"));
            }

            Optional<FolderEntity> folder = repositroy.findById(folderId);
            if (folder.isEmpty()){
                return ResponseEntity.status(404).body(Map.of("error", "Folder not found"));
            }


            return ResponseEntity.ok(folder.get());
        }
        catch (Exception e){
            return  ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
