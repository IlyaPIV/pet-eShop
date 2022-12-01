package pet.eshop.setting;

import pet.eshop.common.entity.setting.Setting;
import pet.eshop.common.entity.setting.SettingBag;

import java.util.List;

public class CurrencySettingBag extends SettingBag {
    public CurrencySettingBag(List<Setting> settingList) {
        super(settingList);
    }

    public String getSymbol(){
        return super.getValue("CURRENCY_SYMBOL");
    }

    public String getSymbolPosition(){
        return super.getValue("CURRENCY_SYMBOL_POSITION");
    }

    public String getDecimalPointType(){
        return super.getValue("DECIMAL_POINT_TYPE");
    }

    public String getThousandsPointType(){
        return super.getValue("THOUSANDS_POINT_TYPE");
    }

    public int getDecimalDigits(){
        return Integer.parseInt(super.getValue("DECIMAL_DIGITS"));
    }
}
