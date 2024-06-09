package com.example.bookstore.dto.user;

import com.example.bookstore.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch(firstField = "password", secondField = "repeatPassword")
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Length(min = 8, max = 50)
    private String password;
    @NotBlank
    @Length(min = 8, max = 50)
    private String repeatPassword;
    @NotBlank
    @Length(max = 30)
    private String firstName;
    @NotBlank
    @Length(max = 30)
    private String lastName;
    @NotBlank
    private String shippingAddress;
}
