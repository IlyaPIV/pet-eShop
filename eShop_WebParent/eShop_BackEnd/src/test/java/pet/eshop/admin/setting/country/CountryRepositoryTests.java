package pet.eshop.admin.setting.country;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import pet.eshop.admin.settings.country.CountryRepository;
import pet.eshop.common.entity.Country;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CountryRepositoryTests {

    @Autowired
    CountryRepository repository;

    @Test
    public void testFindAll(){
        List<Country> countries = repository.findAllByOrderByNameAsc();
        countries.forEach(System.out::println);

        assertThat(countries).isEmpty();
    }

    @Test
    public void testCreateCountry(){
        Country country = new Country("Belarus", "BLR");
        Country savedCountry = repository.save(country);

        assertThat(savedCountry.getId()).isNotNull();
    }

    @Test
    public void testUpdateCountry(){
        Country country = repository.findById(1).get();
        country.setCode("BY");
        Country savedCountry = repository.save(country);

        assertThat(savedCountry.getCode()).isNotEqualTo("BLR");

    }

    @Test
    public void testDeleteCountry(){
        repository.deleteById(1);
    }
}
