package mu.mcb.property.evalution.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import mu.mcb.property.evalution.entity.RefreshToken;
import mu.mcb.property.evalution.entity.User;

/**
 * The Interface RefreshTokenRepository.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	
	Optional<RefreshToken> findByToken(String token);

	Optional<RefreshToken> findByUserId(Long userId);
	
	@Modifying
	int deleteByUser(User user);
}
