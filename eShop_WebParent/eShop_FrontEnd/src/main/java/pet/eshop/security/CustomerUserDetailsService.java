package pet.eshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pet.eshop.common.entity.Customer;
import pet.eshop.customer.CustomerRepository;

public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = repository.findByEmail(email);

        if (customer != null) {
            return new CustomerUserDetails(customer);
        }

        throw new UsernameNotFoundException("Could not find customer with email " + email);
    }
}
