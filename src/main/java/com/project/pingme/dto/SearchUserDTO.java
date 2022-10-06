package com.project.pingme.dto;

import com.project.pingme.enums.UserSearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserDTO {

    private String searchInput;

    @Enumerated(EnumType.STRING)
    private UserSearchCriteria searchCriteria;
}
