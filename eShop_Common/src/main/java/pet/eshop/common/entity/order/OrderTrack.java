package pet.eshop.common.entity.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pet.eshop.common.entity.IdBasedEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "order_track")
@Getter
@Setter
@NoArgsConstructor
public class OrderTrack extends IdBasedEntity {

    @Column(length = 256)
    private String notes;

    private Date updatedTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
