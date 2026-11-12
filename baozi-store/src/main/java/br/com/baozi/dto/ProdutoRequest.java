package br.com.baozi.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record ProdutoRequest(
    @NotBlank @Size(max = 120) String nome,
    @NotNull @DecimalMin("0.01") @Digits(integer = 8, fraction = 2) BigDecimal preco,
    @NotNull Boolean estoque
) {}
