package rent.your.room.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rent.your.room.exception.TokenRefreshException;
import rent.your.room.model.RefreshToken;
import rent.your.room.repository.RefreshTokenRepository;
import rent.your.room.repository.UserRepository;
import rent.your.room.model.User;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${rent.your.room.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserId(userId);

        if (existingToken.isPresent()) {
            RefreshToken refreshToken = existingToken.get();
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setToken(UUID.randomUUID().toString());
            return refreshTokenRepository.save(refreshToken);
        } else {
            RefreshToken refreshToken = new RefreshToken();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));
            refreshToken.setUser(user);
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setToken(UUID.randomUUID().toString());
            return refreshTokenRepository.save(refreshToken);
        }
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenRefreshException(refreshToken.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return refreshToken;
    }

    

        @Transactional

        public int deleteByUserId(Long userId) {

            return userRepository.findById(userId).map(refreshTokenRepository::deleteByUser).orElse(0);

        }

    }

    