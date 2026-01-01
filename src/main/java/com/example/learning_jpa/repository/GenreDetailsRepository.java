package com.example.learning_jpa.repository;

import com.example.learning_jpa.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreDetailsRepository extends JpaRepository<Genre, Long> {
    List<Genre> findByStallId(Long stallId);
}
