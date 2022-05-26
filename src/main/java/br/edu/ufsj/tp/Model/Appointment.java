package br.edu.ufsj.tp.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Appointment {
    @Id
    private String id;
    private String doctorId;
    private String patientId;
    private LocalDateTime dateTime;
    private List<Medicine> medicines = null;
    private boolean active = true;
    private boolean processed = false;
    private boolean finished = false;
    private String observations;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
