package com.example.mandaprojet.Repositoty;

import com.example.mandaprojet.entity.Suspect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuspectRepository extends JpaRepository<Suspect, Long> {
    List<Suspect> findAllByOrderByStatutAscEvtDateDesc();
    List<Suspect> findByPrediction(String prediction);

}



