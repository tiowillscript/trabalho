package br.com.baozi.model;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity
@Table(name = "clientes")
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String nome;
    @Column(nullable = false)
    private LocalDate clienteDesde;
    protected Cliente() {}
    public Cliente(String nome, LocalDate clienteDesde) { this.nome = nome; this.clienteDesde = clienteDesde; }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public LocalDate getClienteDesde() { return clienteDesde; }
}
