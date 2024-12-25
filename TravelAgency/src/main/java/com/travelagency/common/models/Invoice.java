package com.travelagency.common.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    private Long invoiceId;
    private double amount;
    private String details;
    private String status;
} 