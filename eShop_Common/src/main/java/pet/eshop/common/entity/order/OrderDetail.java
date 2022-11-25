package pet.eshop.common.entity.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pet.eshop.common.entity.IdBasedEntity;
import pet.eshop.common.entity.product.Product;

import javax.persistence.*;


@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
public class OrderDetail extends IdBasedEntity {

    private int quantity;

    private float productCost;

    private float shippingCost;

    private float unitPrice;

    private float subtotal;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
