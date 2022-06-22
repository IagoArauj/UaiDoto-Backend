package br.edu.ufsj.tp.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class City {
    @Id
    String id;
    String city;
    int doctorsCounter;
}
