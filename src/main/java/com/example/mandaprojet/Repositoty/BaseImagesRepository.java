package com.example.mandaprojet.Repositoty;


import com.example.mandaprojet.entity.BaseImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseImagesRepository extends JpaRepository<BaseImages, String> {
}

