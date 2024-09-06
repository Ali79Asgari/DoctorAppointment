package com.example.doctorappointment.services;

import com.example.doctorappointment.entities.Appointment;
import com.example.doctorappointment.repositories.AppointmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public Appointment createAppointment(Appointment appointment) {
        log.info("Creating new appointment for doctor: {} at time: {}",
                appointment.getDoctor().getId(), appointment.getStartTime());
        return appointmentRepository.save(appointment);
    }

    public Appointment getAppointmentById(UUID id) {
        log.info("Fetching appointment with id: {}", id);
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id: " + id));
    }

    public List<Appointment> getAllAppointments() {
        log.info("Fetching all appointments");
        return appointmentRepository.findAll();
    }

    @Transactional
    public Appointment updateAppointment(UUID id, Appointment appointmentDetails) {
        log.info("Updating appointment with id: {}", id);
        Appointment appointment = getAppointmentById(id);
        appointment.setDoctor(appointmentDetails.getDoctor());
        appointment.setPatient(appointmentDetails.getPatient());
        appointment.setStartTime(appointmentDetails.getStartTime());
        appointment.setEndTime(appointmentDetails.getEndTime());
        appointment.setStatus(appointmentDetails.getStatus());
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public void deleteAppointment(UUID id) {
        log.info("Deleting appointment with id: {}", id);
        appointmentRepository.deleteById(id);
    }
}
