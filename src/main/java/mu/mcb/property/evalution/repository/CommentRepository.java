package mu.mcb.property.evalution.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mu.mcb.property.evalution.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

	Optional<List<Comment>> findByUserId(Long userId);

}
