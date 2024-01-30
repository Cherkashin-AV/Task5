package ru.vtb.javaCourse.Task5.Integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.javaCourse.Task5.CorporateSettlementInstance.InstanceController;
import ru.vtb.javaCourse.Task5.CorporateSettlementInstance.InstanceRequest;
import ru.vtb.javaCourse.Task5.CorporateSettlementInstance.InstanceResponse;
import ru.vtb.javaCourse.Task5.Entity.Product;
import ru.vtb.javaCourse.Task5.Entity.ProductRegister;
import ru.vtb.javaCourse.Task5.Repository.ProductRegisterRepo;
import ru.vtb.javaCourse.Task5.Repository.ProductRepo;

import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Интеграционное тестирование ЭП")
public class IntegrationTestInstance {

    public static final String instanceUrl = "/corporate-settlement-instance/create";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    InstanceController instanceController;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    ProductRegisterRepo registerRepo;

    public static InstanceRequest getBaseInstanceRequest() {
        InstanceRequest instanceRequest = new InstanceRequest();
        instanceRequest.setInstanceId(1L);
        instanceRequest.setProductType("ДОГОВОР");
        instanceRequest.setProductCode("03.012.002");
        instanceRequest.setRegisterType("03.012.002_47533_ComSoLd");
        instanceRequest.setMdmCode("15");
        instanceRequest.setContractNumber("2024-01-10-000001");
        instanceRequest.setContractDate(LocalDate.now());
        instanceRequest.setPriority(2);
        instanceRequest.setInterestRatePenalty(12.25F);
        instanceRequest.setMinimalBalance(0F);
        instanceRequest.setThresholdAmount(0F);
        instanceRequest.setAccountingDetails("qwerty");
        instanceRequest.setRateType("0");
        instanceRequest.setTaxPercentageRate(13F);
        instanceRequest.setTechnicalOverdraftLimitAmount(1000F);
        instanceRequest.setContractId(112233);
        instanceRequest.setBranchCode("0022");
        instanceRequest.setIsoCurrencyCode("800");
        instanceRequest.setUrgencyCode("00");
        instanceRequest.setReferenceCode(1234);
        InstanceRequest.InstanceArrangement agreement =
                new InstanceRequest.InstanceArrangement();
        agreement.setGeneralAgreementId("123");
        agreement.setSupplementaryAgreementId("456");
        agreement.setArrangementType("НСО");
        agreement.setShedulerJobId(123456789);
        agreement.setNumber("НСО-123");
        agreement.setOpeningDate(LocalDate.of(2024, 01, 15));
        agreement.setClosingDate(LocalDate.of(2024, 01, 15));
        agreement.setCancelDate(null);
        agreement.setValidityDuration(365);
        agreement.setCancellationReason("");
        agreement.setStatus("OPEN");
        agreement.setInterestCalculationDate(null);
        agreement.setInterestRate(0F);
        agreement.setCoefficient(0F);
        agreement.setCoefficientAction("");
        agreement.setMinimumInterestRate(0F);
        agreement.setMinimumInterestRateCoefficient("0");
        agreement.setMinimumInterestRateCoefficientAction("");
        agreement.setMaximalnterestRate(0F);
        agreement.setMaximalnterestRateCoefficient(0F);
        instanceRequest.setArrangements(Set.of(agreement));
        return instanceRequest;
    }

    @Test
    @DisplayName("Проверка создания бинов")
    public void checkControllers() {
        Assertions.assertNotNull(instanceController);
        Assertions.assertNotNull(registerRepo);
        Assertions.assertNotNull(productRepo);
    }

    @Test
    @DisplayName("1. Проверка на обязательные реквизиты")
    public void requiredField() throws Exception {
        InstanceRequest instanceRequest = getBaseInstanceRequest();
        instanceRequest.setProductType(null);
        mockMvc.perform(post(instanceUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instanceRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("productType")));

    }

    @Test
    @DisplayName("2. Проверка ЭП на дубли")
    public void instanceDouble() throws Exception {
        InstanceRequest instanceRequest = getBaseInstanceRequest();
        instanceRequest.setInstanceId(null);
        //Создаем договор
        mockMvc.perform(post(instanceUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instanceRequest)))
                .andExpect(status().isOk());
        //Пытаемся повторно создать договор
        mockMvc.perform(post(instanceUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instanceRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(instanceRequest.getContractNumber())))
        ;

    }

    @Test
    @DisplayName("3. Проверка таблицы ДС на дубли")
    public void test() throws Exception {
        //Создаем договор
        InstanceRequest instanceRequest = getBaseInstanceRequest();
        instanceRequest.setInstanceId(null);
        ResultActions resultActions = mockMvc.perform(post(instanceUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(instanceRequest)));
        resultActions.andExpect(status().isOk());
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        InstanceResponse savedInstance = objectMapper.readValue(responseBody, InstanceResponse.class);

        //Создаем дополнительное соглашение
        instanceRequest.setInstanceId(Long.valueOf(savedInstance.getData().getInstanceId()));
        instanceRequest.setContractNumber("ddd");
        mockMvc.perform(post(instanceUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instanceRequest)))
                .andExpect(status().isOk());

        //Пытаемся создать дополнительное соглашение еще раз
        instanceRequest.setInstanceId(Long.valueOf(savedInstance.getData().getInstanceId()));
        instanceRequest.setContractNumber("ddd");
        mockMvc.perform(post(instanceUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instanceRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(instanceRequest.getArrangements().iterator().next().getNumber())));
    }

    @Test
    @DisplayName("4. Проверка по каталогу продуктов")
    public void registerType() throws Exception {
        InstanceRequest instanceRequest = getBaseInstanceRequest();
        instanceRequest.setInstanceId(null);
        instanceRequest.setProductCode("ProductCode");
        ResultActions resultActions = mockMvc.perform(post(instanceUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instanceRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("ProductCode")));
    }

    @Test
    @DisplayName("5,6,8. Сохранение продукта и продуктового регистра и добавление дополнительного соглашения")
    public void saveProduct() throws Exception {
        //1. Создаем договор
        InstanceRequest instanceRequest = getBaseInstanceRequest();
        instanceRequest.setInstanceId(null);
        ResultActions resultActions = mockMvc.perform(post(instanceUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instanceRequest)))
                .andExpect(status().isOk());

        //Проверяем ответ
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        InstanceResponse savedInstance = objectMapper.readValue(responseBody, InstanceResponse.class);
        Assertions.assertNotNull(savedInstance.getData().getInstanceId());
        Assertions.assertEquals(1, savedInstance.getData().getRegisterId().size());
        Assertions.assertEquals(0, savedInstance.getData().getSupplementaryAgreementId().size());
        Assertions.assertNotNull(savedInstance.getData().getInstanceId());
        Long productID = Long.valueOf(savedInstance.getData().getInstanceId());
        //Ищем договор в БД
        Product product = productRepo
                .findById(Long.valueOf(productID))
                .orElseThrow(()->new ValidationException("Не найден договор в БД"));
        //Проверяем, что нет дополнитьельных соглашений
        Assertions.assertEquals(0, product.getAgreements().size());
        //Ищем созданные ПР
        Set<ProductRegister> registers = registerRepo.findByProduct_id(product.getId());
        Assertions.assertTrue(registers.iterator().hasNext());

        //2. Создаем дополнительное соглашение
        instanceRequest.setInstanceId(Long.valueOf(savedInstance.getData().getInstanceId()));
        resultActions = mockMvc.perform(post(instanceUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instanceRequest)))
                .andExpect(status().isOk());

        //Проверяем ответ
        responseBody = resultActions.andReturn().getResponse().getContentAsString();
        savedInstance = objectMapper.readValue(responseBody, InstanceResponse.class);
        Assertions.assertNotNull(savedInstance.getData().getInstanceId());
        Assertions.assertEquals(1, savedInstance.getData().getRegisterId().size());
        Assertions.assertEquals(1, product.getAgreements().size());
        Assertions.assertNotNull(savedInstance.getData().getInstanceId());
        //Ищем договор в БД и проверяем наличие дополнительного соглашения
        product = productRepo
                .findById(Long.valueOf(productID))
                .orElseThrow(()->new ValidationException("Не найден договор в БД"));
        Assertions.assertEquals(1, product.getAgreements().size());
    }
}
