package br.edu.ufsj.tp.Controller;

import br.edu.ufsj.tp.Model.Appointment;
import br.edu.ufsj.tp.Repository.AppointmentRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("appointments")
@Data
@Slf4j
public class AppointmentController {
    @Autowired
    private AppointmentRepository repository;

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getById(@PathVariable String id) {
        Optional<Appointment> optionalAppointment = repository.findById(id);

        if(optionalAppointment.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalAppointment.get());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<Page<Appointment>> getAllByDoctorId(
            @PathVariable String id,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size
    ) {
        Page<Appointment> appointmentPage = repository.findAllByDoctorIdEquals(id, PageRequest.of(page, size));

        if(appointmentPage.getTotalElements() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(appointmentPage);
    }

    @GetMapping("/doctor/{id}/active")
    public ResponseEntity<Page<Appointment>> getAllByDoctorIdAndActive(
            @PathVariable String id,
            @RequestParam(value = "actives", required = false, defaultValue = "1") boolean actives,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size
    ) {
        Page<Appointment> appointmentPage;
        if(actives) {
            appointmentPage = repository.findAllByDoctorIdEqualsAndActiveIsTrueAndProcessedIsTrue(
                    id, PageRequest.of(page, size)
            );

        } else {
            appointmentPage = repository.findAllByDoctorIdEqualsAndActiveIsFalse(
                    id, PageRequest.of(page, size)
            );

        }

        if(appointmentPage.getTotalElements() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(appointmentPage);
    }

    @GetMapping("/doctor/{id}/not-processed")
    public ResponseEntity<Page<Appointment>> getAllByDoctorIdNotProcessed(
            @PathVariable String id,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size
    ) {
        Page<Appointment> appointmentPage = repository.findAllByDoctorIdEqualsAndProcessedIsFalse(
                id, PageRequest.of(page, size)
        );

        if(appointmentPage.getTotalElements() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(appointmentPage);
    }

    @GetMapping("/doctor/{id}/finished")
    public ResponseEntity<Page<Appointment>> getAllByDoctorIdFinished(
            @PathVariable String id,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size
    ) {
        Page<Appointment> appointmentPage = repository.findAllByDoctorIdEqualsAndFinishedIsTrue(
                id, PageRequest.of(page, size)
        );

        if(appointmentPage.getTotalElements() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(appointmentPage);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<Page<Appointment>> getAllByPatientId(
            @PathVariable String id,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size
    ) {
        Page<Appointment> appointmentPage = repository.findAllByPatientIdEquals(
                id, PageRequest.of(page, size)
        );

        if(appointmentPage.getTotalElements() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(appointmentPage);
    }

    @GetMapping("/patient/{id}/finished/medicines")
    public ResponseEntity<Page<Appointment>> getAllByPatientIdFinished(
            @PathVariable String id,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size
    ) {
        Page<Appointment> appointmentPage = repository.findAllByPatientIdEqualsAndFinishedIsTrueAndMedicinesIsNotNull(
                id, PageRequest.of(page, size)
        );

        if(appointmentPage.getTotalElements() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(appointmentPage);
    }

    @PostMapping("")
    public ResponseEntity<Appointment> saveAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                repository.save(appointment)
        );
    }
}
