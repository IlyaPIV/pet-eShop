package pet.eshop.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import pet.eshop.common.entity.Country;
import pet.eshop.common.entity.Customer;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository repo;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateCustomer(){
        Integer countryID = 234;    // USA
        Country country = entityManager.find(Country.class, countryID);

        Customer newCustomer = new Customer();
        newCustomer.setEmail("testEmail@email.usa");
        newCustomer.setCountry(country);
        newCustomer.setFirstName("John");
        newCustomer.setLastName("Doe");
        newCustomer.setPassword("12345678");
        newCustomer.setPhoneNumber("123-456-7890");
        newCustomer.setCity("Springfield");
        newCustomer.setState("California");
        newCustomer.setPostalCode("98765");
        newCustomer.setAddressLine1("98765 Main Street");
        newCustomer.setAddressLine2("135/1");
        newCustomer.setVerificationCode("7940588");

        Customer savedCustomer = repo.save(newCustomer);
        assertThat(savedCustomer.getId()).isNotNull();
    }

    @Test
    public void testListCustomers(){
        List<Customer> customerList = (List<Customer>) repo.findAll();
        customerList.forEach(System.out::println);
    }

    @Test
    public void testUpdateCustomer(){
        String newPostalCode = "12-300";
        Integer customerId = 1;
        Customer customer = repo.findById(customerId).get();

        customer.setPostalCode(newPostalCode);
        Customer savedCustomer = repo.save(customer);

        assertThat(savedCustomer.getPostalCode()).isEqualTo(newPostalCode);
    }

    @Test
    public void testGetCustomer(){

        Integer customerId = 1;
        Optional<Customer> customer = repo.findById(customerId);

        assertThat(customer).isPresent();
    }

    @Test
    public void testDeleteCustomer(){
        Integer customerId = 1;
        repo.deleteById(customerId);
        Optional<Customer> customer = repo.findById(customerId);

        assertThat(customer).isNotPresent();
    }

    @Test
    public void testFindByEmail(){
        String email = "testEmail@email.usa";
        Customer customer = repo.findByEmail(email);

        assertThat(customer.getId()).isNotNull();
    }

    @Test
    public void testFindByVerificationCode(){
        String veryCode = "testCode";
        Customer customer = repo.findByVerificationCode(veryCode);

        assertThat(customer).isNull();
    }

    @Test
    public void testEnableCustomer(){
        Integer customerId = 1;
        repo.enable(customerId);

        Customer customer = entityManager.find(Customer.class, customerId);
        assertThat(customer.isEnabled()).isTrue();
    }
}
