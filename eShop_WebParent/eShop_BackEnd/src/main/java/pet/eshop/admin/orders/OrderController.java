package pet.eshop.admin.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pet.eshop.admin.paging.PagingAndSortingHelper;
import pet.eshop.admin.paging.PagingAndSortingParam;
import pet.eshop.admin.settings.SettingService;
import pet.eshop.common.entity.Setting;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    private final String defaultRedirectURL = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";

    @Autowired private OrderService orderService;
    @Autowired private SettingService settingService;

    @GetMapping("/orders")
    public String listFirstPage(){
        return defaultRedirectURL;
    }

    @GetMapping("/orders/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "listOrders", moduleURL = "/orders")PagingAndSortingHelper helper,
                             @PathVariable(name = "pageNum") int pageNum,
                             HttpServletRequest request){

        orderService.listByPage(pageNum, helper);
        loadCurrencySetting(request);

        return "orders/orders";
    }

    private void loadCurrencySetting(HttpServletRequest request){
        List<Setting> currencySettings = settingService.getCurrencySettings();

        for (Setting setting:
             currencySettings) {
            request.setAttribute(setting.getKey(), setting.getValue());
        }
    }

}
