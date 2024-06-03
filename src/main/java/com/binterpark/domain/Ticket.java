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
public class Ticket extends Product{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    @Column(name = "venue", nullable = false)
    private String venue; // 장소

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "event_organizer")
    private String eventOrganizer;

}
