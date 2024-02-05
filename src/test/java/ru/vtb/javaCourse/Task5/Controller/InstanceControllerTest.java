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
import ru.vtb.javaCourse.Task5.CorporateSettlementInstance.InstanceController;
import ru.vtb.javaCourse.Task5.CorporateSettlementInstance.InstanceResponse;
import ru.vtb.javaCourse.Task5.Service.InstanceServiceImpl;
import ru.vtb.javaCourse.Task5.Integration.IntegrationTestInstance;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(InstanceController.class)
@DisplayName("Тестирование контроллера InstanceController")
public class InstanceControllerTest {
    @MockBean
    InstanceServiceImpl instanceService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Проверка конечной точки /corporate-settlement-instance/create")
    void checkEndPoint() throws Exception {

        when(instanceService.performRequest(any()))
                .thenReturn(InstanceResponse.createAccountResponceBody("1", Set.of("2"), Set.of("3")));


        mockMvc.perform(post(IntegrationTestInstance.instanceUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(IntegrationTestInstance.getBaseInstanceRequest())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data.instanceId").value("1"))
        ;
    }


}
