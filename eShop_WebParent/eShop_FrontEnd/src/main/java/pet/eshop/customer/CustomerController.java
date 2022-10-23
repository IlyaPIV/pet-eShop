package pet.eshop.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pet.eshop.common.entity.Country;
import pet.eshop.common.entity.Customer;
import pet.eshop.security.CustomerUserDetails;
import pet.eshop.security.oauth.CustomerOAuth2User;
import pet.eshop.setting.EmailSettingBag;
import pet.eshop.setting.SettingService;
import pet.eshop.util.Utility;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SettingService settingService;

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        List<Country> listAllCountries = customerService.listAllCountries();
        model.addAttribute("pageTitle", "Customer registration");
        model.addAttribute("listCountries", listAllCountries);
        model.addAttribute("customer", new Customer());

        return "register/register_form";
    }

    @PostMapping("/create_customer")
    public String createCustomer(Customer customer, Model model,
                                 HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        customerService.registerCustomer(customer);
        sendVerificationEmail(request, customer);

        model.addAttribute("pageTitle", "Registration Succeeded!");

        return "register/register_success";
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String toAddress = customer.getEmail();
        String subject = emailSettings.getCustomerVerifySubject();
        String content = emailSettings.getCustomerVerifyContent();
        String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", customer.getFullName());
        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code, Model model){
        boolean verified = customerService.verify(code);

        return "register/" + (verified ? "verify_success" : "verify_fail");
    }

    @GetMapping("/account_details")
    public String viewAccountDetails(Model model, HttpServletRequest request){

        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        Customer customer = customerService.getCustomerByEmail(email);
        List<Country> listAllCountries = customerService.listAllCountries();

        model.addAttribute("listCountries", listAllCountries);
        model.addAttribute("customer", customer);

        return "customer/account_form";
    }

    @PostMapping("/update_account_details")
    public String updateAccountDetails(Model model, Customer customer,
                                       RedirectAttributes redirectAttributes,
                                       HttpServletRequest request){
        customerService.update(customer);
        
        updateNameForAuthenticatedCustomer(request, customer);

        redirectAttributes.addFlashAttribute("message", "Your account details have been updated.");
        return "redirect:/account_details";
    }

    private void updateNameForAuthenticatedCustomer(HttpServletRequest request,
                                                    Customer customer) {
        Object principal = request.getUserPrincipal();

        if (principal instanceof UsernamePasswordAuthenticationToken
                || principal instanceof RememberMeAuthenticationToken) {
            CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal);
            Customer authCustomer = userDetails.getCustomer();
            authCustomer.setFirstName(customer.getFirstName());
            authCustomer.setLastName(customer.getLastName());
        } else if (principal instanceof OAuth2AuthenticationToken token){
            CustomerOAuth2User oAuth2User = (CustomerOAuth2User) token.getPrincipal();
            oAuth2User.setFullName(customer.getFullName());
        }
    }

    private CustomerUserDetails getCustomerUserDetailsObject(Object principal){
        CustomerUserDetails details = null;
        if (principal instanceof UsernamePasswordAuthenticationToken token) {
            details = (CustomerUserDetails) token.getPrincipal();
        } else if (principal instanceof RememberMeAuthenticationToken token){
            details = (CustomerUserDetails) token.getPrincipal();
        }

        return details;
    }
}
