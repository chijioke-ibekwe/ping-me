package com.project.pingme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO {
    private Long userContactId;
    private Long contactId;
    private String firstName;
    private String lastName;
    private String displayPictureUrl;
}
