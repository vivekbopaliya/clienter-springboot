package com.clienter.clienterbackend.controller;


import com.clienter.clienterbackend.config.request.CreateFolderRequest;
import com.clienter.clienterbackend.config.request.RenameFolderRequest;
import com.clienter.clienterbackend.entity.FileEntity;
import com.clienter.clienterbackend.entity.FolderEntity;
import com.clienter.clienterbackend.service.FolderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folder")
public class FolderController {

    @Autowired
    private final FolderService service;


    @PostMapping("/create")
    public ResponseEntity<?> createFolder(@RequestBody CreateFolderRequest request, HttpServletRequest httpServletRequest) {
        return service.createFolder(request, httpServletRequest);
    }

    @PutMapping("/rename")
    public ResponseEntity<?> renameFolder(@RequestBody RenameFolderRequest request, HttpServletRequest httpServletRequest) throws AccessDeniedException {
        return service.renameFolder(request, httpServletRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFolder(@PathVariable String id, HttpServletRequest httpServletRequest) {
        return service.deleteFolder(id, httpServletRequest);
    }


    @GetMapping("/getAllFolders")
    public ResponseEntity<?> getAllFolders(HttpServletRequest httpServletRequest) {
        return service.getAllFolders(httpServletRequest);
    }

    @GetMapping("/getHomepageFolders")
    public ResponseEntity<?> getRootFolders(HttpServletRequest httpServletRequest) {
        return service.getRootFolders(httpServletRequest);
    }

    @GetMapping("/getFolders/{folderId}")
    public ResponseEntity<?> getFolders(@PathVariable String folderId, HttpServletRequest httpServletRequest) {
        return service.getFolders(folderId, httpServletRequest);
    }

    @GetMapping("/getFolder/{folderId}")
    public ResponseEntity<?> getFolder(@PathVariable String folderId, HttpServletRequest httpServletRequest) {
        return service.getFolder(folderId, httpServletRequest);
    }



}
