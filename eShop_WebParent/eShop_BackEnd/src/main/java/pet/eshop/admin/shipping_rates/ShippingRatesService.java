package pet.eshop.admin.shipping_rates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.eshop.admin.paging.PagingAndSortingHelper;

@Service
public class ShippingRatesService {

    @Autowired
    private ShippingRatesRepository repository;

    public final static int RATES_PER_PAGE = 10;

    public void listByPage(Integer pageNum, PagingAndSortingHelper helper) {
        helper.listEntities(pageNum, RATES_PER_PAGE, repository);
    }
}
