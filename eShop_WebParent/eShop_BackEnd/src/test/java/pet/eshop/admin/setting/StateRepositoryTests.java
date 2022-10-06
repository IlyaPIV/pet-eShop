package pet.eshop.admin.setting;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import pet.eshop.admin.settings.CountryRepository;
import pet.eshop.admin.settings.StateRepository;
import pet.eshop.common.entity.Country;
import pet.eshop.common.entity.State;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class StateRepositoryTests {

    @Autowired
    StateRepository repository;
    @Autowired
    CountryRepository countryRepository;

    @Test
    public void testCreateState(){
        Country country = countryRepository.findById(1).get();
        String name = "Minsk";
        State saved = repository.save(new State(name, country));

        assertThat(saved.getId()).isNotNull();
    }
}
