package com.project.pingme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Long userContactId;
    private String userFullName;
    private String messageText;
    private String messageTime;
    private String sender;
}
