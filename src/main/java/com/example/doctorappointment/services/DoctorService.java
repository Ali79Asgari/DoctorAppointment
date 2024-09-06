package com.example.doctorappointment.services;

import com.example.doctorappointment.entities.Appointment;
import com.example.doctorappointment.entities.Doctor;
import com.example.doctorappointment.entities.enums.AppointmentStatus;
import com.example.doctorappointment.repositories.AppointmentRepository;
import com.example.doctorappointment.repositories.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public Doctor createDoctor(Doctor doctor) {
        log.info("Creating new doctor: {}", doctor.getName());
        return doctorRepository.save(doctor);
    }

    public Doctor getDoctorById(UUID id) {
        log.info("Fetching doctor with id: {}", id);
        return doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + id));
    }

    public List<Doctor> getAllDoctors() {
        log.info("Fetching all doctors");
        return doctorRepository.findAll();
    }

    @Transactional
    public Doctor updateDoctor(UUID id, Doctor doctorDetails) {
        log.info("Updating doctor with id: {}", id);
        Doctor doctor = getDoctorById(id);
        doctor.setName(doctorDetails.getName());
        return doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(UUID id) {
        log.info("Deleting doctor with id: {}", id);
        doctorRepository.deleteById(id);
    }

    @Transactional
    public void addOpenTimes(UUID doctorId, LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Adding open times for doctor with id: {}", doctorId);
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        LocalDateTime currentSlotStart = startTime;
        while (currentSlotStart.plusMinutes(30).isBefore(endTime) || currentSlotStart.plusMinutes(30).isEqual(endTime)) {
            Appointment appointment = Appointment.builder()
                    .doctor(doctor)
                    .startTime(currentSlotStart)
                    .endTime(currentSlotStart.plusMinutes(30))
                    .status(AppointmentStatus.OPEN)
                    .build();
            appointmentRepository.save(appointment);
            currentSlotStart = currentSlotStart.plusMinutes(30);
        }
    }

    public List<Appointment> getAppointments(UUID doctorId, LocalDateTime date, boolean onlyOpen) {
        log.info("Getting all appointments for doctor with id: {}", doctorId);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
        LocalDateTime endOfDay = date.with(LocalTime.MAX);

        if (onlyOpen) {
            return appointmentRepository.findByDoctorAndStatusAndStartTimeBetween(
                    doctor, AppointmentStatus.OPEN, date, endOfDay);
        } else {
            return appointmentRepository.findByDoctorAndStartTimeBetween(doctor, date, endOfDay);
        }
    }

    @Transactional
    public void deleteOpenAppointment(UUID doctorId, UUID appointmentId) {
        log.info("Getting all appointments for doctor with id: {}", doctorId);
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        if (!appointment.getDoctor().getId().equals(doctorId)) {
            throw new IllegalArgumentException("Appointment does not belong to this doctor");
        }

        if (appointment.getStatus() != AppointmentStatus.OPEN) {
            throw new IllegalStateException("Cannot delete a non-open appointment");
        }

        appointmentRepository.delete(appointment);
    }
}
