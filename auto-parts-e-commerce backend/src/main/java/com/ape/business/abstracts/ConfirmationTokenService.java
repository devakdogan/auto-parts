package com.ape.business.abstracts;

import com.ape.entity.ConfirmationTokenEntity;

import java.util.Optional;

public interface ConfirmationTokenService {
    void saveConfirmationToken(ConfirmationTokenEntity token);
    Optional<ConfirmationTokenEntity> getToken(String token);
    void setConfirmedAt(String token);
}
