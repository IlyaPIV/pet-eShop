package pet.eshop.admin.settings;

import org.springframework.data.repository.CrudRepository;
import pet.eshop.common.entity.State;

import java.util.List;

public interface StateRepository extends CrudRepository<State, Integer> {

    public List<State> findAllByOrderByNameAsc();
}
