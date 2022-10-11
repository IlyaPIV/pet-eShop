package pet.eshop.admin.customers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pet.eshop.common.entity.Customer;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer> {

    @Query("SELECT c FROM Customer c WHERE CONCAT(c.lastName, ' ', c.firstName, ' ', c.email) LIKE %?1%")
    public Page<Customer> findAll(String keyword, Pageable pageable);
}
