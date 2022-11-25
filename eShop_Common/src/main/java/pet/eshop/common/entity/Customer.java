package pet.eshop.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@Getter
@Setter
public class Customer extends AbstractAddressWithCountry{

    @Column(name = "email", unique = true, nullable = false, length = 45)
    private String email;

    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Column(name = "created_time")
    private Date createdTime;

    private boolean enabled;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type", length = 10)
    private AuthenticationType authenticationType;

    @Column(name = "reset_password_token", length = 30)
    private String resetPasswordToken;

    /*
     *
     */

    public Customer(Integer id) {
        this.id = id;
    }

    /*
    *
     */

    @Override
    public String toString() {
        return "Customer{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }

    @Transient
    public String getAddress(){
        return getAddressString();
    }
}
