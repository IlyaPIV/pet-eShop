package pet.eshop.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
public class Address extends AbstractAddressWithCountry{

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Column(name = "default_for_shipping")
    private boolean defaultForShipping;

    /*
    * КОНСТРУКТОРЫ
     */


    /*
    * МЕТОДЫ
     */

    @Override
    public String toString() {
        return getAddressString();
    }
}
