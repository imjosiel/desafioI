package br.com.erp.desafioI.integration;

import br.com.erp.desafioI.domain.Pedido;
import br.com.erp.desafioI.domain.ProdutoServico;
import br.com.erp.desafioI.domain.TipoItem;
import br.com.erp.desafioI.dto.ItemPedidoDTO;
import br.com.erp.desafioI.dto.PedidoRequest;
import br.com.erp.desafioI.repository.PedidoRepository;
import br.com.erp.desafioI.repository.ProdutoServicoRepository;
import br.com.erp.desafioI.service.PedidoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;

@SpringBootTest
public class NeonIntegrationTest {

    @Autowired
    private ProdutoServicoRepository produtoServicoRepository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Test
    public void persisteDadosNoNeon() {
        ProdutoServico p = new ProdutoServico(null, "Produto Neon", new BigDecimal("99.99"), TipoItem.PRODUTO);
        p = produtoServicoRepository.save(p);

        PedidoRequest req = new PedidoRequest();
        req.setDescontoPercentual(new BigDecimal("5"));
        ItemPedidoDTO item = new ItemPedidoDTO();
        item.setProdutoServicoId(p.getId());
        item.setQuantidade(3);
        req.setItens(Collections.singletonList(item));

        Pedido pedido = pedidoService.create(req);

        Assertions.assertNotNull(pedido.getId());
        Assertions.assertTrue(pedidoRepository.findById(pedido.getId()).isPresent());
    }
}