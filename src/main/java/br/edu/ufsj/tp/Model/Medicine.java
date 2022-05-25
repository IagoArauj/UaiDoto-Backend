package br.edu.ufsj.tp.Model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Medicine {
    private String name;
    private int gap;
    private LocalDateTime firstTime;
    private String dose;
    private String notes;
}
