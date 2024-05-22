package com.clienter.clienterbackend.controller;

import com.clienter.clienterbackend.config.request.MoveFileRequest;
import com.clienter.clienterbackend.config.request.RenameFileRequest;
import com.clienter.clienterbackend.entity.FileEntity;
import com.clienter.clienterbackend.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService service;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam(value = "folderId", required = false) String folderId,
            HttpServletRequest httpServletRequest
    ) throws IOException {
        System.out.println(file );
            return service.createFile(file, name, folderId, httpServletRequest);
    }

    @PutMapping("/rename")
    public ResponseEntity<Map<String, Object>> renameFile(@RequestBody RenameFileRequest request, HttpServletRequest httpServletRequest) throws AccessDeniedException {
            return  service.renameFile(request, httpServletRequest);
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable String fileId, HttpServletRequest httpServletRequest) throws AccessDeniedException {
        return  service.deleteFile(fileId, httpServletRequest);
    }

    @GetMapping("/getHomeFiles")
    public ResponseEntity<?> getHomeFiles(HttpServletRequest httpServletRequest) {
        return service.getFiles(httpServletRequest);
    }

    @GetMapping("/getFiles/{folderId}")
    public ResponseEntity<?> getFileByFolderID(@PathVariable String folderId,  HttpServletRequest httpServletRequest) {
        System.out.println("HELLO " + folderId);
        return service.getFilesByFolder(folderId, httpServletRequest);
    }


    @PutMapping("/moveFile")
    public ResponseEntity<?> moveFile(@RequestBody MoveFileRequest request, HttpServletRequest httpServletRequest) throws AccessDeniedException {
        return  service.moveFile(request, httpServletRequest);
    }
}
