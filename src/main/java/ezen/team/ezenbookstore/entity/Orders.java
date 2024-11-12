package ezen.team.ezenbookstore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany
    @JoinColumn(name = "orderitem_id", referencedColumnName = "id")
    private List<OrderItem> orderItems;

    @OneToOne
    @JoinColumn(name = "delivery_id", referencedColumnName = "id")
    private Delivery delivery;

    @OneToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

    @Column(name = "orders_date")
    @CreationTimestamp
    private Timestamp orderDate;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "status")
    private Byte status;



}
