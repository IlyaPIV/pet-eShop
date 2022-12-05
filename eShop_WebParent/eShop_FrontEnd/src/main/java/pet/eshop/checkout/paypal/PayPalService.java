package pet.eshop.checkout.paypal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pet.eshop.setting.PaymentSettingBag;
import pet.eshop.setting.SettingService;

import java.util.List;

@Component
public class PayPalService {
    @Autowired
    private SettingService settingService;

    private static final String GET_ORDER_API = "/v2/checkout/orders/";

    public boolean validateOrder(String orderId) throws PayPalApiException {

        PayPalOrderResponse responseBody = getPayPalOrderDetails(orderId);

        return responseBody.validate(orderId);
    }

    private PayPalOrderResponse getPayPalOrderDetails(String orderId) throws PayPalApiException {
        ResponseEntity<PayPalOrderResponse> response = getPayPalOrderResponse(orderId);
        HttpStatus statusCode = response.getStatusCode();
        if (!statusCode.equals(HttpStatus.OK)) {
            switch (statusCode) {
                case NOT_FOUND -> throw new PayPalApiException("Order ID not found");
                case BAD_REQUEST -> throw new PayPalApiException("Bad request to PayPal Checkout API");
                case INTERNAL_SERVER_ERROR -> throw new PayPalApiException("PayPal server error");
                default -> throw new PayPalApiException("PayPal returned non-OK status code");
            }
        }

        return response.getBody();
    }

    private ResponseEntity<PayPalOrderResponse> getPayPalOrderResponse(String orderId) {

        PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
        String baseUrl = paymentSettings.getUrl();
        String requestUrl = baseUrl + GET_ORDER_API + orderId;
        String clientID = paymentSettings.getClientID();
        String clientSecret = paymentSettings.getClientSecret();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language", "en_US");
        headers.setBasicAuth(clientID, clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(requestUrl, HttpMethod.GET,
                                            request, PayPalOrderResponse.class);
    }
}
