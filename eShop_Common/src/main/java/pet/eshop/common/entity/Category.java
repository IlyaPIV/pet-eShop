package pet.eshop.common.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;
    @Column(length = 64, nullable = false, unique = true)
    private String alias;
    @Column(length = 128, nullable = false)
    private String image;

    private boolean enabled;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();

    /*
     * КОНСТРУКТОРЫ
     */

    public Category() {
    }

    public Category(Integer id) {
        this.id = id;
    }

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.image = "default.png";
    }

    public Category(String name, Category parent) {
        this(name);
        this.parent = parent;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }


    /*
     * МЕТОДЫ
     */

    public static Category copyIdAndName(Integer id, String name) {
        Category catCopy = new Category();
        catCopy.setName(name);
        catCopy.setId(id);

        return catCopy;
    }

    public static Category copyFull(Category category) {
        Category catCopy = new Category();
        catCopy.setName(category.getName());
        catCopy.setId(category.getId());
        catCopy.setImage(category.getImage());
        catCopy.setEnabled(category.isEnabled());
        catCopy.setAlias(category.getAlias());

        return catCopy;
    }

    public static Category copyFull(Category category, String prefix) {
        Category catCopy = copyFull(category);
        catCopy.setName(prefix + category.getName());

        return catCopy;
    }

    @Transient
    public String getImagePath(){
        return "/category-images/" + this.id + "/" + this.image;
    }

}
