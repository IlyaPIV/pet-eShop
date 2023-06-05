package pet.eshop.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pet.eshop.common.Constants;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
public class Brand extends IdBasedEntity{

    @Column(name = "name", nullable = false, length = 45, unique = true)
    private String name;

    @Column(name = "logo", nullable = false, length = 128)
    private String logo;

    @ManyToMany
    @JoinTable(name = "brands_categories",
                joinColumns = @JoinColumn(name = "brand_id"),
                inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    /*
     * КОНСТРУКТОРЫ
     */

    public Brand(String name) {
        this.name = name;
        this.logo = "brand-logo.png";
    }

    public Brand(Integer id, String name) {
        this(name);
        this.id = id;
    }

    public Brand(String name, String logo){
        this.name = name;
        this.logo = logo;
    }

    public Brand(String name, Set<Category> categories){
        this(name);
        this.categories = categories;
    }

    public Brand(String name, String logo, Set<Category> categories){
        this(name, logo);
        this.categories = categories;
    }


    /*
     * МЕТОДЫ
     */

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", categories=" + categories +
                '}';
    }

    public void addCategory(Category category){
        categories.add(category);
    }

    @Transient
    public String getLogoPath(){
        if (id == null || logo.equals("brand-logo.png")) return Constants.S3_BASE_URI + "/images/image-thumbnail.png";
        else return Constants.S3_BASE_URI + "/brand-logos/" + this.id + "/" + this.logo;
    }
}
