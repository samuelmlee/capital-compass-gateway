package org.capitalcompass.gateway.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDTO {
    String username;
    String firstName;
    String lastName;
    String email;
    List<String> roles;
}
