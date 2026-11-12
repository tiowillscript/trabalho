package br.com.baozi.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record ClienteRequest(
    @NotBlank @Size(max = 120) String nome,
    @NotNull @PastOrPresent LocalDate clienteDesde
) {}
