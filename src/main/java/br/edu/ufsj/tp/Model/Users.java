package br.edu.ufsj.tp.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Document
public class Users {

    @Id
    private String id;

    private String name;
    private String email;
    private String password;
    private Gender gender;
    private Address address;
    private long phone;
    private List<WorkDays> workDays;
    private long crm;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
