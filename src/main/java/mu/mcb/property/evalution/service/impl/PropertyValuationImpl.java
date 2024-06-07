package mu.mcb.property.evalution.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import mu.mcb.property.evalution.dto.BorrowerDto;
import mu.mcb.property.evalution.dto.CommentsDto;
import mu.mcb.property.evalution.dto.CurrencyDto;
import mu.mcb.property.evalution.dto.DocumentDto;
import mu.mcb.property.evalution.dto.DocumentTypeDto;
import mu.mcb.property.evalution.dto.FacilityDto;
import mu.mcb.property.evalution.dto.FacilityTypeDto;
import mu.mcb.property.evalution.dto.PropertyValuationDto;
import mu.mcb.property.evalution.dto.RoleDto;
import mu.mcb.property.evalution.dto.UserDto;
import mu.mcb.property.evalution.entity.Borrower;
import mu.mcb.property.evalution.entity.Comment;
import mu.mcb.property.evalution.entity.Currency;
import mu.mcb.property.evalution.entity.Document;
import mu.mcb.property.evalution.entity.DocumentType;
import mu.mcb.property.evalution.entity.Facility;
import mu.mcb.property.evalution.entity.FacilityType;
import mu.mcb.property.evalution.entity.PropertyValuation;
import mu.mcb.property.evalution.entity.User;
import mu.mcb.property.evalution.repository.BorrowerRepository;
import mu.mcb.property.evalution.repository.CommentRepository;
import mu.mcb.property.evalution.repository.CurrencyRepository;
import mu.mcb.property.evalution.repository.DocumentRepository;
import mu.mcb.property.evalution.repository.DocumentTypeRepository;
import mu.mcb.property.evalution.repository.FacilityTypeRepository;
import mu.mcb.property.evalution.repository.PropertyValuationRepository;
import mu.mcb.property.evalution.repository.UserRepository;
import mu.mcb.property.evalution.security.UserDetailsImpl;
import mu.mcb.property.evalution.service.PropertyValuationService;

@Service
public class PropertyValuationImpl implements PropertyValuationService {
    
	@Autowired
    private PropertyValuationRepository propertyValuationRepository;

    @Autowired
    private FacilityTypeRepository facilityTypeRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private CurrencyRepository currencyRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BorrowerRepository borrowerRepository;
    
    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Override
    public PropertyValuation createEvaluationApplication(PropertyValuationDto pvDto, MultipartFile file) {
        PropertyValuation propertyValuation = saveAndUpdateApplicationData(pvDto,file);  
        return propertyValuationRepository.save(propertyValuation);
    }
    private PropertyValuation saveAndUpdateApplicationData(PropertyValuationDto pvAppDto, MultipartFile file) {
        PropertyValuation propertyValuation = PropertyValuation.builder().build();
        propertyValuation.setTypeOfEvaluation(pvAppDto.getType());
        propertyValuation.setFosreferenceNumber(pvAppDto.getFosreferenceNumber()); 
        saveAndUpdateUserData(propertyValuation, pvAppDto);
        PropertyValuation savedPropertyValuation =saveAndUpdateFacilityData(propertyValuation, pvAppDto);
        saveAndUpdateBorrowersData(propertyValuation, pvAppDto,savedPropertyValuation);
        saveAndUpdateComments(propertyValuation, pvAppDto,savedPropertyValuation);
        saveAndUpdateDocuments(propertyValuation, pvAppDto, file,savedPropertyValuation);
        propertyValuation.setReferanceNo(referenaceNumber(savedPropertyValuation.getCreatedDate(),savedPropertyValuation.getId()));
        return propertyValuation;
    }

    private List<Comment> saveAndUpdateComments(PropertyValuation pvApplication, PropertyValuationDto pvAppDto,PropertyValuation savedPropertyValuation) {
        List<Comment> commentList = new ArrayList<>();
        pvAppDto.getCommentsDto().forEach(commentDto ->{
            Comment comment = new Comment();
            comment.setComments(commentDto.getComments());
            comment.setUser(savedPropertyValuation.getUser());
            comment.setFacility(savedPropertyValuation.getFacility());
            commentList.add(comment);
        });
        return commentRepository.saveAll(commentList);
    }

    private List<Document> saveAndUpdateDocuments(PropertyValuation pvApplication, PropertyValuationDto pvAppDto, MultipartFile file,PropertyValuation savedPropertyValuation) {
        List<Document> documentList = new ArrayList<>();
        pvAppDto.getDocumentsDto().forEach(documentDto -> {
            Document document = new Document();
            try {
            	document.setUser(savedPropertyValuation.getUser());
                document.setDocument(file.getBytes());
                document.setFacility(savedPropertyValuation.getFacility());
                if(Objects.nonNull(file)) {
                    document.setFileName(file.getOriginalFilename());
                    document.setFileSize(file.getSize());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            DocumentType documentType;
            if (Objects.nonNull(documentDto.getType().getName())){
                documentType = documentTypeRepository.findByName(documentDto.getType().getName());
                if (Objects.nonNull(documentType)){
                    document.setType(documentType);
                }
            }
            documentList.add(document);
        });
        return documentRepository.saveAll(documentList);
    }

    private List<Borrower> saveAndUpdateBorrowersData(PropertyValuation pvApplication, PropertyValuationDto pvAppDto,PropertyValuation savedPropertyValuation) {
        List<Borrower> borrowersList = new ArrayList<>();
        int count=0;
        for( BorrowerDto borrowerDto : pvAppDto.getBorrowersDto()){
        	count++;
            Borrower borrower = new Borrower();
            borrower.setName(borrowerDto.getName());
            borrower.setContactNumber(borrowerDto.getContactNumber());
            borrower.setEmail(borrowerDto.getEmail());
            borrower.setCustomerNumber(borrowerDto.getCustomerNumber());
            borrower.setAddress(borrowerDto.getAddress());
            borrower.setUser(savedPropertyValuation.getUser());
            borrower.setFacility(savedPropertyValuation.getFacility());
            borrower.setIsMainBorrower(count == 1 ? Boolean.TRUE : Boolean.FALSE );
            borrower.setPropertyValuation(savedPropertyValuation);
            borrowersList.add(borrower);
        }
        return borrowerRepository.saveAll(borrowersList);
    }

    private PropertyValuation saveAndUpdateFacilityData(PropertyValuation propertyValuation, PropertyValuationDto pvAppDto) {
        Facility facility = new Facility();
        facility.setCatagory(pvAppDto.getFacilityDto().getCatagory());
        facility.setTerm(pvAppDto.getFacilityDto().getTerm());
        facility.setAmount(pvAppDto.getFacilityDto().getAmount());
        facility.setPurpose(pvAppDto.getFacilityDto().getPurpose()); 
        populateFacilityTypeAndCurrency(pvAppDto, facility);
        facility.setUser(propertyValuation.getUser());
        propertyValuation.setFacility(facility);
        propertyValuation.setReferanceNo(null);
        return propertyValuationRepository.save(propertyValuation);
    }

    private void populateFacilityTypeAndCurrency(PropertyValuationDto pvAppDto, Facility facility) {
        FacilityType facilityType;
        Currency currency;  
        if (Objects.nonNull(pvAppDto.getFacilityDto().getType().getName())){
            facilityType = facilityTypeRepository.findByName(pvAppDto.getFacilityDto().getType().getName());
            if (Objects.nonNull(facilityType)){
                facility.setType(facilityType);
            }
        }
        if (Objects.nonNull(pvAppDto.getFacilityDto().getCcy().getName())){
            currency = currencyRepository.findByName(pvAppDto.getFacilityDto().getCcy().getName());
            if (Objects.nonNull(currency)){
                facility.setCcy(currency);
            }
        }   
    }

    private PropertyValuation saveAndUpdateUserData(PropertyValuation pvApplication, PropertyValuationDto pvAppDto) {
        User user = null;
        if (Objects.nonNull(pvAppDto.getCreatedBy()) && Objects.nonNull(pvAppDto.getCreatedBy().getUserid())){
            Optional<User> optionalUser;
            optionalUser = userRepository.findById(pvAppDto.getCreatedBy().getUserid());
            if(optionalUser.isPresent()){
                user = optionalUser.get();
                user.setContactNumber(pvAppDto.getCreatedBy().getContactNumber());
            } else {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                Optional<User> usr = userRepository.findById(userDetails.getId());
                user = usr.get();
            }
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Optional<User> usr = userRepository.findById(userDetails.getId());
            user = usr.get();
        }
        pvApplication.setUser(user);
        return pvApplication;
    }
    
    @Override
    @Transactional
    public List<PropertyValuationDto> fetchApplication() {
        List<PropertyValuationDto> pvApplicationDtoList = new ArrayList<>();
        try {
            List<PropertyValuation> pvApplicationList = propertyValuationRepository.findAll();
            if(!CollectionUtils.isEmpty(pvApplicationList)){
                pvApplicationList.forEach(pvApplication -> {
                    PropertyValuationDto pVApplicationDto = new PropertyValuationDto();
                    populatePvApplicationResponse(pVApplicationDto, pvApplication);
                    pvApplicationDtoList.add(pVApplicationDto);
                });
            }
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
        return pvApplicationDtoList;
    }

    @Override
    public PropertyValuationDto fetchApplicationById(Long id) {
        PropertyValuationDto pvApplicationDto = new PropertyValuationDto();
        try {
            Optional<PropertyValuation> pvApplication = propertyValuationRepository.findById(id);
            if (pvApplication.isPresent()){
                populatePvApplicationResponse(pvApplicationDto, pvApplication.get());
            }
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
        return pvApplicationDto;
    }

    private void populatePvApplicationResponse(PropertyValuationDto pVApplicationDto, PropertyValuation pvApplication) {
        pVApplicationDto.setId(pvApplication.getId());
        pVApplicationDto.setType(pvApplication.getTypeOfEvaluation());
        pVApplicationDto.setFosreferenceNumber(pvApplication.getFosreferenceNumber());
        pVApplicationDto.setReferanceNo(pvApplication.getReferanceNo());
        pVApplicationDto.setCreatedBy(populateUserDto(pvApplication));
        pVApplicationDto.setFacilityDto(populateFacilityDto(pvApplication));
        pVApplicationDto.setBorrowersDto(populateBorrowerDto(pvApplication));
        pVApplicationDto.setDocumentsDto(populateDocumentsDto(pvApplication));
        pVApplicationDto.setCommentsDto(populateCommentsDto(pvApplication));
        pVApplicationDto.setCreatedDate(pvApplication.getCreatedDate());
        pVApplicationDto.setModifiedDate(pvApplication.getModifiedDate());
        pVApplicationDto.setFosreferenceNumber(fosReferenaceNumber(pvApplication.getFosreferenceNumber()));
        poupulateMainBorrowerName(pVApplicationDto);
       }

		private void poupulateMainBorrowerName(PropertyValuationDto pVApplicationDto) {
			if (Objects.nonNull(pVApplicationDto.getBorrowersDto())) {
				Optional<BorrowerDto> optionBr = pVApplicationDto.getBorrowersDto().stream()
						.filter(borrower -> Boolean.TRUE.equals(borrower.getIsMainBorrower())).findFirst();
				if (optionBr.isPresent()) {
					pVApplicationDto.setMainBorrowerName(optionBr.get().getName());
				}
			}
		}

    private List<CommentsDto> populateCommentsDto(PropertyValuation pvApplication) {
        List<CommentsDto> commentsDtoList;
        User user = pvApplication.getUser();
        Optional<List<Comment>> commentList=commentRepository.findByUserId(user.getId());    
        if (commentList.isPresent() && !CollectionUtils.isEmpty(commentList.get())){
            commentsDtoList = new ArrayList<>();
            commentList.get().forEach(comment -> {
                CommentsDto commentsDto = new CommentsDto();
                BeanUtils.copyProperties(comment, commentsDto);
                commentsDto.setUserName(comment.getUser().getName());
                commentsDtoList.add(commentsDto);
            });
        } else {
            commentsDtoList = null;
        }
        return commentsDtoList;
    }

    private List<DocumentDto> populateDocumentsDto(PropertyValuation pvApplication) {
        List<DocumentDto> documentDtoList;
        User user = pvApplication.getUser();
        Optional<List<Document>> documentList=documentRepository.findByUserId(user.getId());    
        if (!CollectionUtils.isEmpty(documentList.get())){
            documentDtoList = new ArrayList<>();
            documentList.get().forEach(document -> {
                DocumentDto documentDto = new DocumentDto();
                DocumentTypeDto documentTypeDto = new DocumentTypeDto();
                BeanUtils.copyProperties(document,documentDto);
                documentDto.setDocument(document.getDocument());
                BeanUtils.copyProperties(document.getType(), documentTypeDto);
                documentDto.setType(documentTypeDto);
                documentDto.setDocumentType(documentTypeDto.getName());
                documentDto.setUpdatedBy(document.getUser().getName());
                documentDtoList.add(documentDto);
            });
        } else {
            documentDtoList = null;
        }
        return documentDtoList;
    }

    private List<BorrowerDto> populateBorrowerDto(PropertyValuation pvApplication) {
        List<BorrowerDto> borrowerDtoList;
        User user = pvApplication.getUser();
        Optional<List<Borrower>> borrowerList=borrowerRepository.findByUserIdAndPropertyValuationId(user.getId(),pvApplication.getId()); 
        if (borrowerList.isPresent() && !CollectionUtils.isEmpty(borrowerList.get())){
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

    private FacilityDto populateFacilityDto(PropertyValuation pvApplication) {
        FacilityDto facilityDto = null;
        FacilityTypeDto facilityTypeDto = null;
        CurrencyDto currencyDto = null;
        if (Objects.nonNull(pvApplication.getFacility())){
            facilityDto = new FacilityDto();
            facilityTypeDto = new FacilityTypeDto();
            currencyDto = new CurrencyDto();
            BeanUtils.copyProperties(pvApplication.getFacility(), facilityDto);
            BeanUtils.copyProperties(pvApplication.getFacility().getType(), facilityTypeDto);
            BeanUtils.copyProperties(pvApplication.getFacility().getCcy(), currencyDto);
            facilityDto.setType(facilityTypeDto);
            facilityDto.setCcy(currencyDto);
        }
        return facilityDto;
    }

    private static UserDto populateUserDto(PropertyValuation pvApplication) {
        UserDto userDto = null;
        User user = pvApplication.getUser();
        if (Objects.nonNull(user)){
            userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            Set<RoleDto> roles =  new HashSet<>();
            if(!CollectionUtils.isEmpty(user.getRoles())){
                user.getRoles().forEach(role -> {
                    RoleDto roleDto = new RoleDto();
                    roleDto.setId(role.getRoleId());
                    roleDto.setName(role.getName().toString());
                    roles.add(roleDto);
                });
            }
            userDto.setRoles(roles);
        }
        return userDto;
    }

    @Override
    public String updateEvaluationApplication(PropertyValuationDto pvDto) {
       // PVApplication pvApplication = populateApplicationData(pvAppDto);
       // propertyValuationRepository.save(pvApplication);
        return "success";
    }
    private String referenaceNumber(LocalDateTime savedDate, Long sequanceId) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = savedDate.format(dateFormatter);
        String formattedSequenceNumber = String.format("%04d", sequanceId);
        return "PV" + formattedDate + formattedSequenceNumber;
    }
    private String fosReferenaceNumber(String sequanceId) {
    	String sequanceNo[]=Objects.nonNull(sequanceId)? sequanceId.split("/") : new String[0];
        String formattedSequenceNumber = String.format("%04d", Integer.valueOf(sequanceNo[2]));      
        return String.format("%s/%s/%s",sequanceNo[0],sequanceNo[1], formattedSequenceNumber);
    }
}
