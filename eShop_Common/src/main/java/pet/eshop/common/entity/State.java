package pet.eshop.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "states")
@NoArgsConstructor
@Setter
@Getter
public class State extends IdBasedEntity{

    @Column(nullable = false, length = 64)
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    /*
     *
     */

    public State(String name, Country country) {
        this.name = name;
        this.country = country;
    }

    public State(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public State(Integer id, String name, Country country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    /*
     *
     */

}
