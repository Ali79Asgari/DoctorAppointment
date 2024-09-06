package com.example.doctorappointment.controllers;

import com.example.doctorappointment.entities.Appointment;
import com.example.doctorappointment.entities.Doctor;
import com.example.doctorappointment.services.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@Valid @RequestBody Doctor doctor) {
        return new ResponseEntity<>(doctorService.createDoctor(doctor), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable UUID id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable UUID id, @Valid @RequestBody Doctor doctorDetails) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctorDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable UUID id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/open-times")
    public ResponseEntity<Void> addOpenTimes(@PathVariable UUID id,
                                             @RequestParam LocalDateTime startTime,
                                             @RequestParam LocalDateTime endTime) {
        doctorService.addOpenTimes(id, startTime, endTime);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/appointments")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@PathVariable UUID id,
                                                                   @RequestParam LocalDateTime date,
                                                                   @RequestParam(defaultValue = "false") boolean onlyOpen) {
        return ResponseEntity.ok(doctorService.getAppointments(id, date, onlyOpen));
    }

    @DeleteMapping("/{doctorId}/appointments/{appointmentId}")
    public ResponseEntity<Void> deleteOpenAppointment(@PathVariable UUID doctorId,
                                                      @PathVariable UUID appointmentId) {
        doctorService.deleteOpenAppointment(doctorId, appointmentId);
        return ResponseEntity.noContent().build();
    }
}
