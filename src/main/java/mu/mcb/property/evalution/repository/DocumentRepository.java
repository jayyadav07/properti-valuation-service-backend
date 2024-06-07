package mu.mcb.property.evalution.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mu.mcb.property.evalution.entity.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>{

	Optional<List<Document>> findByUserId(Long userId);

	
}
