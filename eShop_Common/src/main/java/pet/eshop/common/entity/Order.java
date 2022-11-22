package pet.eshop.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;
    @Column(name = "address_line1", nullable = false, length = 64)
    private String addressLine1;
    @Column(name = "address_line2", length = 64)
    private String addressLine2;
    @Column(name = "city", nullable = false, length = 45)
    private String city;
    @Column(name = "state", nullable = false, length = 45)
    private String state;
    @Column(name = "country", nullable = false, length = 45)
    private String country;
    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;

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
}
