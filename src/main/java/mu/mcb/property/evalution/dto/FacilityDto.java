package mu.mcb.property.evalution.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDto {
    private Long id;
    private FacilityTypeDto type;
    private String catagory;
    private String purpose;
    private Integer term;
    private CurrencyDto ccy;
    private BigDecimal amount;
	
}
