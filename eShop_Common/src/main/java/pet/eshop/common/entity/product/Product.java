package pet.eshop.common.entity.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pet.eshop.common.entity.Brand;
import pet.eshop.common.entity.Category;
import pet.eshop.common.entity.IdBasedEntity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product extends IdBasedEntity {
    @Column(unique = true, length = 256, nullable = false)
    private String name;
    @Column(unique = true, length = 256, nullable = false)
    private String alias;
    @Column(name = "short_description", length = 512, nullable = false)
    private String shortDescription;
    @Column(name = "full_description",length = 4096, nullable = false)
    private String fullDescription;

    @Column(name = "created_time")
    private Date createdTime;
    @Column(name = "updated_time")
    private Date updatedTime;

    private boolean enabled;
    @Column(name = "in_stock")
    private boolean inStock;

    private float cost;
    private float price;
    @Column(name = "discount_percent")
    private float discountPercent;

    private float length;
    private float width;
    private float height;
    private float weight;

    @Column(name = "main_image", nullable = false)
    private String mainImage;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetails> details = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    /*
     *  КОНСТРУКТОРЫ
     */

    public Product(Integer id) {
        this.id = id;
    }

     /*
     *  МЕТОДЫ
     */

    @Override
    public String toString() {
        return "Product {" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public void addExtraImage(String imageName){
        this.images.add(new ProductImage(imageName, this));
    }

    @Transient
    public String getMainImagePath(){
        if (id == null || mainImage == null) return "/images/image-thumbnail.png";
        else return "/product-images/" + this.id + "/" + this.mainImage;
    }

    public void addDetail(String name, String value) {
        this.details.add(new ProductDetails(name, value, this));
    }

    public void addDetail(Integer id, String name, String value) {
        this.details.add(new ProductDetails(id, name, value, this));
    }

    public boolean containsImageName(String imageName) {
        for (ProductImage image : images) {
            if (image.getName().equals(imageName)) {
                return true;
            }
        }
        return false;
    }

    @Transient
    public String getShortName(){
        if (name.length()>70) {
            return name.substring(0, 70).concat("...");
        }
        return name;
    }

    @Transient
    public float getDiscountPrice(){
        if (discountPercent > 0) {
            return price * ((100 - discountPercent) / 100);
        }
        return this.price;
    }
}
