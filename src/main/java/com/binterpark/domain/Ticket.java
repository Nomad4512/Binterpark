package com.binterpark.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "venue", nullable = false)
    private String venue; // 장소

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "ticket_price")
    private int price;

    @Column(name = "discounted_ticket_price")
    private int discountedPrice;

    @Column(name = "availableQuantity", nullable = false)
    private int availableQuantity;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "sold_count", nullable = false)
    private int soldCount;

    @Column(name = "review_count", nullable = false)
    private int reviewCount;

    @Column(name = "event_description")
    private String eventDescription;

    @Column(name = "event_organizer")
    private String eventOrganizer;

}
