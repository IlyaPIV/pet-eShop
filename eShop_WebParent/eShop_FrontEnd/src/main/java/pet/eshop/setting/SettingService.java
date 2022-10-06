package pet.eshop.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.eshop.common.entity.Setting;
import pet.eshop.common.entity.SettingCategory;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {

    @Autowired
    private SettingRepository repo;

    public List<Setting> getGeneralSettings(){
        return repo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }

    public void saveAll(Iterable<Setting> settings) {
        repo.saveAll(settings);
    }
}
