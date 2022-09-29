package pet.eshop.admin.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.eshop.common.entity.Product;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository repo;

    public List<Product> listAll(){
        return (List<Product>) repo.findAll();
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setCreatedTime(new Date());
        }

        if (product.getAlias().isEmpty() || product.getAlias() == null) {
            String defaultAlias = product.getName().replaceAll(" ","-");
            product.setAlias(defaultAlias);
        } else {
            product.setAlias(product.getAlias().replaceAll(" ","-"));
        }

        product.setUpdatedTime(new Date());

        return repo.save(product);
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);
        Product prodByName = repo.findByName(name);
        if (isCreatingNew) {
            if (prodByName != null) return "Duplicate";
        } else {
            if (prodByName != null && prodByName.getId() != id) return "Duplicate";
        }

        return "OK";
    }

    public void updateProductEnabledStatus(Integer id, boolean enabled) {
        repo.updateEnabledStatus(id, enabled);
    }

    public void delete(Integer id) throws ProductNotFoundException {
        Long countById = repo.countById(id);
        if (countById == null || countById == 0) {
            throw new ProductNotFoundException("Could not find any Product with ID = " + id);
        }
        repo.deleteById(id);
    }

    public Product get(Integer id) throws ProductNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException ex){
            throw new ProductNotFoundException("Could not find any Product with ID = " + id);
        }
    }
}
