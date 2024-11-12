package ezen.team.ezenbookstore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "publish_date")
    private Timestamp publishDate;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "ifkr")
    private Byte ifkr;

    @Column(name = "price")
    private Integer price;

    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private SubCategory subcategory;

    @Column(name = "count")
    private Long count;

    @Column(name = "discount")
    private Integer discount;

    @OneToOne
    @JoinColumn(name = "bookdescription_id", referencedColumnName = "id")
    private BookDescription bookdescription;

}
