package br.com.baozi.service;
import br.com.baozi.dto.*;
import br.com.baozi.model.*;
import br.com.baozi.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@Service
@Transactional(readOnly = true)
public class CadastroService {
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;
    private final PedidoRepository pedidos;
    public CadastroService(ClienteRepository clientes, ProdutoRepository produtos, PedidoRepository pedidos) {
        this.clientes = clientes; this.produtos = produtos; this.pedidos = pedidos;
    }
    public List<Cliente> listarClientes() { return clientes.findAll(Sort.by("id")); }
    public Cliente buscarCliente(Long id) {
        return clientes.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }
    @Transactional
    public Cliente criarCliente(ClienteRequest r) {
        return clientes.save(new Cliente(r.nome().trim(), r.clienteDesde()));
    }
    @Transactional
    public void excluirCliente(Long id) {
        Cliente cliente = buscarCliente(id);
        if (pedidos.existsByClienteId(id)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Cliente possui pedido vinculado");
        clientes.delete(cliente);
    }
    public List<Produto> listarProdutos() { return produtos.findAll(Sort.by("id")); }
    public Produto buscarProduto(Long id) {
        return produtos.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }
    @Transactional
    public Produto criarProduto(ProdutoRequest r) {
        return produtos.save(new Produto(r.nome().trim(), r.preco(), r.estoque()));
    }
    @Transactional
    public void excluirProduto(Long id) {
        Produto produto = buscarProduto(id);
        if (pedidos.existsByProdutoId(id)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Produto possui pedido vinculado");
        produtos.delete(produto);
    }
}
