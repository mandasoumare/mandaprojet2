package com.example.mandaprojet.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Data
@Entity
public class Suspect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long evtId;
    private String name;
    private String firstname;
    private String identityDocument;
    private String identityString;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate evtDate;

    private Double lat;
    private Double lng;
    private Long rusId;
    private String remarqueText;
    private String imagePath;
    private String uniqueToken;
    private int statut;
    @Column(length = 255)
    private String prediction;
}

