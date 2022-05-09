package br.edu.ufsj.tp.Model;

import lombok.Data;

import java.time.LocalTime;
import java.util.Date;

@Data
public class WorkHours {
    private LocalTime start;
    private LocalTime end;
}
