package com.ape.business.concretes;

import com.ape.business.abstracts.ConfirmationTokenService;
import com.ape.dao.ConfirmationTokenDao;
import com.ape.entity.ConfirmationTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ConfirmationTokenManager implements ConfirmationTokenService {

    private final ConfirmationTokenDao confirmationTokenDao;
    @Override
    @Transactional
    public void saveConfirmationToken(ConfirmationTokenEntity token) {
        confirmationTokenDao.save(token);
    }

    @Override
    public Optional<ConfirmationTokenEntity> getToken(String token) {
        return confirmationTokenDao.findByToken(token);
    }

    @Override
    public void setConfirmedAt(String token) {
        confirmationTokenDao.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
