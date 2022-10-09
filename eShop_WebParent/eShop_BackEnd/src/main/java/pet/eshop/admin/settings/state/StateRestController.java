package pet.eshop.admin.settings.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pet.eshop.common.entity.Country;
import pet.eshop.common.entity.State;
import pet.eshop.common.entity.StateDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StateRestController {

    @Autowired
    private StateRepository repo;

    @GetMapping("/states/list/{id}")
    public List<StateDTO> listByCountryId(@PathVariable("id") Integer countryId){
        List<State> stateList = repo.findByCountryOrderByNameAsc(new Country(countryId));
        List<StateDTO> resultList = new ArrayList<>();
        stateList.forEach(state -> {
            resultList.add(new StateDTO(state));
        });

        return resultList;
    }

    @PostMapping("/states/save")
    public String save(@RequestBody State state) {
        System.out.println(state);

        State saved = repo.save(state);

        return String.valueOf(saved.getId());
    }

    @DeleteMapping("/states/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        repo.deleteById(id);
    }
}
