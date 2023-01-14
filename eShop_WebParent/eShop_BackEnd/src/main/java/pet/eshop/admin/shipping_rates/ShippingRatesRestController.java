package pet.eshop.admin.shipping_rates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pet.eshop.common.exception.ShippingRateNotFoundException;

@RestController
public class ShippingRatesRestController {
    @Autowired
    private ShippingRatesService ratesService;

    @PostMapping("/get_shipping_cost")
    public String getShippingCost(Integer productId, Integer countryId, String state) throws ShippingRateNotFoundException {
        float shippingCost = ratesService.calculateShippingCost(productId, countryId, state);

        return String.valueOf(shippingCost);
    }
}
