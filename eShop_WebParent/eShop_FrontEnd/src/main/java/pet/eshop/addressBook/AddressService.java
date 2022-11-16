package pet.eshop.addressBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.eshop.common.entity.Address;
import pet.eshop.common.entity.Customer;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AddressService {

    private final AddressRepository repository;

    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    public List<Address> addressList(Customer customer){
        return repository.findByCustomer(customer);
    }

    public void save(Address address){
        repository.save(address);
    }

    public Address get(Integer addressId, Integer customerId) {
        return repository.findByIdAndCustomerId(addressId, customerId);
    }

    public void delete(Integer addressId, Integer customerId){
        repository.deleteByIdAndCustomerId(addressId, customerId);
    }

    public void setDefaultAddress(Integer defaultAddressId, Customer customer){
        if (defaultAddressId > 0) {
            repository.setDefaultAddress(defaultAddressId);
        }
        repository.setNonDefaultForOthers(defaultAddressId, customer.getId());
    }
}
