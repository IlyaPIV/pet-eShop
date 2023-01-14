package pet.eshop.admin.shipping_rates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pet.eshop.admin.products.ProductRepository;
import pet.eshop.common.entity.ShippingRate;
import pet.eshop.common.entity.product.Product;
import pet.eshop.common.exception.ShippingRateNotFoundException;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class ShippingRatesServiceTest {
    @MockBean private ShippingRatesRepository shipRepo;
    @MockBean private ProductRepository productRepo;

    @InjectMocks
    private ShippingRatesService shipService;

    @Test
    public void testCalculateShippingCost_NoRateFound() {
        Integer productId = 1;
        Integer countryId = 234;
        String state = "ABCDE";

        Mockito.when(shipRepo.findByCountryAndState(countryId, state)).thenReturn(null);

        assertThrows(ShippingRateNotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                shipService.calculateShippingCost(productId, countryId, state);
            }
        });
    }

    @Test
    public void testCalculateShippingCost_RateFound() throws ShippingRateNotFoundException {
        Integer productId = 1;
        Integer countryId = 234;
        String state = "New York";

        ShippingRate rate = new ShippingRate();
        rate.setRate(10);

        Mockito.when(shipRepo.findByCountryAndState(countryId, state)).thenReturn(rate);

        Product product = new Product();
        product.setWeight(5);
        product.setWidth(4);
        product.setHeight(3);
        product.setLength(8);

        Mockito.when(productRepo.findById(productId)).thenReturn(Optional.of(product));

        float shippingCost = shipService.calculateShippingCost(productId, countryId, state);

        assertEquals(50, shippingCost);
    }

}