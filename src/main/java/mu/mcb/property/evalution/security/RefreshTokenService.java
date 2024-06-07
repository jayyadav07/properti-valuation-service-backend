package mu.mcb.property.evalution.security;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mu.mcb.property.evalution.entity.RefreshToken;
import mu.mcb.property.evalution.exception.TokenRefreshException;
import mu.mcb.property.evalution.repository.RefreshTokenRepository;
import mu.mcb.property.evalution.repository.UserRepository;

/**
 * The Class RefreshTokenService.
 */
@Service
public class RefreshTokenService {

	/** The refresh token duration in ms. */
	@Value("${app.refreshTokenTime}")
	private Long refreshTokenDurationMs;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private UserRepository userRepository;

	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	public RefreshToken createRefreshToken(Long userId) {
		RefreshToken refreshToken = new RefreshToken();
		Optional<RefreshToken> refreshTokenoption = refreshTokenRepository.findByUserId(userId);
		if (refreshTokenoption.isPresent()) {
			refreshToken = refreshTokenoption.get();
			refreshToken.setId(refreshTokenoption.get().getId());
			refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
			refreshToken.setToken(UUID.randomUUID().toString());
			refreshToken = refreshTokenRepository.save(refreshToken);
		} else {
			refreshToken.setUser(userRepository.findById(userId).get());
			refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
			refreshToken.setToken(UUID.randomUUID().toString());
			refreshToken = refreshTokenRepository.save(refreshToken);
		}

		return refreshToken;
	}

	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			throw new TokenRefreshException(token.getToken(),
					"Refresh token was expired. Please make a new signin request");
		}
		return token;
	}

	@Transactional
	public int deleteByUserId(Long userId) {
		return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
	}
}
