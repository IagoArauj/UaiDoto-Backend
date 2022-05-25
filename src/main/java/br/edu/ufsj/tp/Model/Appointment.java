package br.edu.ufsj.tp.Model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Appointment {
    private String doctorId;
    private String patientId;
    private LocalDateTime dateTime;
    private List<Medicine> medicines;
}
