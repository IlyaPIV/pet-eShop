package pet.eshop.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "shipping_rates")
@NoArgsConstructor
@Getter
@Setter
public class ShippingRate extends IdBasedEntity{

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;
    @Column(name = "state", nullable = false, length = 45)
    private String state;
    @Column(name = "rate", nullable = false)
    private float rate;
    @Column(name = "days")
    private int days;
    @Column(name = "cod_supported")
    private boolean codSupported;

    /*
    * CONSTRUCTORS
     */



    /*
    * METHODS
     */


}
