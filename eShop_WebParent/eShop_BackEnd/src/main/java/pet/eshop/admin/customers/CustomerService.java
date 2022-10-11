package pet.eshop.admin.customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pet.eshop.common.entity.Customer;

import javax.transaction.Transactional;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository repo;

    public final static int CUSTOMERS_PER_PAGE = 10;


    public Page<Customer> getPage(Integer pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, CUSTOMERS_PER_PAGE, sort);

        if (keyword == null){
            return  repo.findAll(pageable);
        } else {
            return repo.findAll(keyword, pageable);
        }
    }

    public void updateEnabledStatus(Integer id, boolean enabled){
        repo.updateEnabledStatus(id, enabled);
    }
}
