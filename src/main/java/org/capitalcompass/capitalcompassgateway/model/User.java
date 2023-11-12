package org.capitalcompass.capitalcompassgateway.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class User {
    String username;
    String firstName;
    String lastName;
    String email;
    List<String> roles;
}
