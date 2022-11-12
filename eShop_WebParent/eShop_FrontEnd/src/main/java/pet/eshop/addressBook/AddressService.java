package pet.eshop.addressBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.eshop.common.entity.Address;
import pet.eshop.common.entity.Customer;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository repository;

    public List<Address> addressList(Customer customer){
        return repository.findByCustomer(customer);
    }
}
