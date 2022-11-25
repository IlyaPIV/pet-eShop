package pet.eshop.admin.settings;

import pet.eshop.common.entity.setting.Setting;
import pet.eshop.common.entity.setting.SettingBag;

import java.util.List;

public class GeneralSettingBag extends SettingBag {

    public GeneralSettingBag(List<Setting> settingList) {
        super(settingList);
    }

    public void updateCurrencySymbol(String value){
        super.update("CURRENCY_SYMBOL", value);
    }

    public void updateSiteLogo(String value){
        super.update("SITE_LOGO", value);
    }


}
