package mu.mcb.property.evalution.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import mu.mcb.property.evalution.entity.RefreshToken;
import mu.mcb.property.evalution.exception.TokenRefreshException;
import mu.mcb.property.evalution.repository.RefreshTokenRepository;
import mu.mcb.property.evalution.repository.UserRepository;

class RefreshTokenServiceTest {

	@InjectMocks
	private RefreshTokenService refreshTokenService;

	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	@Mock
	private UserRepository userRepository;

	private final Long userId = 1L;
	private final String tokenString = UUID.randomUUID().toString();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindByToken() {
		String token = "exampleToken";
		when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(new RefreshToken()));

		Optional<RefreshToken> foundToken = refreshTokenService.findByToken(token);

		assertTrue(foundToken.isPresent());
		verify(refreshTokenRepository).findByToken(token);
	}

	@Test
	void testCreateRefreshToken() {
		when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(null));
		when(refreshTokenRepository.save(any())).thenReturn(new RefreshToken());

		RefreshToken createdToken = refreshTokenService.createRefreshToken(userId);

		assertNotNull(createdToken);
		verify(refreshTokenRepository).save(any());
	}

	@Test
	void testVerifyExpiration_NotExpired() {
		RefreshToken token = new RefreshToken();
		token.setExpiryDate(Instant.now().plusSeconds(3600));

		RefreshToken verifiedToken = refreshTokenService.verifyExpiration(token);

		assertNotNull(verifiedToken);
	}

	@Test
	void testVerifyExpiration_Expired() {
		RefreshToken token = new RefreshToken();
		token.setExpiryDate(Instant.now().minusSeconds(3600));

		assertThrows(TokenRefreshException.class, () -> refreshTokenService.verifyExpiration(token));
		verify(refreshTokenRepository).delete(token);
	}

	@Test
	void testDeleteByUserId() {
		when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(null));
		when(refreshTokenRepository.deleteByUser(any())).thenReturn(1);

		int deletedCount = refreshTokenService.deleteByUserId(userId);

		assertEquals(1, deletedCount);
		verify(refreshTokenRepository).deleteByUser(any());
	}
}
