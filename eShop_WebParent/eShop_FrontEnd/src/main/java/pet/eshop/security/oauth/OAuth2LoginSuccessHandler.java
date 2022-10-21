package pet.eshop.security.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pet.eshop.common.entity.AuthenticationType;
import pet.eshop.common.entity.Customer;
import pet.eshop.customer.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private CustomerService customerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        CustomerOAuth2User oAuth2User = (CustomerOAuth2User) authentication.getPrincipal();

        String name = oAuth2User.getName();
        String email = oAuth2User.getEmail();
        String countryCode = request.getLocale().getCountry();

        AuthenticationType authenticationType = getAuthenticationType(oAuth2User.getClientName());

        Customer customer = customerService.getCustomerByEmail(oAuth2User.getEmail());
        if (customer == null) {
            customerService.addNewCustomerOAuthLogin(name, email, countryCode, authenticationType);
        } else {
            customerService.updateAuthenticationType(customer, authenticationType);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private AuthenticationType getAuthenticationType(String clientName){
        return switch (clientName) {
            case "Google" -> AuthenticationType.GOOGLE;
            case "Facebook" -> AuthenticationType.FACEBOOK;
            default -> AuthenticationType.DATABASE;
        };
    }
}
