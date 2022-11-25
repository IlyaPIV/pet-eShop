package pet.eshop.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor
public class Country extends IdBasedEntity{

    @Column(length = 64, nullable = false, unique = true)
    private String name;

    @Column(length = 5, nullable = false, unique = true)
    private String code;

    @OneToMany(mappedBy = "country")
    @OrderBy("name asc")
    private Set<State> states = new HashSet<>();

    /*
     *
     */

    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Country(Integer countryId, String countryName, String countryCode) {
        this.id = countryId;
        this.name = countryName;
        this.code = countryCode;
    }

    public Country(Integer countryId) {
        this.id = countryId;
    }


    /*
     *
     */

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
