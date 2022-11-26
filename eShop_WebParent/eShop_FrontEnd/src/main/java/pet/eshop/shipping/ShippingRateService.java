package pet.eshop.shipping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.eshop.common.entity.Address;
import pet.eshop.common.entity.Customer;
import pet.eshop.common.entity.ShippingRate;

@Service
public class ShippingRateService {

    @Autowired private ShippingRateRepository repository;

    public ShippingRate getShippingRateForCustomer(Customer customer){
        String state = customer.getState();
        if (state == null || state.isBlank()) {
            state = customer.getCity();
        }

        return repository.findByCountryAndState(customer.getCountry(), state);
    }

    public ShippingRate getShippingRateForAddress(Address address){
        String state = address.getState();
        if (state == null || state.isBlank()) {
            state = address.getCity();
        }

        return repository.findByCountryAndState(address.getCountry(), state);
    }
}
