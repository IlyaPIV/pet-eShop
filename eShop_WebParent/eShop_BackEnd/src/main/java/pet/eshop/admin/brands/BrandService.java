package pet.eshop.admin.brands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.eshop.admin.categories.CategoryService;
import pet.eshop.admin.paging.PagingAndSortingHelper;
import pet.eshop.common.entity.Brand;
import pet.eshop.common.entity.Category;
import pet.eshop.common.exception.BrandNotFoundException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {

    public static final int BRANDS_PER_PAGE = 8;

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

    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);
        Brand brandByName = repo.findByName(name);
        if (isCreatingNew) {
            if (brandByName != null) return "Duplicate";
        } else {
            if (brandByName != null && brandByName.getId() != id) return "Duplicate";
        }

        return "OK";
    }

    public void listByPage(int pageNum, PagingAndSortingHelper helper){
        helper.listEntities(pageNum, BRANDS_PER_PAGE, repo);
    }
}
