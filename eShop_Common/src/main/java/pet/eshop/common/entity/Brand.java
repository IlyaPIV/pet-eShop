package pet.eshop.common.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    public Brand() {
    }

    public Brand(String name) {
        this.name = name;
        this.logo = "brand-logo.png";
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
     * ГЕТТЕРЫ И СЕТТЕРЫ
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
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
        if (id == null || logo.equals("brand-logo.png")) return "/images/image-thumbnail.png";
        else return "/brand-logos/" + this.id + "/" + this.logo;
    }
}
