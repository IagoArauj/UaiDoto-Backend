package br.edu.ufsj.tp.Model;

import lombok.Data;

@Data
public class WorkDays {
    private enum Days { SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY }

    private Days day;
    private WorkHours workHours;
}
