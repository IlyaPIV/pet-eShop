package pet.eshop.setting;

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

    @GetMapping("/settings/list_states_by_country/{id}")
    public List<StateDTO> listByCountryId(@PathVariable("id") Integer countryId){
        List<State> stateList = repo.findByCountryOrderByNameAsc(new Country(countryId));
        List<StateDTO> resultList = new ArrayList<>();
        stateList.forEach(state -> {
            resultList.add(new StateDTO(state));
        });

        return resultList;
    }

}
