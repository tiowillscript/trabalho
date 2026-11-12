package br.com.baozi.controller;
import br.com.baozi.dto.ProdutoRequest;
import br.com.baozi.model.Produto;
import br.com.baozi.service.CadastroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final CadastroService service;
    public ProdutoController(CadastroService service) { this.service = service; }
    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody ProdutoRequest request) {
        Produto criado = service.criarProduto(request);
        return ResponseEntity.created(URI.create("/produtos/" + criado.getId())).body(criado);
    }
    @GetMapping
    public List<Produto> listar() { return service.listarProdutos(); }
    @GetMapping("/{id}")
    public Produto buscar(@PathVariable Long id) { return service.buscarProduto(id); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluirProduto(id);
        return ResponseEntity.noContent().build();
    }
}
