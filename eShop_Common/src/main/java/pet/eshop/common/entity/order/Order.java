package pet.eshop.common.entity.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pet.eshop.common.entity.AbstractAddress;
import pet.eshop.common.entity.Address;
import pet.eshop.common.entity.Customer;
import pet.eshop.common.entity.IdBasedEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order extends AbstractAddress {
    @Column(name = "country", nullable = false, length = 45)
    private String country;

    private Date orderTime;

    private float shippingCost;

    private float productsCost;

    private float subtotal;

    private float tax;

    private float total;

    private int deliverDays;

    private Date deliverDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    /*
    * конструкторы
     */

    /*
    * методы
     */

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer +
                ", status=" + status +
                ", subtotal=" + subtotal +
                ", paymentMethod=" + paymentMethod +
                '}';
    }

    public void copyAddressFromCustomer(Customer customer){
        setFirstName(customer.getFirstName());
        setLastName(customer.getLastName());
        setPhoneNumber(customer.getPhoneNumber());
        setAddressLine1(customer.getAddressLine1());
        setAddressLine2(customer.getAddressLine2());
        setCity(customer.getCity());
        setCountry(customer.getCountry().getName());
        setState(customer.getState());
        setPostalCode(customer.getPostalCode());
    }

    @Transient
    public String getDestination(){
        String destination = city + ", ";
        if (state != null && !state.isEmpty()) destination += state + ", ";
        destination += country;

        return destination;
    }

    public void copyShippingAddress(Address address) {
        setFirstName(address.getFirstName());
        setLastName(address.getLastName());
        setPhoneNumber(address.getPhoneNumber());
        setAddressLine1(address.getAddressLine1());
        setAddressLine2(address.getAddressLine2());
        setCity(address.getCity());
        setCountry(address.getCountry().getName());
        setState(address.getState());
        setPostalCode(address.getPostalCode());
    }
}
