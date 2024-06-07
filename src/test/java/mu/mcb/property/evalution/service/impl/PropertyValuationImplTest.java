package mu.mcb.property.evalution.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import mu.mcb.property.evalution.dto.PropertyValuationDto;
import mu.mcb.property.evalution.entity.PropertyValuation;
import mu.mcb.property.evalution.repository.BorrowerRepository;
import mu.mcb.property.evalution.repository.CommentRepository;
import mu.mcb.property.evalution.repository.CurrencyRepository;
import mu.mcb.property.evalution.repository.DocumentRepository;
import mu.mcb.property.evalution.repository.DocumentTypeRepository;
import mu.mcb.property.evalution.repository.FacilityTypeRepository;
import mu.mcb.property.evalution.repository.PropertyValuationRepository;
import mu.mcb.property.evalution.repository.UserRepository;

public class PropertyValuationImplTest {

	@InjectMocks
	private PropertyValuationImpl propertyValuationService;

	@Mock
	private PropertyValuationRepository propertyValuationRepository;

	@Mock
	private FacilityTypeRepository facilityTypeRepository;

	@Mock
	private DocumentTypeRepository documentTypeRepository;

	@Mock
	private CurrencyRepository currencyRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private BorrowerRepository borrowerRepository;

	@Mock
	private DocumentRepository documentRepository;

	@Mock
	private CommentRepository commentRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateEvaluationApplication() throws IOException {
		PropertyValuationDto dto = new PropertyValuationDto();
		MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());

		when(propertyValuationRepository.save(any(PropertyValuation.class))).thenAnswer(i -> i.getArguments()[0]);

		Object result = propertyValuationService.createEvaluationApplication(dto, file);

		assertEquals("success", result);
		verify(propertyValuationRepository, times(1)).save(any(PropertyValuation.class));
	}

	@Test
	void testUpdateEvaluationApplication() {
		PropertyValuationDto dto = new PropertyValuationDto();

		String result = propertyValuationService.updateEvaluationApplication(dto);

		assertEquals("success", result);
		// Verify other interactions as needed
	}

	@Test
	void testFetchApplication() {
		PropertyValuation propertyValuation1 = new PropertyValuation();
		PropertyValuation propertyValuation2 = new PropertyValuation();
		List<PropertyValuation> pvList = Arrays.asList(propertyValuation1, propertyValuation2);

		when(propertyValuationRepository.findAll()).thenReturn(pvList);

		List<PropertyValuationDto> result = propertyValuationService.fetchApplication();

		assertEquals(2, result.size());
		verify(propertyValuationRepository, times(1)).findAll();
	}

	@Test
	void testFetchApplicationById() {
		Long id = 1L;
		PropertyValuation propertyValuation = new PropertyValuation();
		propertyValuation.setId(id);

		when(propertyValuationRepository.findById(id)).thenReturn(Optional.of(propertyValuation));

		PropertyValuationDto result = propertyValuationService.fetchApplicationById(id);

		assertNotNull(result);
		assertEquals(id, result.getId());
		verify(propertyValuationRepository, times(1)).findById(id);
	}

}