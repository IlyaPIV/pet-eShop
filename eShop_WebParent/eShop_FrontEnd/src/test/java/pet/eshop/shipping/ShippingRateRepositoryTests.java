package pet.eshop.shipping;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pet.eshop.common.entity.Country;
import pet.eshop.common.entity.ShippingRate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShippingRateRepositoryTests {

    @Autowired private ShippingRateRepository repository;

    @Test
    public void testFindByCountryAndState(){
        Country usa = new Country(234);
        String state = "New York";

        ShippingRate rate = repository.findByCountryAndState(usa, state);

        assertThat(rate).isNotNull();
        System.out.println(rate);
    }

}
