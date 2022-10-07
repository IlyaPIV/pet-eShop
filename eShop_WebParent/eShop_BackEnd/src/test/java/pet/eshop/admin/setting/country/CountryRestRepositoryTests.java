package pet.eshop.admin.setting.country;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pet.eshop.admin.settings.country.CountryRepository;
import pet.eshop.common.entity.Country;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestRepositoryTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CountryRepository repository;

    @Test
    @WithMockUser(username = "petermoloch@mail.ru", password = "7940588")
    public void testListCountries() throws Exception {
        String url = "/countries/list";

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);

        assertThat(countries).hasSizeGreaterThan(0);
    }

    @Test
    @WithMockUser(username = "petermoloch@mail.ru", password = "7940588")
    public void testCreateCountry() throws Exception {
        String url = "/countries/save";
        String countryName = "Germany";
        String countryCode = "DE";
        Country country = new Country(countryName, countryCode);

        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
                .content(objectMapper.writeValueAsString(country))
                        .with(csrf()))                                                  //spring security token
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer countryId = Integer.parseInt(response);
        Optional<Country> foundedById = repository.findById(countryId);
        assertThat(foundedById.isPresent());
        Country savedCountry = foundedById.get();

        assertThat(savedCountry.getName()).isEqualTo(countryName);
    }

    @Test
    @WithMockUser(username = "petermoloch@mail.ru", password = "7940588")
    public void testUpdateCountry() throws Exception {
        String url = "/countries/save";

        Integer countryId = 2;
        String countryName = "Canada";
        String countryCode = "CA";
        Country country = new Country(countryId, countryName, countryCode);

        mockMvc.perform(post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(country))
                        .with(csrf()))                                                  //spring security token
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(countryId)));


        Optional<Country> foundedById = repository.findById(countryId);
        assertThat(foundedById.isPresent());
        Country savedCountry = foundedById.get();

        assertThat(savedCountry.getCode()).isEqualTo(countryCode);
    }

    @Test
    @WithMockUser(username = "petermoloch@mail.ru", password = "7940588")
    public void testDeleteCountry() throws Exception {

        Integer countryId = 3;

        String url = "/countries/delete/" + countryId;

        mockMvc.perform(get(url))
                .andExpect(status().isOk());

        Optional<Country> foundedById = repository.findById(countryId);
        assertThat(foundedById).isNotPresent();

    }
}
