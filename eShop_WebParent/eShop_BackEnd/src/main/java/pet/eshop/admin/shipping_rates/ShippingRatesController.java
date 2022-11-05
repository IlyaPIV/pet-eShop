package pet.eshop.admin.shipping_rates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pet.eshop.admin.paging.PagingAndSortingHelper;
import pet.eshop.admin.paging.PagingAndSortingParam;

@Controller
public class ShippingRatesController {

    @Autowired
    private ShippingRatesService service;

    @GetMapping("/shipping")
    public String listFirstPage(){
        return "redirect:/shipping/page/1?sortField=country&sortDir=asc";
    }

    @GetMapping("/shipping/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "listShippingRates", moduleURL = "/shipping")PagingAndSortingHelper helper,
                             @PathVariable("pageNum") Integer pageNum){
        service.listByPage(pageNum, helper);

        return "shipping_rates/shipping_rates";
    }
}
