package pet.eshop.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import pet.eshop.common.entity.Customer;
import pet.eshop.common.exception.CustomerNotFoundException;
import pet.eshop.customer.CustomerService;
import pet.eshop.security.oauth.CustomerOAuth2User;
import pet.eshop.setting.CurrencySettingBag;
import pet.eshop.setting.EmailSettingBag;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;

@Service
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

    public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request){
        Object principal = request.getUserPrincipal();
        if (principal == null) return null;

        String customerEmail = null;

        if (principal instanceof UsernamePasswordAuthenticationToken
                || principal instanceof RememberMeAuthenticationToken) {
            customerEmail = request.getUserPrincipal().getName();
        } else if (principal instanceof OAuth2AuthenticationToken token){
            CustomerOAuth2User oAuth2User = (CustomerOAuth2User) token.getPrincipal();
            customerEmail = oAuth2User.getEmail();
        }

        return customerEmail;
    }

    public static String formatCurrency(float amount, CurrencySettingBag currencySettings) {
        String symbol = currencySettings.getSymbol();
        String symbolPosition = currencySettings.getSymbolPosition();
        String decimalPointType = currencySettings.getDecimalPointType();
        String thousandsPointType = currencySettings.getThousandsPointType();
        int decimalDigits = currencySettings.getDecimalDigits();

        StringBuilder pattern = new StringBuilder(symbolPosition.equals("Before price") ? symbol : "");
        pattern.append("###,###");
        if (decimalDigits > 0 ){
            pattern.append(".");
            pattern.append("#".repeat(decimalDigits));
        }
        pattern.append(symbolPosition.equals("After price") ? symbol : "");

        char thousandSeparator = switch (thousandsPointType) {
            case "POINT" -> '.';
            case "COMMA" -> ',';
            case "WHITESPACE" -> ' ';
            default -> Character.MIN_VALUE;
        };

        char decimalSeparator = decimalPointType.equals("POINT") ? '.' : ',';

        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
        decimalFormatSymbols.setGroupingSeparator(thousandSeparator);

        DecimalFormat formatter = new DecimalFormat(pattern.toString(), decimalFormatSymbols);

        return formatter.format(amount);
    }


}
