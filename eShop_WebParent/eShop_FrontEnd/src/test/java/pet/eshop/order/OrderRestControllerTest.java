package pet.eshop.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderRestControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @WithUserDetails("petermoloch@mail.ru")
    public void testSendOrderReturnRequestFailed() throws Exception {
        String requestUrl = "/orders/return";
        Integer orderId = 123;

        OrderReturnRequest returnRequest = new OrderReturnRequest(orderId, "", "");

        mockMvc.perform(post(requestUrl)
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(returnRequest))
                ).andExpect(status().isNotFound())
                .andDo(print());
    }
}