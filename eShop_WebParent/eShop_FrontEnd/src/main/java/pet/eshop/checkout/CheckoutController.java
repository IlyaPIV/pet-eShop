package pet.eshop.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pet.eshop.addressBook.AddressService;
import pet.eshop.common.entity.Address;
import pet.eshop.common.entity.CartItem;
import pet.eshop.common.entity.Customer;
import pet.eshop.common.entity.ShippingRate;
import pet.eshop.common.entity.order.Order;
import pet.eshop.common.entity.order.PaymentMethod;
import pet.eshop.customer.CustomerService;
import pet.eshop.order.OrderService;
import pet.eshop.setting.CurrencySettingBag;
import pet.eshop.setting.EmailSettingBag;
import pet.eshop.setting.SettingService;
import pet.eshop.shipping.ShippingRateService;
import pet.eshop.shoppingcart.ShoppingCartService;
import pet.eshop.util.Utility;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class CheckoutController {
    @Autowired private CheckoutService checkoutService;
    @Autowired private CustomerService customerService;
    @Autowired private AddressService addressService;
    @Autowired private ShippingRateService shipService;
    @Autowired private ShoppingCartService cartService;
    @Autowired private OrderService orderService;
    @Autowired private SettingService settingService;

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpServletRequest request){
        Customer customer = getAuthenticatedCustomer(request);
                ShippingRate shippingRate = null;
        Address defaultAddress = addressService.getDefaultAddress(customer);
        if (defaultAddress != null) {
            model.addAttribute("shippingAddress", defaultAddress.toString());
            shippingRate = shipService.getShippingRateForAddress(defaultAddress);
        } else {
            model.addAttribute("shippingAddress", customer.getAddress());
            shippingRate = shipService.getShippingRateForCustomer(customer);
        }
        if (shippingRate == null) {
            return "redirect:/cart";
        }
        List<CartItem> cartItems = cartService.cartItemList(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
        model.addAttribute("checkoutInfo", checkoutInfo);
        model.addAttribute("cartItems", cartItems);

        return "checkout/checkout";
    }

    public Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String customerEmail = Utility.getEmailOfAuthenticatedCustomer(request);

        return customerService.getCustomerByEmail(customerEmail);
    }

    @PostMapping("/place_order")
    public String placeOrder(HttpServletRequest request)
            throws MessagingException, UnsupportedEncodingException {
        String paymentType = request.getParameter("paymentMethod");
        PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);

        Customer customer = getAuthenticatedCustomer(request);
        ShippingRate shippingRate = null;
        Address defaultAddress = addressService.getDefaultAddress(customer);
        if (defaultAddress != null) {

            shippingRate = shipService.getShippingRateForAddress(defaultAddress);
        } else {

            shippingRate = shipService.getShippingRateForCustomer(customer);
        }

        List<CartItem> cartItems = cartService.cartItemList(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);

        Order order = orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
        cartService.deleteByCustomer(customer);
        sendOrderConfirmationEmail(request, order);

        return "checkout/order_completed";
    }

    private void sendOrderConfirmationEmail(HttpServletRequest request, Order order) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
        mailSender.setDefaultEncoding("utf-8");

        String toAddress = order.getCustomer().getEmail();
        String subject = emailSettings.getOrderConfirmationSubject();
        String content = emailSettings.getOrderConfirmationContent();

        subject = subject.replace("[[orderId]]", String.valueOf(order.getId()));

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        DateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss E, dd MMM yyyy");
        String orderTime = dateFormatter.format(order.getOrderTime());

        CurrencySettingBag currencySettings = settingService.getCurrencySettings();
        String totalAmount = Utility.formatCurrency(order.getTotal(), currencySettings);

        content = content.replace("[[name]]", order.getCustomer().getFullName());
        content = content.replace("[[orderId]]", String.valueOf(order.getId()));
        content = content.replace("[[orderTime]]", orderTime);
        content = content.replace("[[shippingAddress]]", order.getShippingAddress());
        content = content.replace("[[total]]", totalAmount);
        content = content.replace("[[paymentMethod]]", order.getPaymentMethod().toString());

        helper.setText(content, true);
        mailSender.send(message);
    }
}
