package br.com.erp.desafioI.integration;

import br.com.erp.desafioI.domain.TipoItem;
import br.com.erp.desafioI.dto.ItemPedidoDTO;
import br.com.erp.desafioI.dto.PedidoRequest;
import br.com.erp.desafioI.dto.PedidoResponse;
import br.com.erp.desafioI.domain.ProdutoServico;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiCrudTest {

    @Autowired
    private TestRestTemplate rest;

    private static UUID produtoId;
    private static UUID servicoId;
    private static UUID pedidoId;
    private static UUID itemExtraId;

    @Test
    @Order(1)
    public void criaCatalogoProdutoEServico() {
        ProdutoServico produto = new ProdutoServico(null, "Produto Teste", new BigDecimal("100.00"), TipoItem.PRODUTO);
        ResponseEntity<ProdutoServico> rProduto = rest.postForEntity("/api/catalogo", produto, ProdutoServico.class);
        Assertions.assertEquals(201, rProduto.getStatusCode().value());
        Assertions.assertNotNull(rProduto.getBody());
        produtoId = rProduto.getBody().getId();

        ProdutoServico servico = new ProdutoServico(null, "Serviço Teste", new BigDecimal("50.00"), TipoItem.SERVICO);
        ResponseEntity<ProdutoServico> rServico = rest.postForEntity("/api/catalogo", servico, ProdutoServico.class);
        Assertions.assertEquals(201, rServico.getStatusCode().value());
        Assertions.assertNotNull(rServico.getBody());
        servicoId = rServico.getBody().getId();

        ResponseEntity<ProdutoServico[]> lista = rest.getForEntity("/api/catalogo", ProdutoServico[].class);
        Assertions.assertEquals(200, lista.getStatusCode().value());
        Assertions.assertTrue(lista.getBody().length >= 2);

        ResponseEntity<ProdutoServico> busca = rest.getForEntity("/api/catalogo/" + produtoId, ProdutoServico.class);
        Assertions.assertEquals(200, busca.getStatusCode().value());
        Assertions.assertEquals("Produto Teste", busca.getBody().getNome());
    }

    @Test
    @Order(2)
    public void atualizaEDesativaServico() {
        ProdutoServico atualizacao = new ProdutoServico(servicoId, "Serviço Atualizado", new BigDecimal("60.00"), TipoItem.SERVICO);
        ResponseEntity<ProdutoServico> upd = rest.exchange("/api/catalogo/" + servicoId, HttpMethod.PUT, new HttpEntity<>(atualizacao), ProdutoServico.class);
        Assertions.assertEquals(200, upd.getStatusCode().value());
        Assertions.assertEquals("Serviço Atualizado", upd.getBody().getNome());

        ResponseEntity<Void> del = rest.exchange("/api/catalogo/" + servicoId, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        Assertions.assertEquals(204, del.getStatusCode().value());
    }

    @Test
    @Order(3)
    public void criaPedidoComItensEDesconto() {
        PedidoRequest req = new PedidoRequest();
        req.setDescontoPercentual(new BigDecimal("10"));

        List<ItemPedidoDTO> itens = new ArrayList<>();
        ItemPedidoDTO itemProduto = new ItemPedidoDTO();
        itemProduto.setProdutoServicoId(produtoId);
        itemProduto.setQuantidade(2);
        itens.add(itemProduto);

        // Serviço foi removido; cria outro serviço para compor o pedido
        ProdutoServico novoServico = new ProdutoServico(null, "Serviço Pedido", new BigDecimal("80.00"), TipoItem.SERVICO);
        ResponseEntity<ProdutoServico> servCriado = rest.postForEntity("/api/catalogo", novoServico, ProdutoServico.class);
        Assertions.assertEquals(201, servCriado.getStatusCode().value());

        ItemPedidoDTO itemServico = new ItemPedidoDTO();
        itemServico.setProdutoServicoId(servCriado.getBody().getId());
        itemServico.setQuantidade(1);
        itens.add(itemServico);

        req.setItens(itens);

        ResponseEntity<PedidoResponse> rPedido = rest.postForEntity("/api/pedidos", req, PedidoResponse.class);
        Assertions.assertEquals(201, rPedido.getStatusCode().value());
        Assertions.assertNotNull(rPedido.getBody());
        pedidoId = rPedido.getBody().getId();

        Assertions.assertEquals(new BigDecimal("200.00"), rPedido.getBody().getTotalProdutos());
        Assertions.assertEquals(new BigDecimal("80.00"), rPedido.getBody().getTotalServicos());
        Assertions.assertEquals(new BigDecimal("20.00"), rPedido.getBody().getTotalDesconto());
        Assertions.assertEquals(new BigDecimal("260.00"), rPedido.getBody().getTotalFinal());
    }

    @Test
    @Order(4)
    public void adicionaAtualizaRemoveItemDoPedido() {
        ItemPedidoDTO extra = new ItemPedidoDTO();
        extra.setProdutoServicoId(produtoId);
        extra.setQuantidade(1);
        ResponseEntity<PedidoResponse> add = rest.postForEntity("/api/pedidos/" + pedidoId + "/itens", extra, PedidoResponse.class);
        Assertions.assertEquals(200, add.getStatusCode().value());
        Assertions.assertTrue(add.getBody().getItens().size() >= 3);
        itemExtraId = add.getBody().getItens().stream().filter(i -> i.getQuantidade() == 1 && i.getProdutoServicoId().equals(produtoId)).findFirst().map(PedidoResponse.ItemPedidoDetalhe::getId).orElse(null);
        Assertions.assertNotNull(itemExtraId);

        ItemPedidoDTO upd = new ItemPedidoDTO();
        upd.setProdutoServicoId(produtoId);
        upd.setQuantidade(3);
        ResponseEntity<PedidoResponse> updr = rest.exchange("/api/pedidos/" + pedidoId + "/itens/" + itemExtraId, HttpMethod.PUT, new HttpEntity<>(upd), PedidoResponse.class);
        Assertions.assertEquals(200, updr.getStatusCode().value());
        Assertions.assertTrue(updr.getBody().getItens().stream().anyMatch(i -> i.getId().equals(itemExtraId) && i.getQuantidade() == 3));

        ResponseEntity<PedidoResponse> delr = rest.exchange("/api/pedidos/" + pedidoId + "/itens/" + itemExtraId, HttpMethod.DELETE, HttpEntity.EMPTY, PedidoResponse.class);
        Assertions.assertEquals(200, delr.getStatusCode().value());
        Assertions.assertTrue(delr.getBody().getItens().stream().noneMatch(i -> i.getId().equals(itemExtraId)));
    }

    @Test
    @Order(5)
    public void atualizaPedidoEDepoisRemove() {
        PedidoRequest req = new PedidoRequest();
        req.setDescontoPercentual(new BigDecimal("5"));

        List<ItemPedidoDTO> itens = new ArrayList<>();
        ItemPedidoDTO itemProduto = new ItemPedidoDTO();
        itemProduto.setProdutoServicoId(produtoId);
        itemProduto.setQuantidade(1);
        itens.add(itemProduto);
        req.setItens(itens);

        ResponseEntity<PedidoResponse> upd = rest.exchange("/api/pedidos/" + pedidoId, HttpMethod.PUT, new HttpEntity<>(req), PedidoResponse.class);
        Assertions.assertEquals(200, upd.getStatusCode().value());

        Assertions.assertEquals(new BigDecimal("100.00"), upd.getBody().getTotalProdutos());
        Assertions.assertEquals(new BigDecimal("0.00"), upd.getBody().getTotalServicos());
        Assertions.assertEquals(new BigDecimal("5.00"), upd.getBody().getTotalDesconto());
        Assertions.assertEquals(new BigDecimal("95.00"), upd.getBody().getTotalFinal());

        ResponseEntity<PedidoResponse[]> lista = rest.getForEntity("/api/pedidos", PedidoResponse[].class);
        Assertions.assertEquals(200, lista.getStatusCode().value());
        Assertions.assertTrue(lista.getBody().length >= 1);

        ResponseEntity<Void> del = rest.exchange("/api/pedidos/" + pedidoId, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        Assertions.assertEquals(204, del.getStatusCode().value());
    }
}