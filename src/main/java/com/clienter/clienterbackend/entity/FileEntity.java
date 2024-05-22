package com.clienter.clienterbackend.entity;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Files")
public class FileEntity {

    @Id
    private  String id;

    private String name;

    @Nullable
    private String size;

    @Nullable
    private String url;

    private String userId;

    private String folderId;

}

