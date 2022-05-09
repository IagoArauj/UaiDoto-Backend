package br.edu.ufsj.tp.Model;

import lombok.Data;

@Data
public class Address {
    private String zipCode;
    private String city;
    private String street;
    private String number;
    private String complement;
}
