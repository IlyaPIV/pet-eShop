package pet.eshop.admin.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pet.eshop.admin.paging.PagingAndSortingHelper;
import pet.eshop.admin.paging.PagingAndSortingParam;
import pet.eshop.admin.settings.SettingService;
import pet.eshop.common.entity.Order;
import pet.eshop.common.entity.Setting;
import pet.eshop.common.exception.OrderNotFoundException;

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

    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetails(@PathVariable("id") Integer id, Model model,
                                   RedirectAttributes ra,
                                   HttpServletRequest request) {
        try {
            Order order = orderService.getOrder(id);
            loadCurrencySetting(request);
            model.addAttribute("order", order);
            model.addAttribute("isVisibleForAdminOrSalesperson", true);
            return "orders/order_detail_modal";
        } catch (OrderNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return defaultRedirectURL;
        }
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            orderService.deleteOrder(id);
            ra.addFlashAttribute("message", "The Order ID: " + id + " has been deleted.");
        } catch (OrderNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }
        return defaultRedirectURL;
    }

}
