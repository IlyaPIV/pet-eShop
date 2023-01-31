package pet.eshop.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pet.eshop.common.entity.Customer;
import pet.eshop.common.exception.CustomerNotFoundException;
import pet.eshop.common.exception.OrderNotFoundException;
import pet.eshop.customer.CustomerService;
import pet.eshop.util.Utility;

import javax.servlet.http.HttpServletRequest;

@RestController
public class OrderRestController {
    @Autowired private OrderService orderService;
    @Autowired private CustomerService customerService;

    @PostMapping("/orders/return")
    public ResponseEntity<?> handleOrderReturnRequest(@RequestBody OrderReturnRequest returnRequest,
                                                      HttpServletRequest servletRequest){

        Customer customer;
        try {
             customer = getAuthenticatedCustomer(servletRequest);
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>("Authentication required", HttpStatus.BAD_REQUEST);
        }

        try {
            orderService.setOrderReturnRequested(returnRequest, customer);
        } catch (OrderNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new OrderReturnResponse(returnRequest.getOrderId()), HttpStatus.OK);
    }


    public Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String customerEmail = Utility.getEmailOfAuthenticatedCustomer(request);
        if (customerEmail == null) {
            throw new CustomerNotFoundException("No authenticated customer");
        }
        return customerService.getCustomerByEmail(customerEmail);
    }
}
