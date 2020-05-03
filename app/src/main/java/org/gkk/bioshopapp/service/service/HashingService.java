package org.gkk.bioshopapp.service.service;

public interface HashingService {
    String hash(String str);

    boolean isPasswordMatch(String inputPassword, String oldPassword);
}
