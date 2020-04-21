package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.service.service.HashingService;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

@Service
public class HashingServiceImpl implements HashingService {
    @Override
    public String hash(String str) {
        return DigestUtils.sha256Hex(str);
    }
}
