package br.com.baozi.dto;
import jakarta.validation.constraints.*;
public record PedidoRequest(
    @NotNull @Positive Long clienteId,
    @NotNull @Positive Long produtoId,
    @NotNull @Positive Integer quantidade
) {}
