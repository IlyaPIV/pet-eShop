package pet.eshop.admin.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pet.eshop.admin.util.AmazonS3Util;
import pet.eshop.admin.util.FileUploadUtil;
import pet.eshop.common.Constants;
import pet.eshop.common.entity.Currency;
import pet.eshop.common.entity.setting.Setting;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class SettingController {
    @Autowired
    private SettingService service;
    @Autowired
    private CurrencyRepository currencyRepo;

    @GetMapping("/settings")
    public String listAll(Model model){

        List<Setting> settingList = service.listAllSettings();
        List<Currency> currencyList = currencyRepo.findAllByOrderByNameAsc();

        model.addAttribute("listCurrencies", currencyList);
        model.addAttribute("pageTitle", "Settings - application Admin");

        settingList.forEach(setting -> {
            model.addAttribute(setting.getKey(), setting.getValue());
        });

        model.addAttribute("S3_BASE_URI", Constants.S3_BASE_URI);

        return "settings/settings";
    }

    @PostMapping("/settings/save_general")
    public String saveGeneral(@RequestParam("fileImage")MultipartFile multipartFile,
                              HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {

        GeneralSettingBag settingBag = service.getGeneralSettings();

        saveSiteLogo(multipartFile, settingBag);
        saveCurrencySymbol(request, settingBag);
        updateSettingValuesFromForm(request, settingBag.list());

        redirectAttributes.addFlashAttribute("message", "General settings have been saved.");

        return "redirect:/settings";
    }

    private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String value = "/site-logo/" + fileName;
            settingBag.updateSiteLogo(value);

            // local
//            String uploadDir = "../site-logo/";
//            FileUploadUtil.cleanDir(uploadDir);
//            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

            // amazon
            String uploadDir = "site-logo";
            AmazonS3Util.removeFolder(uploadDir);
            AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
        }
    }

    private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag){
        Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        Optional<Currency> findResult = currencyRepo.findById(currencyId);

        if (findResult.isPresent()) {
            Currency currency = findResult.get();
            settingBag.updateCurrencySymbol(currency.getSymbol());
        }
    }

    private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> settingList) {
        settingList.forEach(setting -> {
            String value = request.getParameter(setting.getKey());
            if (value != null) {
                setting.setValue(value);
            }
        });

        service.saveAll(settingList);
    }

    @PostMapping("/settings/save_mail_server")
    public String saveMailServerSettings(HttpServletRequest request,
                                            RedirectAttributes redirectAttributes){
        List<Setting> mailServerSettings = service.getMailServerSettings();

        updateSettingValuesFromForm(request, mailServerSettings);
        redirectAttributes.addFlashAttribute("message", "Mail server settings have been saved.");

        return "redirect:/settings";
    }

    @PostMapping("/settings/save_mail_templates")
    public String saveMailTemplateSettings(HttpServletRequest request,
                                            RedirectAttributes redirectAttributes){
        List<Setting> mailTemplateSettings = service.getMailTemplateSettings();

        updateSettingValuesFromForm(request, mailTemplateSettings);
        redirectAttributes.addFlashAttribute("message", "Mail templates settings have been saved.");

        return "redirect:/settings";
    }

    @PostMapping("/settings/save_payment")
    public String savePaymentSettings(HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) {
        List<Setting> paymentSettings = service.getPaymentSettings();
        updateSettingValuesFromForm(request, paymentSettings);

        redirectAttributes.addFlashAttribute("message", "Payment settings have been saved");

        return "redirect:/settings#payment";
    }
}
