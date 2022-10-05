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
import pet.eshop.admin.util.FileUploadUtil;
import pet.eshop.common.entity.Currency;
import pet.eshop.common.entity.Setting;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

            String uploadDir = "../site-logo/";
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
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

}
