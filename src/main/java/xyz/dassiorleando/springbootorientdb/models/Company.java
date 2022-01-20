package xyz.dassiorleando.springbootorientdb.models;

import lombok.Data;

@Data
public class Company {
    private Long companyId;
    private String companyName;
    private double companyOrder;
}
