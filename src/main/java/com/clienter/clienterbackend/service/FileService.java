package com.clienter.clienterbackend.service;

import com.clienter.clienterbackend.config.JwtService;
import com.clienter.clienterbackend.config.request.MoveFileRequest;
import com.clienter.clienterbackend.config.request.RenameFileRequest;
import com.clienter.clienterbackend.entity.FileEntity;
import com.clienter.clienterbackend.entity.FolderEntity;
import com.clienter.clienterbackend.repository.FileRepository;
import com.clienter.clienterbackend.repository.FolderRepositroy;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class FileService {


    @Autowired
    private FileRepository repositroy;

    @Autowired
    private FolderRepositroy folderRepositroy;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private JwtService jwtService;

    public ResponseEntity<Map<String, String>> createFile(MultipartFile file,String name, String folderId, HttpServletRequest httpServletRequest) throws IOException {
        try {
            String userId = jwtService.extractUserIdFromRequest(httpServletRequest);

            if (userId == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized!"));
            }

            Map uploadResult = cloudinaryService.upload(file);
            System.out.println(uploadResult);

            FileEntity fileData = new FileEntity();
            fileData.setName(name);
            fileData.setUrl(uploadResult.get("secure_url").toString());
            fileData.setSize(uploadResult.get("bytes").toString());
            fileData.setUserId(userId);
            fileData.setFolderId((folderId == null || folderId.isEmpty()) ? "root" : folderId);
            repositroy.save(fileData);
            return ResponseEntity.status(200).body(Map.of("message", "file created!"));

        }
        catch (Exception e){
            return  ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }


    public ResponseEntity<Map<String, Object>> renameFile(RenameFileRequest request, HttpServletRequest httpServletRequest) throws AccessDeniedException {

        try{

        String userId = jwtService.extractUserIdFromRequest(httpServletRequest);

        if (userId == null){
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized!"));
        }

        if (request.getFileId() == null || request.getName() == null){
            return ResponseEntity.status(400).body(Map.of("error", "Please provid valid data"));
        }

        Optional<FileEntity> file = repositroy.findById(request.getFileId());

        if (file.isEmpty()){
            return ResponseEntity.status(404).body(Map.of("error", "File not found"));
        }
        file.get().setName(request.getName());
        repositroy.save(file.get());
        return ResponseEntity.status(200).body(Map.of("message", "File renamed!"));
        }

        catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }

    }


    public  ResponseEntity<Map<String, Object>> deleteFile(String fileId, HttpServletRequest httpServletRequest) throws AccessDeniedException {
        try{
            String userId = jwtService.extractUserIdFromRequest(httpServletRequest);

            if (userId == null){
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized!"));
            }
            if (fileId == null){
                return ResponseEntity.status(400).body(Map.of("error", "Please provide valid data!"));
            }
            Optional<FileEntity> file = repositroy.findById(fileId);

            if (file.isEmpty()){
                return ResponseEntity.status(404).body(Map.of("error", "File not found"));
            }
            repositroy.deleteById(fileId);

            return ResponseEntity.status(200).body(Map.of("message", "file deleted!"));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    public  ResponseEntity<Map<String, Object>>  moveFile(MoveFileRequest request, HttpServletRequest httpServletRequest) throws AccessDeniedException{
try {
    String userId = jwtService.extractUserIdFromRequest(httpServletRequest);

    if (userId == null){
        return ResponseEntity.status(401).body(Map.of("error", "Unauthorized!"));
    }
    if (request.getFileId() == null || request.getFolderId() == null){
        return ResponseEntity.status(400).body(Map.of("error", "Please provide valid data!"));
    }
    Optional<FolderEntity> folder = folderRepositroy.findById(request.getFolderId());
    Optional<FileEntity> file = repositroy.findById(request.getFileId());

    if (file.isEmpty()){
        return ResponseEntity.status(404).body(Map.of("error", "Not found"));
    }

    file.get().setFolderId((request.getFolderId() == null || request.getFolderId().isEmpty() )? "root" : request.getFolderId());
    repositroy.save(file.get());
    return ResponseEntity.status(200).body(Map.of("message", "file moved"));
}
catch (Exception e){
    return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
}

    }
  

    public ResponseEntity<?> getFiles(HttpServletRequest httpServletRequest){
        try {
            String userId = jwtService.extractUserIdFromRequest(httpServletRequest);

            if (userId == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized!"));
            }

            List<FileEntity> files =  repositroy.findFilesOnRoot(userId);
            return ResponseEntity.ok(files);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }

    }

    public ResponseEntity<?> getFilesByFolder(String folderId, HttpServletRequest httpServletRequest) {
        try {
            String userId = jwtService.extractUserIdFromRequest(httpServletRequest);

            if (userId == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized!"));
            }

            List<FileEntity> files = repositroy.findFilesByFolderId(folderId, userId);
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
