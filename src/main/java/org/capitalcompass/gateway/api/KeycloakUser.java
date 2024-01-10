package org.capitalcompass.gateway.api;

import lombok.Data;

@Data
public class KeycloakUser {

    private Object createdTimestamp;
    private String username;
    private boolean enabled;
    private String email;
}
