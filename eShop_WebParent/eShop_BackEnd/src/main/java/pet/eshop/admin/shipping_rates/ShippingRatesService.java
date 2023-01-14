package pet.eshop.admin.shipping_rates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.eshop.admin.paging.PagingAndSortingHelper;
import pet.eshop.admin.products.ProductRepository;
import pet.eshop.admin.settings.country.CountryRepository;
import pet.eshop.common.entity.Country;
import pet.eshop.common.entity.ShippingRate;
import pet.eshop.common.entity.product.Product;
import pet.eshop.common.exception.ShippingRateAlreadyExistsException;
import pet.eshop.common.exception.ShippingRateNotFoundException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ShippingRatesService {

    @Autowired
    private ShippingRatesRepository repository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private ProductRepository productRepository;

    public final static int RATES_PER_PAGE = 10;
    private static final int DIM_DIVISOR = 139; //139 or 163 - for inches, 4000 or 5000 - for sm.

    public void listByPage(Integer pageNum, PagingAndSortingHelper helper) {
        helper.listEntities(pageNum, RATES_PER_PAGE, repository);
    }

    public List<Country> getCountryList(){
        return countryRepository.findAllByOrderByNameAsc();
    }

    public ShippingRate get(Integer id) throws ShippingRateNotFoundException {
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new ShippingRateNotFoundException("Could not find any Shipping Rate with ID = " + id);
        }
    }

    public boolean isRateUnique(Integer id, Integer countryId, String state) {
        ShippingRate rate = repository.findByCountryAndState(countryId, state);
        if (rate == null) return true;

        if (id == null) return false;
            else return rate.getId() == id;
    }

    public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
        ShippingRate rateInDB = repository.findByCountryAndState(rateInForm.getCountry().getId(),
                                                                    rateInForm.getState());
        boolean alreadyExists = (rateInForm.getId() == null && rateInDB != null) ||
                                    (rateInForm.getId() != null && rateInDB != null);

        if (alreadyExists) throw new ShippingRateAlreadyExistsException("There's already a rate for the destination: "
                                                + rateInForm.getCountry().getName() + ","
                                                + rateInForm.getState());

        repository.save(rateInForm);
    }

    public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException{
        Long count = repository.countById(id);
        if (count == null || count == 0) {
            throw new ShippingRateNotFoundException("Could not find shipping rate with ID = " + id);
        }

        repository.updateCODSupport(id, codSupported);
    }

    public void delete(Integer id) throws ShippingRateNotFoundException {
        Long count = repository.countById(id);
        if (count == null || count == 0) {
            throw new ShippingRateNotFoundException("Could not find shipping rate with ID = " + id);
        }

        repository.deleteById(id);
    }

    public float calculateShippingCost(Integer productId, Integer countryId,
                                       String state) throws ShippingRateNotFoundException {
        ShippingRate shippingRate = repository.findByCountryAndState(countryId, state);

        if (shippingRate == null) {
            throw new ShippingRateNotFoundException("No shipping rate found for the given"
            + " destination. You have to enter shipping cost manually.");
        }

        Product product = productRepository.findById(productId).orElseThrow();
        float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
        float finalWeight = Math.max(product.getWeight(), dimWeight);

        return finalWeight * shippingRate.getRate();
    }
}
