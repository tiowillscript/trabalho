package br.com.baozi.controller;
import br.com.baozi.dto.ClienteRequest;
import br.com.baozi.model.Cliente;
import br.com.baozi.service.CadastroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final CadastroService service;
    public ClienteController(CadastroService service) { this.service = service; }
    @PostMapping
    public ResponseEntity<Cliente> criar(@Valid @RequestBody ClienteRequest request) {
        Cliente criado = service.criarCliente(request);
        return ResponseEntity.created(URI.create("/clientes/" + criado.getId())).body(criado);
    }
    @GetMapping
    public List<Cliente> listar() { return service.listarClientes(); }
    @GetMapping("/{id}")
    public Cliente buscar(@PathVariable Long id) { return service.buscarCliente(id); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluirCliente(id);
        return ResponseEntity.noContent().build();
    }
}
