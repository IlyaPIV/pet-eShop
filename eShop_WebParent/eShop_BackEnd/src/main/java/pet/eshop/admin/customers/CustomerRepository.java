package pet.eshop.admin.customers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pet.eshop.admin.paging.SearchRepository;
import pet.eshop.common.entity.Customer;

@Repository
public interface CustomerRepository extends SearchRepository<Customer, Integer> {

    @Query("SELECT c FROM Customer c WHERE CONCAT(c.lastName, ' ', c.firstName, ' ', c.email) LIKE %?1%")
    public Page<Customer> findAll(String keyword, Pageable pageable);

    @Modifying
    @Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
    public void updateEnabledStatus(Integer id, boolean enabled);

    public Customer findByEmail(String email);

    public Long countById(Integer id);
}
