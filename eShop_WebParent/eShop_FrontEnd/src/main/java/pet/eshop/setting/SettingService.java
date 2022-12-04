package pet.eshop.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.eshop.common.entity.Currency;
import pet.eshop.common.entity.setting.Setting;
import pet.eshop.common.entity.setting.SettingCategory;

import java.util.List;
import java.util.Optional;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;
    @Autowired
    private CurrencyRepository currencyRepository;

    public List<Setting> getGeneralSettings(){
        return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }

   public EmailSettingBag getEmailSettings(){
        List<Setting> settings = settingRepository.findByTwoCategories(SettingCategory.MAIL_SERVER, SettingCategory.MAIL_TEMPLATES);
        return new EmailSettingBag(settings);
   }

   public CurrencySettingBag getCurrencySettings(){
        List<Setting> settings = settingRepository.findByCategory(SettingCategory.CURRENCY);
        return new CurrencySettingBag(settings);
   }

   public PaymentSettingBag getPaymentSettings(){
        List<Setting> settings = settingRepository.findByCategory(SettingCategory.PAYMENT);
        return new PaymentSettingBag(settings);
   }

   public String getCurrencyCode(){
       Setting setting = settingRepository.findByKey("CURRENCY_ID");
       Integer currencyId = Integer.parseInt(setting.getValue());
       Currency currency = currencyRepository.findById(currencyId).get();

       return currency.getCode();
   }

}
