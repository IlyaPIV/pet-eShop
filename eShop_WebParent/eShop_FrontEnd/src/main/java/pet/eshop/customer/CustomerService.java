package pet.eshop.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.eshop.common.entity.Country;
import pet.eshop.common.entity.Customer;
import pet.eshop.setting.CountryRepository;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CountryRepository countryRepo;
    @Autowired
    private CustomerRepository customerRepo;

    public List<Country> listAllCountries(){
        return countryRepo.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(String email){
        Customer customer = customerRepo.findByEmail(email);

        return customer == null;
    }
}
