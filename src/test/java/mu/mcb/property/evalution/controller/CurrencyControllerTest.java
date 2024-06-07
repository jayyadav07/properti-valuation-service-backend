package mu.mcb.property.evalution.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import mu.mcb.property.evalution.dto.CurrencyDto;
import mu.mcb.property.evalution.service.CurrencyService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyController currencyController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(currencyController).build();
    }

    @Test
    void testGetCurrencies() throws Exception {
        CurrencyDto currency1 = new CurrencyDto();  // Populate with sample data
        CurrencyDto currency2 = new CurrencyDto();  // Populate with sample data
        List<CurrencyDto> currencyList = Arrays.asList(currency1, currency2);

        when(currencyService.getCurrencies()).thenReturn(currencyList);

        mockMvc.perform(get("/api/v1/currency/currencies")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{},{}]"));  // Adjust the JSON as per the actual structure of CurrencyDto
    }
}
