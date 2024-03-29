package pet.eshop.common.entity.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pet.eshop.common.entity.AbstractAddress;
import pet.eshop.common.entity.Address;
import pet.eshop.common.entity.Customer;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("updatedTime ASC")
    private List<OrderTrack> orderTracks = new ArrayList<>();

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

    @Transient
    public String getShippingAddress(){
        StringBuilder address = new StringBuilder(firstName);

        if (lastName!= null && !lastName.isEmpty()) address.append(" ").append(lastName);
        if (!addressLine1.isBlank()) address.append(", ").append(addressLine1);
        if (addressLine2 != null && !addressLine2.isEmpty()) address.append(", ").append(addressLine2);
        if (!city.isBlank()) address.append(", ").append(city);
        if (state != null && !state.isBlank()) address.append(", ").append(state);
        address.append(", ").append(country);
        if (!postalCode.isBlank()) address.append(". Postal Code: ").append(postalCode);
        if (!phoneNumber.isBlank()) address.append(". Phone Number: ").append(phoneNumber);

        return address.toString();
    }

    @Transient
    public String getDeliverDateOnForm(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormatter.format(this.deliverDate);
    }

    public void setDeliverDateOnForm(String dateString){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.deliverDate = dateFormatter.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Transient
    public String getRecipientName() {
        String name = firstName;
        if (lastName != null && !lastName.isEmpty()) name += " " + lastName;
        return name;
    }

    @Transient
    public String getRecipientAddress() {
        String address = addressLine1;

        if (addressLine2 != null && !addressLine2.isEmpty()) address += ", " + addressLine2;

        if (!city.isEmpty()) address += ", " + city;

        if (state != null && !state.isEmpty()) address += ", " + state;

        address += ", " + country;

        if (!postalCode.isEmpty()) address += ". " + postalCode;

        return address;
    }

    @Transient
    public boolean isCOD() {
        return paymentMethod.equals(PaymentMethod.COD);
    }

    @Transient
    public boolean isProcessing() {
        return hasStatus(OrderStatus.PROCESSING);
    }

    @Transient
    public boolean isPicked() {
        return hasStatus(OrderStatus.PICKED);
    }

    @Transient
    public boolean isShipping() {
        return hasStatus(OrderStatus.SHIPPING);
    }

    @Transient
    public boolean isDelivered() {
        return hasStatus(OrderStatus.DELIVERED);
    }

    @Transient
    public boolean isReturnRequested() {
        return hasStatus(OrderStatus.RETURN_REQUESTED);
    }

    @Transient
    public boolean isReturned() {
        return hasStatus(OrderStatus.RETURNED);
    }

    public boolean hasStatus(OrderStatus status) {
        for (OrderTrack aTrack : orderTracks) {
            if (aTrack.getStatus().equals(status)) {
                return true;
            }
        }

        return false;
    }

    @Transient
    public String getProductNames() {
        StringBuilder productNames = new StringBuilder();

        productNames = new StringBuilder("<ul>");

        for (OrderDetail detail : orderDetails) {
            productNames.append("<li>").append(detail.getProduct().getShortName()).append("</li>");
        }

        productNames.append("</ul>");

        return productNames.toString();
    }
}
