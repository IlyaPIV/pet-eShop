package pet.eshop.admin.settings;

import org.springframework.data.repository.CrudRepository;
import pet.eshop.common.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {
}
