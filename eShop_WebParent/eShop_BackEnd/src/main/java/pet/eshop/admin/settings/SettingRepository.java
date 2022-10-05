package pet.eshop.admin.settings;

import org.springframework.data.repository.CrudRepository;
import pet.eshop.common.entity.Setting;
import pet.eshop.common.entity.SettingCategory;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting, String> {

    public List<Setting> findByCategory(SettingCategory category);
}
