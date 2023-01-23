package com.project.pingme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConnectRequestDTO {

    private Long requestId;
    private Long recipientId;
    private String recipientName;
    private String senderName;
    private String activity;
    private String senderDisplayPictureUrl;
}
