package pet.eshop.admin.customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pet.eshop.admin.paging.PagingAndSortingHelper;
import pet.eshop.common.entity.Customer;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public final static int CUSTOMERS_PER_PAGE = 10;


    public void listByPage(Integer pageNum, PagingAndSortingHelper helper) {
        helper.listEntities(pageNum, CUSTOMERS_PER_PAGE, repo);
    }

    public void updateEnabledStatus(Integer id, boolean enabled){
        repo.updateEnabledStatus(id, enabled);
    }

    public Customer get(Integer id) throws CustomerNotFoundException {
        try{
            return repo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CustomerNotFoundException("Could not find any Customer with ID = " + id);
        }
    }

    public boolean isEmailUnique(String email, Integer id) {
        Customer customer = repo.findByEmail(email);

        return customer == null || customer.getId() == id;
    }

    private void encodePassword(Customer customer){
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
    }

    public void save(Customer customerInForm) {
        Customer existingCustomer = repo.findById(customerInForm.getId()).get();
        if (customerInForm.getPassword().isEmpty()){
            customerInForm.setPassword(existingCustomer.getPassword());
        } else  {
            encodePassword(customerInForm);
        }
        customerInForm.setEnabled(existingCustomer.isEnabled());
        customerInForm.setCreatedTime(existingCustomer.getCreatedTime());
        customerInForm.setVerificationCode(existingCustomer.getVerificationCode());

        repo.save(customerInForm);
    }

    public void delete(Integer id) throws CustomerNotFoundException {
        Long countByID = repo.countById(id);
        if (countByID == null || countByID == 0){
            throw new CustomerNotFoundException("Could not find any customer with ID = " + id);
        }

        repo.deleteById(id);
    }
}
