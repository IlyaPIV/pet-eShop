package pet.eshop.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractAddressWithCountry extends AbstractAddress{
    @ManyToOne
    @JoinColumn(name = "country_id")
    protected Country country;

    protected String getAddressString() {
        StringBuilder address = new StringBuilder(firstName);

        if (lastName!= null && !lastName.isEmpty()) address.append(" ").append(lastName);
        if (!addressLine1.isBlank()) address.append(", ").append(addressLine1);
        if (addressLine2 != null && !addressLine2.isEmpty()) address.append(", ").append(addressLine2);
        if (!city.isBlank()) address.append(", ").append(city);
        if (state != null && !state.isBlank()) address.append(", ").append(state);
        address.append(", ").append(country.getName());
        if (!postalCode.isBlank()) address.append(". Postal Code: ").append(postalCode);
        if (!phoneNumber.isBlank()) address.append(". Phone Number: ").append(phoneNumber);

        return address.toString();
    }
}
