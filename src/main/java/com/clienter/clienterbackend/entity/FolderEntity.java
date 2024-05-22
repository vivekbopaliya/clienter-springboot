package com.clienter.clienterbackend.entity;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Folders")
public class FolderEntity {

    @Id
    private String id;

    private String name;

    @Nullable
    private String folderId;

    private String userId;

    public FolderEntity(String FolderName, String folderId, String userId) {
        this.name = FolderName;
        this.folderId = folderId;
        this.userId = userId;
    }
}
