package com.clienter.clienterbackend.config.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RenameFileRequest {
    private  String name;
    private  String fileId;

}
