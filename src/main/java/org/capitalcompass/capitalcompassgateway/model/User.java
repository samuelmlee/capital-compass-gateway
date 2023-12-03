package org.capitalcompass.capitalcompassgateway.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class User {
    String username;
    String firstName;
    String lastName;
    String email;
    List<String> roles;
}
