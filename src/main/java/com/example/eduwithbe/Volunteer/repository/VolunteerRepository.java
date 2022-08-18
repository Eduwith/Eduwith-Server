package com.example.eduwithbe.Volunteer.repository;

import com.example.eduwithbe.Volunteer.domain.VolunteerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerRepository extends JpaRepository<VolunteerEntity, Long> {
}
