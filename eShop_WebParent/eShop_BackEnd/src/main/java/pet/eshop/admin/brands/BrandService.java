package pet.eshop.admin.brands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import pet.eshop.admin.categories.CategoryService;
import pet.eshop.common.entity.Brand;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandRepository repo;
    @Autowired
    private CategoryService catService;


    public List<Brand> findAll(){
        return (List<Brand>) repo.findAll();
    }

}
