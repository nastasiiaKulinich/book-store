package com.example.bookstore.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookSearchParametersDto {
    private String[] titles;
    private String[] authors;
    private BigDecimal fromPrice;
    private BigDecimal toPrice;
    private String[] descriptions;
    private String[] coverImages;
}
