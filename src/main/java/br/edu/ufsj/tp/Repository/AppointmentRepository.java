package br.edu.ufsj.tp.Repository;

import br.edu.ufsj.tp.Model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {

    Page<Appointment> findAll(Pageable pageable);

    Page<Appointment> findAllByDoctorIdEquals(String doctorId, Pageable pageable);
    Page<Appointment> findAllByDoctorIdEqualsAndActiveIsTrueAndProcessedIsTrueAndFinishedIsFalse(String doctorId, Pageable pageable);
    Page<Appointment> findAllByDoctorIdEqualsAndActiveIsFalse(String doctorId, Pageable pageable);
    Page<Appointment> findAllByDoctorIdEqualsAndProcessedIsFalse(String doctorId, Pageable pageable);
    Page<Appointment> findAllByDoctorIdEqualsAndFinishedIsTrue(String doctorId, Pageable pageable);

    Page<Appointment> findAllByPatientIdEquals(String patientId, Pageable pageable);
    Page<Appointment> findAllByPatientIdEqualsAndFinishedIsTrueAndMedicinesIsNotNull(String patientId, Pageable pageable);
}
