package com.example.doctorappointment.repositories;

import com.example.doctorappointment.entities.Appointment;
import com.example.doctorappointment.entities.Doctor;
import com.example.doctorappointment.entities.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findByDoctorAndStatusAndStartTimeBetween(
            Doctor doctor,
            AppointmentStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Appointment> findByPatientPhoneNumber(String phoneNumber);

    List<Appointment> findByDoctorAndStartTimeBetween(
            Doctor doctor,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Appointment> findByStatusAndStartTimeBetween(
            AppointmentStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
