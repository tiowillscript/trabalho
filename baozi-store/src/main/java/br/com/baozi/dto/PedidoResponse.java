package br.com.baozi.dto;
import br.com.baozi.model.Pedido;
public record PedidoResponse(Long id, Long clienteId, Long produtoId, Integer quantidade) {
    public static PedidoResponse from(Pedido p) {
        return new PedidoResponse(p.getId(), p.getCliente().getId(), p.getProduto().getId(), p.getQuantidade());
    }
}
