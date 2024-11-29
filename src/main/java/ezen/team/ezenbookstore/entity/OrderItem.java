package ezen.team.ezenbookstore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder(toBuilder = true)
@Table(name = "orderitem")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "orders_id", referencedColumnName = "id")
    private Orders orders;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "status")
    private Byte status;

}
