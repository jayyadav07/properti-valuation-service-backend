package mu.mcb.property.evalution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mu.mcb.property.evalution.entity.PropertyValuation;

@Repository
public interface PropertyValuationRepository extends JpaRepository<PropertyValuation, Long> {
}
