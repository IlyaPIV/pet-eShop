package pet.eshop.customer;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pet.eshop.common.entity.Country;
import pet.eshop.common.entity.Customer;
import pet.eshop.setting.CountryRepository;

import java.util.Date;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CountryRepository countryRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Country> listAllCountries(){
        return countryRepo.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(String email){
        Customer customer = customerRepo.findByEmail(email);

        return customer == null;
    }

    public void registerCustomer(Customer customer){
        encodePassword(customer);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());

        customer.setVerificationCode(RandomString.make(64));

        customerRepo.save(customer);
    }

    private void encodePassword(Customer customer){
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
    }
}
