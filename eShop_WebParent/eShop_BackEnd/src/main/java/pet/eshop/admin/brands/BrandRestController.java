package pet.eshop.admin.brands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pet.eshop.admin.categories.CategoryDTO;
import pet.eshop.common.entity.Brand;
import pet.eshop.common.entity.Category;
import pet.eshop.common.exception.BrandNotFoundException;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class BrandRestController {

    @Autowired
    public BrandService service;

    @PostMapping("/brands/check_unique")
    public String checkUnique(@Param("id") Integer id,
                              @Param("name") String name){
        return service.checkUnique(id, name);
    }

    @GetMapping("/brands/{id}/categories")
    public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandNotFoundRESTException {
        try {
            Brand brand = service.get(brandId);
            Set<Category> categories = brand.getCategories();
            List<CategoryDTO> listDTO = new ArrayList<>();

            for (Category cat : categories) {
                CategoryDTO dto = new CategoryDTO(cat.getId(), cat.getName());
                listDTO.add(dto);
            }

            return listDTO;

        } catch (BrandNotFoundException ex) {
            throw new BrandNotFoundRESTException();
        }
    }
}
