package com.example.doctorappointment.services;

import com.example.doctorappointment.entities.Appointment;
import com.example.doctorappointment.entities.Patient;
import com.example.doctorappointment.entities.enums.AppointmentStatus;
import com.example.doctorappointment.repositories.AppointmentRepository;
import com.example.doctorappointment.repositories.PatientRepository;
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
public class PatientService {
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public Patient createPatient(Patient patient) {
        log.info("Creating new patient: {}", patient.getName());
        return patientRepository.save(patient);
    }

    public Patient getPatientById(UUID id) {
        log.info("Fetching patient with id: {}", id);
        return patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));
    }

    public List<Patient> getAllPatients() {
        log.info("Fetching all patients");
        return patientRepository.findAll();
    }

    @Transactional
    public Patient updatePatient(UUID id, Patient patientDetails) {
        log.info("Updating patient with id: {}", id);
        Patient patient = getPatientById(id);
        patient.setName(patientDetails.getName());
        patient.setPhoneNumber(patientDetails.getPhoneNumber());
        return patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(UUID id) {
        log.info("Deleting patient with id: {}", id);
        patientRepository.deleteById(id);
    }

    public List<Appointment> getOpenAppointments(LocalDateTime date) {
        LocalDateTime endOfDay = date.with(LocalTime.MAX);
        return appointmentRepository.findByStatusAndStartTimeBetween(
                AppointmentStatus.OPEN, date, endOfDay);
    }

    @Transactional
    public void takeAppointment(UUID appointmentId, String patientName, String phoneNumber) {
        if (patientName == null || patientName.trim().isEmpty() || phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name and phone number are required");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.OPEN) {
            throw new IllegalStateException("Appointment is not available");
        }

        Patient patient = patientRepository.findByPhoneNumber(phoneNumber)
                .stream().findFirst()
                .orElseGet(() -> {
                    Patient newPatient = Patient.builder()
                            .name(patientName)
                            .phoneNumber(phoneNumber)
                            .build();
                    return patientRepository.save(newPatient);
                });

        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.TAKEN);
        appointmentRepository.save(appointment);
    }

    public List<Appointment> getPatientAppointments(String phoneNumber) {
        return appointmentRepository.findByPatientPhoneNumber(phoneNumber);
    }
}
