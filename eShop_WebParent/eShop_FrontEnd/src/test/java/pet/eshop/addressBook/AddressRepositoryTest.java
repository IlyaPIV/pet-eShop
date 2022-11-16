package pet.eshop.addressBook;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import pet.eshop.common.entity.Address;
import pet.eshop.common.entity.Country;
import pet.eshop.common.entity.Customer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class AddressRepositoryTest {

    @Autowired
    private AddressRepository repository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testAddNew(){
        Integer countryId = 234; // USA
        Country country = entityManager.find(Country.class, countryId);

        Integer customerId = 37;
        Customer customer = entityManager.find(Customer.class, customerId);

        Address newAddress = new Address();
        newAddress.setFirstName(customer.getFirstName());
        newAddress.setLastName(customer.getLastName());
        newAddress.setCustomer(customer);
        newAddress.setCountry(country);
        newAddress.setAddressLine1("Main Street");
        newAddress.setAddressLine2("135/1");
        newAddress.setCity("Springfield");
        newAddress.setState("California");
        newAddress.setPostalCode("98765");
        newAddress.setPhoneNumber("123-456-7890");
        newAddress.setDefaultForShipping(false);

        Address savedAddress = repository.save(newAddress);
        assertThat(savedAddress.getId()).isNotNull();
    }

    @Test
    void testCopyNewFromCustomer(){
        Integer customerId = 2;
        Customer customer = entityManager.find(Customer.class, customerId);

        Address newAddress = new Address();
        newAddress.setFirstName(customer.getFirstName());
        newAddress.setLastName(customer.getLastName());
        newAddress.setCustomer(customer);
        newAddress.setCountry(customer.getCountry());
        newAddress.setAddressLine1(customer.getAddressLine1());
        newAddress.setAddressLine2(customer.getAddressLine2());
        newAddress.setCity(customer.getCity());
        newAddress.setState(customer.getState());
        newAddress.setPostalCode(customer.getPostalCode());
        newAddress.setPhoneNumber(customer.getPhoneNumber());
        newAddress.setDefaultForShipping(true);

        Address savedAddress = repository.save(newAddress);
        assertThat(savedAddress.getId()).isNotNull();
    }

    @Test
    void testFindByCustomer() {
        Integer customerId = 2;
        Customer customer = entityManager.find(Customer.class, customerId);

        List<Address> addresses = repository.findByCustomer(customer);
        addresses.forEach(System.out::println);

        assertThat(addresses.size()).isGreaterThan(0);
    }

    @Test
    void findByIdAndCustomer() {
        Integer customerId = 2;
        Customer customer = entityManager.find(Customer.class, customerId);
        Integer addressId = 1;

        Address address = repository.findByIdAndCustomer(addressId, customer);
        System.out.println(address);

        assertThat(address).isNotNull();
    }

    @Test
    void deleteByIdAndCustomer() {
        Integer customerId = 2;
        Customer customer = entityManager.find(Customer.class, customerId);
        Integer addressId = 1;

        repository.deleteByIdAndCustomer(addressId, customer);

        Address address = repository.findByIdAndCustomer(addressId, customer);

        assertThat(address).isNull();
    }

    @Test
    public void testSetDefault(){
        Integer addressId = 8;
        repository.setDefaultAddress(addressId);

        Address address = repository.findById(addressId).get();
        assertThat(address.isDefaultForShipping()).isTrue();
    }

    @Test
    public void testSetNonDefaultAddresses(){
        Integer addressId = 8;
        Integer customerId = 5;

        repository.setNonDefaultForOthers(addressId, customerId);
    }
}