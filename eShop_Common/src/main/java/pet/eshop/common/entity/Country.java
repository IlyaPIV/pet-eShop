package pet.eshop.common.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "countries")
public class Country{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    public Country() {
    }

    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Country(Integer countryId, String countryName, String countryCode) {
        this.id = countryId;
        this.name = countryName;
        this.code = countryCode;
    }

    /*
     *
     */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
//
//    public Set<State> getStates() {
//        return states;
//    }
//
//    public void setStates(Set<State> states) {
//        this.states = states;
//    }

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
