package com.example.mandaprojet.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
public class BaseImages {

        @Id
        private String id;  // format : "yyyyMMddHHmmss"
        private String nom;
        private String path;
        private String embedding; // éventuellement en JSON ou binaire compressé


        public void generateIdFromCurrentDateTime() {
            this.id = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        }
    }

