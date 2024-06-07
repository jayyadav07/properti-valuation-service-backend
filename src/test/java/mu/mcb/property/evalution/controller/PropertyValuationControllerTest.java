package mu.mcb.property.evalution.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import mu.mcb.property.evalution.dto.PropertyValuationDto;
import mu.mcb.property.evalution.service.PropertyValuationService;

@WebMvcTest(PropertyValuationController.class)
public class PropertyValuationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyValuationService propertyValuationService;

    @InjectMocks
    private PropertyValuationController propertyValuationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateValuationApplication() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
        PropertyValuationDto dto = new PropertyValuationDto(); // Set properties if needed
        ObjectMapper mapper = new ObjectMapper();
        String appData = mapper.writeValueAsString(dto);

        mockMvc.perform(multipart("/api/v1/propertyval/evaluation-form")
                .file(file)
                .param("appData", appData))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(propertyValuationService, times(1)).createEvaluationApplication(any(PropertyValuationDto.class), any(MultipartFile.class));
    }

    @Test
    void testUpdateEvaluationApplication() throws Exception {
        PropertyValuationDto dto = new PropertyValuationDto(); // Set properties if needed
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/propertyval/evaluation-form-update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(propertyValuationService, times(1)).updateEvaluationApplication(any(PropertyValuationDto.class));
    }

    @Test
    void testFetchAllApplication() throws Exception {
        PropertyValuationDto dto1 = new PropertyValuationDto(); // Set properties if needed
        PropertyValuationDto dto2 = new PropertyValuationDto(); // Set properties if needed
        List<PropertyValuationDto> dtos = Arrays.asList(dto1, dto2);

        when(propertyValuationService.fetchApplication()).thenReturn(dtos);

        mockMvc.perform(get("/api/v1/propertyval/fetchapplications")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists());

        verify(propertyValuationService, times(1)).fetchApplication();
    }

    @Test
    void testFetchAByApplicationId() throws Exception {
        PropertyValuationDto dto = new PropertyValuationDto(); // Set properties if needed
        Long id = 1L;

        when(propertyValuationService.fetchApplicationById(id)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/propertyval/fetchapplication/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id)); // Adjust based on actual dto properties

        verify(propertyValuationService, times(1)).fetchApplicationById(id);
    }
}
