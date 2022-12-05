package pet.eshop.checkout.paypal;

import org.junit.jupiter.api.Test;

import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class PayPalApiTests {
    private static final String BASE_URL = "https://api.sandbox.paypal.com";
    private static final String GET_ORDER_API = "/v2/checkout/orders/";
    private static final String CLIENT_ID = "AcqTHAhfG4eK6HPkNimxArUDUrl_EECLcQEM1UWLa0jtFcgGcehkPQS4y4IAF8Gn1zizw_dXaQgES45_";
    private static final String CLIENT_SECRET = "EAqcGtUTH6YqaQHEBXpPOooQPNVue1fKy2a62-v7TO85mHIUlBJ0NZNvyalpaxiKODO4oKYSGCtLKzl3";

    @Test
    public void testGetOrderDetails(){
        String orderId = "3AV61008HV278200U";
        String requestUrl = BASE_URL + GET_ORDER_API + orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language", "en_US");
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(requestUrl, HttpMethod.GET,
                                                                                request, PayPalOrderResponse.class);
        PayPalOrderResponse responseBody = response.getBody();
        System.out.println("Order ID: " + responseBody.getId());
        System.out.println("Validated: " + responseBody.validate(orderId));
    }
}
