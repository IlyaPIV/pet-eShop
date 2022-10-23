package pet.eshop.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pet.eshop.common.entity.Customer;
import pet.eshop.common.exception.CustomerNotFoundException;
import pet.eshop.setting.EmailSettingBag;
import pet.eshop.setting.SettingService;
import pet.eshop.util.Utility;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SettingService settingService;

    @GetMapping("/forgot_password")
    public String showRequestForm(){

        return "customer/forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processRequestForm(HttpServletRequest request, Model model){
        String email = request.getParameter("email");

        try {
            String token = customerService.updateResetPasswordToken(email);
            String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(link, email);

            model.addAttribute("message", "Check your email for reset password link");
        } catch (CustomerNotFoundException ex){
            model.addAttribute("error", ex.getMessage());
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", "Could not send email");
        }

        return "customer/forgot_password_form";
    }

    private void sendEmail(String link, String email) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String subject = "Here's the link to reset your password";
        String content = "<p> Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(email);
        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetForm(@Param("token") String token, Model model){
        Customer customer = customerService.getByResetPasswordToken(token);
        if (customer != null) {
            model.addAttribute("token", token);
            model.addAttribute("pageTitle", "Reset password");
        } else {
            model.addAttribute("message", "Invalid Token");
            model.addAttribute("pageTitle", "Invalid Token");

            return "message";
        }

        return "customer/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetForm(HttpServletRequest request, Model model){
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        try {
            customerService.updatePassword(token, password);
            model.addAttribute("title", "Reset Your Password");
            model.addAttribute("message", "You have successfully changed your password");
            model.addAttribute("pageTitle", "Success");
        } catch (CustomerNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("pageTitle", "Invalid Token");
        }
        return "message";
    }
}
