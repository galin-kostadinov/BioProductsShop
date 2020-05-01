package org.gkk.bioshopapp.service.service;

import java.util.List;

public interface AuthenticatedUserService {
    String getUsername();

    List<String> getRoles();
}
