package com.example.filemanager.dto.requests;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class InUseWith {

    private String serviceName;
    private LocalDateTime autoExpire;
}
