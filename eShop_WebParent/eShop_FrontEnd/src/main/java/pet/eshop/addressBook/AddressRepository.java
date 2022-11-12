package pet.eshop.addressBook;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pet.eshop.common.entity.Address;
import pet.eshop.common.entity.Customer;

import java.util.List;

public interface AddressRepository extends CrudRepository<Address, Integer> {

    public List<Address> findByCustomer(Customer customer);

    public Address findByIdAndCustomer(Integer id, Customer customer);

    @Query("SELECT a FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
    public Address findByIdAndCustomerId(Integer addressId, Integer customerId);

    @Modifying
    public void deleteByIdAndCustomer(Integer id, Customer customer);

    @Query("DELETE FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
    @Modifying
    public void deleteByIdAndCustomerId(Integer addressId, Integer customerId);
}
