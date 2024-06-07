package mu.mcb.property.evalution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mu.mcb.property.evalution.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Long> {
    
	Currency findByName(String name);
}
