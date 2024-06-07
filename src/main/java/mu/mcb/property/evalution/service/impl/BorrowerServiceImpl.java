package mu.mcb.property.evalution.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import mu.mcb.property.evalution.dto.BorrowerDto;
import mu.mcb.property.evalution.entity.Borrower;
import mu.mcb.property.evalution.entity.User;
import mu.mcb.property.evalution.repository.BorrowerRepository;
import mu.mcb.property.evalution.repository.UserRepository;
import mu.mcb.property.evalution.service.BorrowerService;

@Service
public class BorrowerServiceImpl implements BorrowerService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BorrowerRepository borrowerRepository;
	
	@Override
	public List<BorrowerDto> getBorrowerData(Long userId) {
		Optional<User> optuser = userRepository.findById(userId);
		if (optuser.isPresent()) {
			List<BorrowerDto> borrowerDtoList;
			Optional<List<Borrower>> borrowerList = borrowerRepository.findByUserId(userId);
			if (borrowerList.isPresent() && !CollectionUtils.isEmpty(borrowerList.get())) {
				borrowerDtoList = new ArrayList<>();
				borrowerList.get().forEach(borrower -> {
					BorrowerDto borrowerDto = new BorrowerDto();
					BeanUtils.copyProperties(borrower, borrowerDto);
					borrowerDtoList.add(borrowerDto);
				});
			} else {
				borrowerDtoList = null;
			}

			return borrowerDtoList;
		}
		return null;
	}

	
}
