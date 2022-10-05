package pet.eshop.admin.settings;

import org.springframework.data.repository.CrudRepository;
import pet.eshop.common.entity.Setting;

public interface SettingRepository extends CrudRepository<Setting, String> {
}
