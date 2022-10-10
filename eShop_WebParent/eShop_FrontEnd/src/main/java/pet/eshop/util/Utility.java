package pet.eshop.util;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import pet.eshop.setting.EmailSettingBag;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

public class Utility {

    public static String getSiteURL(HttpServletRequest request){
        String siteURL = request.getRequestURL().toString();

        return siteURL.replace(request.getServletPath(), "");
    }

    public static JavaMailSenderImpl prepareMailSender(EmailSettingBag emailSettings){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailSettings.getHost());
        mailSender.setPort(emailSettings.getPort());
        mailSender.setUsername(emailSettings.getUsername());
        mailSender.setPassword(emailSettings.getPassword());

        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.smtp.auth", emailSettings.getSmtpAuth());
        mailProperties.setProperty("mail.smtp.starttls.enable", emailSettings.getSmtpSecured());

        mailSender.setJavaMailProperties(mailProperties);

        return mailSender;
    }
}
