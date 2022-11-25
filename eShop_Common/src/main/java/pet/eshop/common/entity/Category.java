package pet.eshop.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category extends IdBasedEntity{

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

    @Column(name = "all_parent_ids", length = 256, nullable = true)
    private String allParentsIDs;

    @OneToMany(mappedBy = "parent")
    @OrderBy("name asc")
    private Set<Category> children = new HashSet<>();

    /*
     * КОНСТРУКТОРЫ
     */

    public Category(Integer id) {
        this.id = id;
    }

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(Integer id, String name, String alias) {
        this(id, name);
        this.alias = alias;
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
     * МЕТОДЫ
     */

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", enabled=" + enabled +
                ", parent=" + parent +
                '}';
    }

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
        catCopy.setHasChildren(!category.getChildren().isEmpty());

        return catCopy;
    }

    public static Category copyFull(Category category, String prefix) {
        Category catCopy = copyFull(category);
        catCopy.setName(prefix + category.getName());


        return catCopy;
    }

    @Transient
    public String getImagePath(){
        if (id == null || image.equals("default.png")) return "/images/image-thumbnail.png";
            else return "/category-images/" + this.id + "/" + this.image;
    }

    @Transient
    public boolean hasChildren;

    public boolean isHasChildren(){
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren){
        this.hasChildren = hasChildren;
    }

}
