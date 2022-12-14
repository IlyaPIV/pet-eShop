package pet.eshop.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.eshop.checkout.CheckoutInfo;
import pet.eshop.common.entity.Address;
import pet.eshop.common.entity.CartItem;
import pet.eshop.common.entity.Customer;
import pet.eshop.common.entity.order.Order;
import pet.eshop.common.entity.order.OrderDetail;
import pet.eshop.common.entity.order.OrderStatus;
import pet.eshop.common.entity.order.PaymentMethod;
import pet.eshop.common.entity.product.Product;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    @Autowired private OrderRepository repository;

    public Order createOrder(Customer customer, Address address, List<CartItem> cartItems,
                             PaymentMethod paymentMethod, CheckoutInfo checkoutInfo){
        Order newOrder = new Order();
        fillNewOrderCommonData(paymentMethod, checkoutInfo, newOrder);
        newOrder.setCustomer(customer);

        if (address == null) {
            newOrder.copyAddressFromCustomer(customer);
        } else {
            newOrder.copyShippingAddress(address);
        }

        Set<OrderDetail> orderDetails = newOrder.getOrderDetails();
        for (CartItem cartItem:
            cartItems) {

            OrderDetail orderDetail = new OrderDetail();
            fillNewOrderDetailFromCartItem(newOrder, cartItem, orderDetail);

            orderDetails.add(orderDetail);
        }
        newOrder.setOrderDetails(orderDetails);

        return repository.save(newOrder);
    }

    private static void fillNewOrderDetailFromCartItem(Order newOrder, CartItem cartItem, OrderDetail orderDetail) {
        Product product = cartItem.getProduct();
        orderDetail.setOrder(newOrder);
        orderDetail.setProduct(product);
        orderDetail.setQuantity(cartItem.getQuantity());
        orderDetail.setUnitPrice(product.getDiscountPrice());
        orderDetail.setProductCost(product.getCost() * cartItem.getQuantity());
        orderDetail.setSubtotal(cartItem.getSubtotal());
        orderDetail.setShippingCost(cartItem.getShippingCost());
    }

    private static void fillNewOrderCommonData(PaymentMethod paymentMethod, CheckoutInfo checkoutInfo, Order newOrder) {
        if (paymentMethod.equals(PaymentMethod.PAYPAL)) {
            newOrder.setStatus(OrderStatus.PAID);
        } else {
            newOrder.setStatus(OrderStatus.NEW);
        }
        newOrder.setOrderTime(new Date());
        newOrder.setProductsCost(checkoutInfo.getProductCost());
        newOrder.setSubtotal(checkoutInfo.getProductTotal());
        newOrder.setShippingCost(checkoutInfo.getShippingCostTotal());
        newOrder.setTax(0.0f);
        newOrder.setTotal(checkoutInfo.getPaymentTotal());
        newOrder.setPaymentMethod(paymentMethod);
        newOrder.setDeliverDays(checkoutInfo.getDeliverDays());
        newOrder.setDeliverDate(checkoutInfo.getDeliverDate());
    }
}
