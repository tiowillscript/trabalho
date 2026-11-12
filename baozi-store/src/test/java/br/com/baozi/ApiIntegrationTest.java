package br.com.baozi;
import br.com.baozi.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class ApiIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Autowired PedidoRepository pedidos;
    @Autowired ClienteRepository clientes;
    @Autowired ProdutoRepository produtos;
    @BeforeEach void limpar() { pedidos.deleteAll(); clientes.deleteAll(); produtos.deleteAll(); }
    long criar(String rota, String json) throws Exception {
        String body = mvc.perform(post(rota).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isCreated()).andExpect(header().exists("Location"))
            .andReturn().getResponse().getContentAsString();
        return mapper.readTree(body).get("id").asLong();
    }
    long cliente() throws Exception { return criar("/clientes", "{\"nome\":\"Willian Douglas Ferreira1813098\",\"clienteDesde\":\"2026-11-12\"}"); }
    long produto(boolean estoque) throws Exception { return criar("/produtos", "{\"nome\":\"Baozi tradicional\",\"preco\":8.50,\"estoque\":" + estoque + "}"); }
    String pedido(long c, long p, int q) { return "{\"clienteId\":" + c + ",\"produtoId\":" + p + ",\"quantidade\":" + q + "}"; }
    @Test void cicloCompletoDasTresEntidades() throws Exception {
        long c=cliente(), p=produto(true), id=criar("/pedidos", pedido(c,p,4));
        mvc.perform(get("/clientes")).andExpect(status().isOk()).andExpect(jsonPath("$[0].nome").value("Willian Douglas Ferreira1813098"));
        mvc.perform(get("/produtos")).andExpect(status().isOk()).andExpect(jsonPath("$[0].preco").value(8.5));
        mvc.perform(get("/pedidos")).andExpect(status().isOk()).andExpect(jsonPath("$[0].quantidade").value(4));
        mvc.perform(get("/clientes/"+c)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(c));
        mvc.perform(get("/produtos/"+p)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(p));
        mvc.perform(get("/pedidos/"+id)).andExpect(status().isOk()).andExpect(jsonPath("$.clienteId").value(c)).andExpect(jsonPath("$.produtoId").value(p));
        mvc.perform(delete("/pedidos/"+id)).andExpect(status().isNoContent()).andExpect(content().string(""));
        mvc.perform(delete("/clientes/"+c)).andExpect(status().isNoContent());
        mvc.perform(delete("/produtos/"+p)).andExpect(status().isNoContent());
        mvc.perform(get("/pedidos/"+id)).andExpect(status().isNotFound());
        mvc.perform(get("/clientes")).andExpect(content().json("[]"));
        mvc.perform(get("/produtos")).andExpect(content().json("[]"));
    }
    @Test void bloqueiaExclusaoDeRegistrosVinculados() throws Exception {
        long c=cliente(), p=produto(true); criar("/pedidos",pedido(c,p,4));
        mvc.perform(delete("/clientes/"+c)).andExpect(status().isConflict());
        mvc.perform(delete("/produtos/"+p)).andExpect(status().isConflict());
    }
    @Test void rejeitaQuantidadeZero() throws Exception {
        long c=cliente(), p=produto(true);
        mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content(pedido(c,p,0))).andExpect(status().isBadRequest());
    }
    @Test void rejeitaQuantidadeNegativa() throws Exception {
        long c=cliente(), p=produto(true);
        mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content(pedido(c,p,-1))).andExpect(status().isBadRequest());
    }
    @Test void rejeitaClienteInexistente() throws Exception {
        long p=produto(true);
        mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content(pedido(99999,p,4))).andExpect(status().isNotFound());
    }
    @Test void rejeitaProdutoInexistente() throws Exception {
        long c=cliente();
        mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content(pedido(c,99999,4))).andExpect(status().isNotFound());
    }
    @Test void rejeitaProdutoIndisponivel() throws Exception {
        long c=cliente(), p=produto(false);
        mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content(pedido(c,p,4))).andExpect(status().isUnprocessableEntity());
    }
    @Test void rejeitaNomeVazioEDataFutura() throws Exception {
        mvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\" \",\"clienteDesde\":\"2999-01-01\"}"))
            .andExpect(status().isBadRequest());
    }
    @Test void rejeitaPrecoNegativo() throws Exception {
        mvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\"Baozi\",\"preco\":-1,\"estoque\":true}"))
            .andExpect(status().isBadRequest());
    }
    @Test void rejeitaJsonMalformado() throws Exception {
        mvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON).content("{invalido"))
            .andExpect(status().isBadRequest());
    }
    @Test void consultaEExclusaoInexistentesRetornam404() throws Exception {
        for (String r : new String[]{"/clientes/99999","/produtos/99999","/pedidos/99999"}) {
            mvc.perform(get(r)).andExpect(status().isNotFound());
            mvc.perform(delete(r)).andExpect(status().isNotFound());
        }
    }
}
