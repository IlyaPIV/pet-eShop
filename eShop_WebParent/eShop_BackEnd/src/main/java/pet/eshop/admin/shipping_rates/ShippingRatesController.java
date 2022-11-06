package pet.eshop.admin.shipping_rates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pet.eshop.admin.paging.PagingAndSortingHelper;
import pet.eshop.admin.paging.PagingAndSortingParam;
import pet.eshop.common.entity.ShippingRate;
import pet.eshop.common.exception.ShippingRateAlreadyExistsException;
import pet.eshop.common.exception.ShippingRateNotFoundException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;

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

    @GetMapping("/shipping/new")
    public String newRate(Model model){

        model.addAttribute("rate", new ShippingRate());
        model.addAttribute("pageTitle", "Create new Shipping Rate");
        model.addAttribute("listCountries", service.getCountryList());

        return "shipping_rates/shipping_rate_form";
    }

    @GetMapping("/shipping/edit/{id}")
    public String editRate(@PathVariable("id") Integer id, Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            ShippingRate rate = service.get(id);

            model.addAttribute("rate", rate);
            model.addAttribute("pageTitle", "Edit Shipping Rate (ID=" + id + ")");
            model.addAttribute("listCountries", service.getCountryList());

            return "shipping_rates/shipping_rate_form";
        } catch (ShippingRateNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/shipping";
        }
    }

    @PostMapping("/shipping/save")
    public String saveRate(ShippingRate shippingRate, RedirectAttributes redirectAttributes){
        try {
            service.save(shippingRate);
            redirectAttributes.addFlashAttribute("message", "The shipping rate has been saved successfully.");
        } catch (ShippingRateAlreadyExistsException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/shipping";
    }

    @GetMapping("/shipping/cod/{id}/supported/{supported}")
    @Transactional
    public String updateCODSupport(@PathVariable(name = "id") Integer id,
                                   @PathVariable(name = "supported") Boolean supported,
                                   Model model, RedirectAttributes redirectAttributes) {
        try {
            service.updateCODSupport(id, supported);
            redirectAttributes.addFlashAttribute("message", "COD support for shipping rate (ID = " +
                                                    id + ") has been updated");
        } catch (ShippingRateNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/shipping";
    }

    @GetMapping("/shipping/delete/{id}")
    public String deleteRate(@PathVariable(name = "id") Integer id, Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("message", "The shipping rate ID " + id +
                                                                            " has been deleted.");
        } catch (ShippingRateNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/shipping";
    }
}
