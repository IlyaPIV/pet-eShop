package pet.eshop.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pet.eshop.common.entity.Product;
import pet.eshop.common.exception.ProductNotFoundException;

@Service
public class ProductService {

    public static final int PRODUCTS_PER_PAGE = 10;
    public static final int SEARCH_RESULTS_PER_PAGE = 8;

    @Autowired
    private ProductRepository repository;

    public Page<Product> listByCategory(int pageNum, Integer categoryId){
        String categoryIdMatch = "-" + categoryId + "-";
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);

        return repository.listByCategory(categoryId, categoryIdMatch, pageable);
    }

    public Product getProduct(String alias) throws ProductNotFoundException {
        Product product = repository.findByAlias(alias);

        if (product == null) {
            throw new ProductNotFoundException("Could not find any Product with alias = " + alias);
        }
        return product;
    }

    public Page<Product> search(String keyword, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE);

        return repository.search(keyword, pageable);
    }
}
