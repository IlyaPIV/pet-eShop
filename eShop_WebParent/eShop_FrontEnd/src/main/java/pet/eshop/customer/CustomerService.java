package pet.eshop.customer;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import pet.eshop.common.entity.AuthenticationType;
import pet.eshop.common.entity.Country;
import pet.eshop.common.entity.Customer;
import pet.eshop.setting.CountryRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
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
        customer.setAuthenticationType(AuthenticationType.DATABASE);

        customer.setVerificationCode(RandomString.make(64));

        customerRepo.save(customer);
    }

    public Customer getCustomerByEmail(String email){
        return customerRepo.findByEmail(email);
    }

    private void encodePassword(Customer customer){
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
    }

    public boolean verify(String verificationCode) {
        Customer customer = customerRepo.findByVerificationCode(verificationCode);

        if (customer == null || customer.isEnabled()) {
            return false;
        } else {
            customerRepo.enable(customer.getId());
            return true;
        }
    }

    public void updateAuthenticationType(Customer customer, AuthenticationType type){
        if (customer.getAuthenticationType() != type){
            customerRepo.updateAuthenticationType(customer.getId(), type);
        }
    }

    public void addNewCustomerOAuthLogin(String name, String email, String countryCode,
                                         AuthenticationType authenticationType) {
        Customer customer = new Customer();
        customer.setEmail(email);
        setName(name, customer);
        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(authenticationType);
        customer.setPassword("");
        customer.setAddressLine1("");
        customer.setCity("");
        customer.setState("");
        customer.setPhoneNumber("");
        customer.setPostalCode("");
        customer.setCountry(countryRepo.findByCode(countryCode));

        customerRepo.save(customer);
    }

    private void setName(String name, Customer customer){
        String[] nameArray = name.split(" ");
        if (nameArray.length < 2) {
            customer.setFirstName(name);
            customer.setLastName("");
        } else {
            customer.setFirstName(nameArray[0]);
            String lastName = name.replaceFirst(nameArray[0] + " ", "");
            customer.setLastName(lastName);
        }
    }

    public void update(Customer customerInForm) {
        Customer existingCustomer = customerRepo.findById(customerInForm.getId()).get();

        if (existingCustomer.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
            if (customerInForm.getPassword().isEmpty()) {
                customerInForm.setPassword(existingCustomer.getPassword());
            } else {
                encodePassword(customerInForm);
            }
        } else {
            customerInForm.setPassword(existingCustomer.getPassword());
        }

        customerInForm.setEnabled(existingCustomer.isEnabled());
        customerInForm.setCreatedTime(existingCustomer.getCreatedTime());
        customerInForm.setVerificationCode(existingCustomer.getVerificationCode());
        customerInForm.setAuthenticationType(existingCustomer.getAuthenticationType());

        customerRepo.save(customerInForm);
    }



}
