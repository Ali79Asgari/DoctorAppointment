package com.example.doctorappointment.controllers;

import com.example.doctorappointment.entities.Appointment;
import com.example.doctorappointment.entities.Patient;
import com.example.doctorappointment.services.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        return new ResponseEntity<>(patientService.createPatient(patient), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable UUID id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable UUID id, @Valid @RequestBody Patient patientDetails) {
        return ResponseEntity.ok(patientService.updatePatient(id, patientDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getOpenAppointments(@RequestParam LocalDateTime date) {
        return ResponseEntity.ok(patientService.getOpenAppointments(date));
    }

    @PostMapping("/appointments/{appointmentId}")
    public ResponseEntity<Void> takeAppointment(@PathVariable UUID appointmentId,
                                                @RequestParam String patientName,
                                                @RequestParam String phoneNumber) {
        patientService.takeAppointment(appointmentId, patientName, phoneNumber);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/appointments/my")
    public ResponseEntity<List<Appointment>> getPatientAppointments(@RequestParam String phoneNumber) {
        return ResponseEntity.ok(patientService.getPatientAppointments(phoneNumber));
    }
}
