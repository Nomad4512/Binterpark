package com.binterpark.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@SuperBuilder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "productId")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "discounted_price")
    private int discountedPrice;

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "sold_count", nullable = false)
    private int soldCount;

    @Column(name = "review_count", nullable = false)
    private int reviewCount;

    @Column(name = "description")
    private String description;

    private String mainImage;

    private String specificImage;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<Wishlist> wishlists;

}
