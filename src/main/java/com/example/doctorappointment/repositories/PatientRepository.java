package com.example.doctorappointment.repositories;

import com.example.doctorappointment.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    List<Patient> findByPhoneNumber(String phoneNumber);
}
