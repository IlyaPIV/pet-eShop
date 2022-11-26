package pet.eshop.shipping;

import org.springframework.data.repository.CrudRepository;
import pet.eshop.common.entity.Country;
import pet.eshop.common.entity.ShippingRate;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {

    public ShippingRate findByCountryAndState(Country country, String state);

}
