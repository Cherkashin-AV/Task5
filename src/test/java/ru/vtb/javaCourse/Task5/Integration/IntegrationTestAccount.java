package ru.vtb.javaCourse.Task5.Integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.javaCourse.Task5.CorporateSettlementAccount.AccountController;
import ru.vtb.javaCourse.Task5.CorporateSettlementAccount.AccountRequest;
import ru.vtb.javaCourse.Task5.CorporateSettlementAccount.AccountResponse;
import ru.vtb.javaCourse.Task5.CorporateSettlementAccount.AccountService;
import ru.vtb.javaCourse.Task5.CorporateSettlementInstance.InstanceController;
import ru.vtb.javaCourse.Task5.CorporateSettlementInstance.InstanceRequest;
import ru.vtb.javaCourse.Task5.CorporateSettlementInstance.InstanceResponse;
import ru.vtb.javaCourse.Task5.Entity.Product;
import ru.vtb.javaCourse.Task5.Entity.ProductRegister;
import ru.vtb.javaCourse.Task5.Repository.ProductRegisterRepo;
import ru.vtb.javaCourse.Task5.Repository.ProductRepo;

import javax.sql.DataSource;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.schema=\"Task5\"",
        "spring.jpa.properties.hibernate.default_schema=\"Task5\""
})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, value = "/BeforeAccountTest.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS, value = "/AfterAccountTest.sql")
@DisplayName("Интеграционное тестирование ПР")
public class IntegrationTestAccount {
    private final String createRegistrUrl = "/corporate-settlement-account/create";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    AccountController accountController;
    @Autowired
    ProductRegisterRepo registerRepo;
    @Autowired
    ProductRepo productRepo;

    private AccountRequest getBaseAccountRequest() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setInstanceId(1L);
        accountRequest.setRegistryTypeCode("03.012.002_4753 3_ComSoLd");
        accountRequest.setAccountType("Клиентский");
        accountRequest.setCurrencyCode("800");
        accountRequest.setBranchCode("0022");
        accountRequest.setPriorityCode("00");
        accountRequest.setMdmCode("15");
        accountRequest.setClientCode("РЖД");
        accountRequest.setTrainRegion("РЖД");
        accountRequest.setCounter("РЖД");
        accountRequest.setSalesCode("123");
        return accountRequest;
    }

    @Test
    @DisplayName("1. Проверка на обязательные реквизиты")
    public void requiredField() throws Exception {
        AccountRequest accountRequest = getBaseAccountRequest();
        accountRequest.setInstanceId(null);
        mockMvc.perform(post(createRegistrUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("instanceId")));
    }

    @Test
    @DisplayName("2. Проверка таблицы ЭП на дубли")
    public void accountDouble() throws Exception {
        AccountRequest accountRequest = getBaseAccountRequest();
        Product product = productRepo.findByNumber("number").orElseThrow(()->new ValidateException("Не найден тестовый договор (заполняется скриптом)"));
        accountRequest.setInstanceId(product.getId());
        accountRequest.setRegistryTypeCode("type");
        mockMvc.perform(post(createRegistrUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("type")))
                .andExpect(content().string(containsString(product.getId().toString())));

    }

    @Test
    @DisplayName("3. Проверка создаваемого ПР в каталоге продуктов")
    public void accountCatalog() throws Exception {
        AccountRequest accountRequest = getBaseAccountRequest();
        Product product = productRepo.findByNumber("number").orElseThrow(()->new ValidateException("Не найден тестовый договор (заполняется скриптом)"));
        accountRequest.setInstanceId(product.getId());
        accountRequest.setRegistryTypeCode("type1");
        mockMvc.perform(post(createRegistrUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("type1")))
                .andExpect(content().string(containsString("tpp_ref_product_register_type")));

    }

    @Test
    @DisplayName("4. Создание ПР")
    public void createAccount() throws Exception {
        AccountRequest accountRequest = getBaseAccountRequest();
        Product product = productRepo.findByNumber("number").orElseThrow(()->new ValidateException("Не найден тестовый договор (заполняется скриптом)"));
        //Удаляем существующий регистр
        ProductRegister register = registerRepo.findByProduct_idAndType_value(product.getId(), "type").orElseThrow(()->new ValidateException(""));
        if(register!=null){
            registerRepo.deleteById(register.getId());
        }
        //Добавляем регистр
        accountRequest.setInstanceId(product.getId());
        accountRequest.setRegistryTypeCode("type");
        ResultActions resultActions = mockMvc.perform(post(createRegistrUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isOk());
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        AccountResponse savedAccount = objectMapper.readValue(responseBody, AccountResponse.class);
        Assertions.assertNotNull(savedAccount.getData().getAccountId());
    }

}
