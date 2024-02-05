package ru.vtb.javaCourse.Task5.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.vtb.javaCourse.Task5.CorporateSettlementAccount.AccountController;
import ru.vtb.javaCourse.Task5.CorporateSettlementAccount.AccountRequest;
import ru.vtb.javaCourse.Task5.CorporateSettlementAccount.AccountResponse;
import ru.vtb.javaCourse.Task5.Service.AccountServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AccountController.class)
@DisplayName("Тестирование контроллера AccountController")
public class AccountControllerTest {
    @MockBean
    AccountServiceImpl accountService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    private AccountRequest accountRequest = new AccountRequest(
            1L,
            "03.012.002_47533_ComSoLd",
            "Клиентский",
            "800",
            "0022",
            "00",
            "15");

    @Test
    @DisplayName("Проверка конечной точки /corporate-settlement-account/create")
    void checkEndPoint() throws Exception {

        when(accountService.createProductRegister(any())).thenReturn(AccountResponse.createAccountResponceBody("1"));

        mockMvc.perform(post("/corporate-settlement-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(jsonPath("$.data.accountId").value("1"))
        ;
    }

}
