package pet.eshop.setting;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pet.eshop.common.entity.setting.Setting;
import pet.eshop.common.entity.setting.SettingCategory;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting, String> {

    public List<Setting> findByCategory(SettingCategory category);

    @Query("SELECT s FROM Setting s where s.category = ?1 OR s.category = ?2")
    public List<Setting> findByTwoCategories(SettingCategory categoryOne, SettingCategory categoryTwo);
}
