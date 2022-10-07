package pet.eshop.admin.settings.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pet.eshop.common.entity.Country;

import java.util.List;

@RestController
public class CountryRestController {

    @Autowired
    private CountryRepository repo;

    @GetMapping("/countries/list")
    public List<Country> listAll(){
        return repo.findAllByOrderByNameAsc();
    }

    @PostMapping("/countries/save")
    public String save(@RequestBody Country country){
        Country saved = repo.save(country);

        return String.valueOf(saved.getId());
    }

    @GetMapping("/countries/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        repo.deleteById(id);
    }

}
