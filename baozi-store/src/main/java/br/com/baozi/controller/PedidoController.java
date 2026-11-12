package br.com.baozi.controller;
import br.com.baozi.dto.*;
import br.com.baozi.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService service;
    public PedidoController(PedidoService service) { this.service = service; }
    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoRequest request) {
        PedidoResponse criado = service.criar(request);
        return ResponseEntity.created(URI.create("/pedidos/" + criado.id())).body(criado);
    }
    @GetMapping
    public List<PedidoResponse> listar() { return service.listar(); }
    @GetMapping("/{id}")
    public PedidoResponse buscar(@PathVariable Long id) { return service.buscar(id); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
