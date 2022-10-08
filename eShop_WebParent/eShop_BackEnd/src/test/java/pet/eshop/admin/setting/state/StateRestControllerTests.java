package pet.eshop.admin.setting.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pet.eshop.admin.settings.state.StateRepository;
import pet.eshop.common.entity.Country;
import pet.eshop.common.entity.State;
import pet.eshop.common.entity.StateDTO;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    StateRepository repository;

    @Test
    @WithMockUser(username = "petermoloch@mail.ru", password = "7940588")
    public void testCreateState() throws Exception {
        String url = "/states/save";
        String stateName = "Menskaya obl.";
        Country stateCountry = new Country(1);
        State state = new State(stateName, stateCountry);

        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
                .content(objectMapper.writeValueAsString(state))
                    .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer sateId = Integer.parseInt(response);
        Optional<State> foundedById = repository.findById(sateId);
        State savedState = foundedById.get();

        assertThat(savedState.getName()).isEqualTo(stateName);
    }

    @Test
    @WithMockUser(username = "petermoloch@mail.ru", password = "7940588")
    public void testListStates() throws Exception {
        int countryId = 1;
        String url = "/states/list/" + countryId;

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        StateDTO[] statesList = objectMapper.readValue(jsonResponse, StateDTO[].class);

        assertThat(statesList).hasSizeGreaterThan(0);
    }

    @Test
    @WithMockUser(username = "petermoloch@mail.ru", password = "7940588")
    public void testUpdateState() throws Exception {
        String url = "/states/save";

        Integer stateId = 4;
        String stateName = "Minskaya obl.";
        State state = repository.findById(stateId).get();
        state.setName(stateName);

        mockMvc.perform(post(url).contentType("application/json")
                    .content(objectMapper.writeValueAsString(state))
                    .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(stateId)));

        Optional<State> foundedById = repository.findById(stateId);
        assertThat(foundedById.isPresent());
        State savedState = foundedById.get();

        assertThat(savedState.getName()).isEqualTo(stateName);
    }

    @Test
    @WithMockUser(username = "petermoloch@mail.ru", password = "7940588")
    public void testDeleteState() throws Exception{
        Integer stateId = 3;
        String url = "/states/delete/" + stateId;

        mockMvc.perform(get(url))
                .andExpect(status().isOk());

        Optional<State> foundedById = repository.findById(stateId);
        assertThat(foundedById).isNotPresent();
    }
}
