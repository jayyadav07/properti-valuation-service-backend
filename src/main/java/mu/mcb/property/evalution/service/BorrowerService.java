package mu.mcb.property.evalution.service;

import java.util.List;

import mu.mcb.property.evalution.dto.BorrowerDto;

public interface BorrowerService {

	List<BorrowerDto> getBorrowerData(Long userId);

}
