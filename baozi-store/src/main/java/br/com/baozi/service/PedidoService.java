package br.com.baozi.service;
import br.com.baozi.dto.*;
import br.com.baozi.model.*;
import br.com.baozi.repository.PedidoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@Service
@Transactional(readOnly = true)
public class PedidoService {
    private final PedidoRepository pedidos;
    private final CadastroService cadastro;
    public PedidoService(PedidoRepository pedidos, CadastroService cadastro) {
        this.pedidos = pedidos; this.cadastro = cadastro;
    }
    private Pedido encontrar(Long id) {
        return pedidos.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }
    public List<PedidoResponse> listar() {
        return pedidos.findAll(Sort.by("id")).stream().map(PedidoResponse::from).toList();
    }
    public PedidoResponse buscar(Long id) { return PedidoResponse.from(encontrar(id)); }
    @Transactional
    public PedidoResponse criar(PedidoRequest r) {
        Cliente cliente = cadastro.buscarCliente(r.clienteId());
        Produto produto = cadastro.buscarProduto(r.produtoId());
        if (!Boolean.TRUE.equals(produto.getEstoque()))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Produto indisponível em estoque");
        return PedidoResponse.from(pedidos.save(new Pedido(cliente, produto, r.quantidade())));
    }
    @Transactional
    public void excluir(Long id) { pedidos.delete(encontrar(id)); }
}
