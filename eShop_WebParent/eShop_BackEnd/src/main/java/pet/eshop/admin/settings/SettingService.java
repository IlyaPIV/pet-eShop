package pet.eshop.admin.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.eshop.common.entity.Setting;
import pet.eshop.common.entity.SettingCategory;

import java.util.List;

@Service
public class SettingService {

    @Autowired
    private SettingRepository repo;

    public List<Setting> listAllSettings(){
        return (List<Setting>) repo.findAll();
    }

    public GeneralSettingBag getGeneralSettings(){
        return new GeneralSettingBag(repo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY));
    }

    public void saveAll(Iterable<Setting> settings) {
        repo.saveAll(settings);
    }

    public List<Setting> getMailServerSettings(){
        return repo.findByCategory(SettingCategory.MAIL_SERVER);
    }
    public List<Setting> getMailTemplateSettings(){
        return repo.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }

    public List<Setting> getCurrencySettings(){
        return repo.findByCategory(SettingCategory.CURRENCY);
    }
}
