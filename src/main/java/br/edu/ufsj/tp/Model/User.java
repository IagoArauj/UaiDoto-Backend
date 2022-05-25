package br.edu.ufsj.tp.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document
public class User {

    @Id
    private String id;

    private String name;
    private String email;
    private String password;
    private Gender gender;
    private Address address;
    private long phone;
    private List<WorkDays> workDays;
    private String crm;
    private String specialty;
    private Date createdAt;
    private Date updatedAt;
}
