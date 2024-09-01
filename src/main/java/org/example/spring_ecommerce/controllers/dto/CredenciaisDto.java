package org.example.spring_ecommerce.controllers.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CredenciaisDto {
    private String email;
    private String senha;
}
