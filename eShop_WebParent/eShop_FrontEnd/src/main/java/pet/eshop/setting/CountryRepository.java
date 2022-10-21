package pet.eshop.setting;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pet.eshop.common.entity.Country;

import java.util.List;

public interface CountryRepository extends CrudRepository<Country, Integer> {

    public List<Country> findAllByOrderByNameAsc();

    @Query("SELECT c FROM Country c WHERE c.code = ?1")
    public Country findByCode(String code);
}
