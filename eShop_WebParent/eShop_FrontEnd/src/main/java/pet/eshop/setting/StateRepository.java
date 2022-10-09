package pet.eshop.setting;

import org.springframework.data.repository.CrudRepository;
import pet.eshop.common.entity.Country;
import pet.eshop.common.entity.State;

import java.util.List;

public interface StateRepository extends CrudRepository<State, Integer> {

    public List<State> findAllByOrderByNameAsc();

    public List<State> findByCountryOrderByNameAsc(Country country);
}
