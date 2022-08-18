package com.example.eduwithbe.Volunteer.repository;

import com.example.eduwithbe.Volunteer.domain.VolunteerScrapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerScrapRepository extends JpaRepository<VolunteerScrapEntity, Long> {
}
