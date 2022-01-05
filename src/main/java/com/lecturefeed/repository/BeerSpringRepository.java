package com.lecturefeed.repository;

import com.lecturefeed.entity.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeerSpringRepository extends JpaRepository<Beer, Long> {}