package br.edu.ufsj.tp.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Specialty {
    @Id
    private String id;
    private String specialty;
}
