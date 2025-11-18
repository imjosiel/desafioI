package br.com.erp.desafioI.service;

import br.com.erp.desafioI.domain.Pedido;
import br.com.erp.desafioI.domain.ProdutoServico;
import br.com.erp.desafioI.domain.TipoItem;
import br.com.erp.desafioI.dto.ItemPedidoDTO;
import br.com.erp.desafioI.dto.PedidoRequest;
import br.com.erp.desafioI.repository.ItemPedidoRepository;
import br.com.erp.desafioI.repository.PedidoRepository;
import br.com.erp.desafioI.repository.ProdutoServicoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @Mock
    private ProdutoServicoRepository produtoServicoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    public void calculaDescontoApenasEmProdutos() {
        ProdutoServico produto = new ProdutoServico(UUID.randomUUID(), "Produto A", new BigDecimal("100.00"), TipoItem.PRODUTO);
        ProdutoServico servico = new ProdutoServico(UUID.randomUUID(), "Serviço B", new BigDecimal("50.00"), TipoItem.SERVICO);

        when(produtoServicoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(produtoServicoRepository.findById(servico.getId())).thenReturn(Optional.of(servico));
        when(pedidoRepository.save(org.mockito.ArgumentMatchers.any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PedidoRequest req = new PedidoRequest();
        req.setDescontoPercentual(new BigDecimal("10"));
        ItemPedidoDTO itemProduto = new ItemPedidoDTO();
        itemProduto.setProdutoServicoId(produto.getId());
        itemProduto.setQuantidade(2);
        ItemPedidoDTO itemServico = new ItemPedidoDTO();
        itemServico.setProdutoServicoId(servico.getId());
        itemServico.setQuantidade(1);
        req.setItens(Arrays.asList(itemProduto, itemServico));

        Pedido pedido = pedidoService.create(req);

        Assertions.assertEquals(new BigDecimal("200.00"), pedido.getTotalProdutos());
        Assertions.assertEquals(new BigDecimal("50.00"), pedido.getTotalServicos());
        Assertions.assertEquals(new BigDecimal("20.00"), pedido.getTotalDesconto());
        Assertions.assertEquals(new BigDecimal("230.00"), pedido.getTotalFinal());
    }
}