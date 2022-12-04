package com.project.pingme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {

    private String username;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String profilePicture;
}
