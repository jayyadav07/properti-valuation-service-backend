package mu.mcb.property.evalution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mu.mcb.property.evalution.entity.FacilityType;

@Repository
public interface FacilityTypeRepository extends JpaRepository<FacilityType, Long> {
    FacilityType findByName(String name);
}
