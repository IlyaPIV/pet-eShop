package pet.eshop.admin.shipping_rates;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import pet.eshop.common.entity.Country;
import pet.eshop.common.entity.ShippingRate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ShippingRatesRepositoryTests {

    @Autowired
    private ShippingRatesRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNew(){
        Country country = entityManager.find(Country.class, 192); //russia

        ShippingRate shippingRate = new ShippingRate();
        shippingRate.setCountry(country);
        shippingRate.setState("Tagil");
        shippingRate.setDays(17);
        shippingRate.setRate(11);
        shippingRate.setCodSupported(true);

        ShippingRate savedRate = repo.save(shippingRate);

        assertThat(savedRate.getId()).isGreaterThan(0);

    }

    @Test
    public void testUpdate(){

        ShippingRate currentRate = repo.findById(1).get();
        String oldState = currentRate.getState();
        currentRate.setState("Voronezh");
        ShippingRate savedRate = repo.save(currentRate);

        assertThat(savedRate.getState()).isNotEqualTo(oldState);
    }

    @Test
    public void testFindAll(){
        List<ShippingRate> rates = (List<ShippingRate>) repo.findAll();
        rates.forEach(System.out::println);
        assertThat(rates.size()).isGreaterThan(0);
    }

    @Test
    public void testFindByCountryAndState(){
        Integer countryId = 192; //russia
        String state1 = "Moskva";
        String state2 = "Voronezh";

        ShippingRate rate1 = repo.findByCountryAndState(countryId, state1);
        ShippingRate rate2 = repo.findByCountryAndState(countryId, state2);

        assertThat(rate1).isNull();
        assertThat(rate2).isNotNull();
    }

    @Test
    public void testUpdateCODSupport(){
        Integer rateId = 2;
        boolean supported = false;
        repo.updateCODSupport(rateId, supported);

        ShippingRate rate = repo.findById(rateId).get();
        assertThat(rate.isCodSupported()).isFalse();
    }

    @Test
    public void testDelete(){
        Integer rateId = 2;
        repo.deleteById(rateId);

        Optional<ShippingRate> rate = repo.findById(rateId);
        assertThat(rate).isNotPresent();
    }

}
