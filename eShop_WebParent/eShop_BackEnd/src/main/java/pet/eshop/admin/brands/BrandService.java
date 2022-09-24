package pet.eshop.admin.brands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import pet.eshop.admin.categories.CategoryService;
import pet.eshop.common.entity.Brand;
import pet.eshop.common.entity.Category;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {
    @Autowired
    private BrandRepository repo;
    @Autowired
    private CategoryService catService;


    public List<Brand> findAll(){
        return (List<Brand>) repo.findAll();
    }

    public List<Category> listCategoriesUsedInForm() {
        return catService.listCategoriesUsedInForm();
    }

    public Brand save(Brand brand) {
        return repo.save(brand);
    }

    public Brand get(Integer id) throws BrandNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new BrandNotFoundException("Could not find any brand with ID = " + id);
        }
    }

    public void delete(Integer id) throws BrandNotFoundException {
        Long countById = repo.countById(id);
        if (countById == null || countById == 0) {
            throw new BrandNotFoundException("Could not find any Brand with ID = " + id);
        }
        repo.deleteById(id);
    }
}
