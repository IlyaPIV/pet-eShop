package pet.eshop.admin.settings;

import org.springframework.data.repository.CrudRepository;
import pet.eshop.common.entity.Currency;

import java.util.List;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

    public List<Currency> findAllByOrderByNameAsc();
}
