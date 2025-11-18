package br.com.erp.desafioI.service;

import br.com.erp.desafioI.domain.ItemPedido;
import br.com.erp.desafioI.domain.Pedido;
import br.com.erp.desafioI.domain.ProdutoServico;
import br.com.erp.desafioI.domain.TipoItem;
import br.com.erp.desafioI.dto.ItemPedidoDTO;
import br.com.erp.desafioI.dto.PedidoRequest;
import br.com.erp.desafioI.repository.ItemPedidoRepository;
import br.com.erp.desafioI.repository.PedidoRepository;
import br.com.erp.desafioI.repository.ProdutoServicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Serviço de aplicação para operações de pedido e itens, incluindo
 * cálculo de totais e aplicação de desconto apenas sobre produtos.
 */
@Service
@Transactional
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ProdutoServicoRepository produtoServicoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ItemPedidoRepository itemPedidoRepository, ProdutoServicoRepository produtoServicoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.produtoServicoRepository = produtoServicoRepository;
    }

    /** Cria um pedido com itens a partir do request */
    public Pedido create(PedidoRequest request) {
        Pedido pedido = new Pedido();
        pedido.setDescontoPercentual(request.getDescontoPercentual());
        List<ItemPedido> itens = new ArrayList<>();
        for (ItemPedidoDTO dto : request.getItens()) {
            ProdutoServico ps = produtoServicoRepository.findById(dto.getProdutoServicoId()).orElseThrow(() -> new IllegalArgumentException("Produto/Serviço não encontrado"));
            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProdutoServico(ps);
            item.setQuantidade(dto.getQuantidade());
            item.setPrecoUnitario(ps.getPreco());
            itens.add(item);
        }
        pedido.setItens(itens);
        calcularTotais(pedido);
        Pedido saved = pedidoRepository.save(pedido);
        return saved;
    }

    /** Atualiza itens e desconto de um pedido */
    public Pedido update(UUID id, PedidoRequest request) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        pedido.setDescontoPercentual(request.getDescontoPercentual());
        pedido.getItens().clear();
        for (ItemPedidoDTO dto : request.getItens()) {
            ProdutoServico ps = produtoServicoRepository.findById(dto.getProdutoServicoId()).orElseThrow(() -> new IllegalArgumentException("Produto/Serviço não encontrado"));
            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProdutoServico(ps);
            item.setQuantidade(dto.getQuantidade());
            item.setPrecoUnitario(ps.getPreco());
            pedido.getItens().add(item);
        }
        calcularTotais(pedido);
        return pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    /** Busca um pedido por ID */
    public Pedido get(UUID id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
    }

    @Transactional(readOnly = true)
    /** Lista todos os pedidos */
    public List<Pedido> list() {
        return pedidoRepository.findAll();
    }

    /** Remove um pedido por ID */
    public void delete(UUID id) {
        if (!pedidoRepository.existsById(id)) {
            throw new IllegalArgumentException("Pedido não encontrado");
        }
        pedidoRepository.deleteById(id);
    }

    /** Adiciona um item ao pedido */
    public ItemPedido addItem(UUID pedidoId, ItemPedidoDTO dto) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        ProdutoServico ps = produtoServicoRepository.findById(dto.getProdutoServicoId()).orElseThrow(() -> new IllegalArgumentException("Produto/Serviço não encontrado"));
        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setProdutoServico(ps);
        item.setQuantidade(dto.getQuantidade());
        item.setPrecoUnitario(ps.getPreco());
        itemPedidoRepository.save(item);
        pedido.getItens().add(item);
        calcularTotais(pedido);
        pedidoRepository.save(pedido);
        return item;
    }

    /** Atualiza um item do pedido */
    public ItemPedido updateItem(UUID pedidoId, UUID itemId, ItemPedidoDTO dto) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        ItemPedido item = itemPedidoRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item não encontrado"));
        if (!item.getPedido().getId().equals(pedido.getId())) {
            throw new IllegalArgumentException("Item não pertence ao pedido");
        }
        ProdutoServico ps = produtoServicoRepository.findById(dto.getProdutoServicoId()).orElseThrow(() -> new IllegalArgumentException("Produto/Serviço não encontrado"));
        item.setProdutoServico(ps);
        item.setQuantidade(dto.getQuantidade());
        item.setPrecoUnitario(ps.getPreco());
        itemPedidoRepository.save(item);
        calcularTotais(pedido);
        pedidoRepository.save(pedido);
        return item;
    }

    /** Remove um item do pedido */
    public void deleteItem(UUID pedidoId, UUID itemId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        ItemPedido item = itemPedidoRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item não encontrado"));
        if (!item.getPedido().getId().equals(pedido.getId())) {
            throw new IllegalArgumentException("Item não pertence ao pedido");
        }
        pedido.getItens().removeIf(i -> i.getId().equals(item.getId()));
        itemPedidoRepository.delete(item);
        calcularTotais(pedido);
        pedidoRepository.save(pedido);
    }

    /**
     * Calcula os totais do pedido:
     * - totalProdutos: soma dos subtotais dos itens PRODUTO
     * - totalServicos: soma dos subtotais dos itens SERVICO
     * - totalDesconto: descontoPercentual aplicado somente sobre totalProdutos
     * - totalFinal: (totalProdutos - totalDesconto) + totalServicos
     */
    private void calcularTotais(Pedido pedido) {
        BigDecimal totalProdutos = BigDecimal.ZERO;
        BigDecimal totalServicos = BigDecimal.ZERO;
        for (ItemPedido item : pedido.getItens()) {
            BigDecimal subtotal = item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()));
            if (item.getProdutoServico().getTipo() == TipoItem.PRODUTO) {
                totalProdutos = totalProdutos.add(subtotal);
            } else {
                totalServicos = totalServicos.add(subtotal);
            }
        }
        BigDecimal descontoPercentual = pedido.getDescontoPercentual() == null ? BigDecimal.ZERO : pedido.getDescontoPercentual();
        BigDecimal totalDesconto = totalProdutos.multiply(descontoPercentual).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal totalFinal = totalProdutos.subtract(totalDesconto).add(totalServicos);
        pedido.setTotalProdutos(totalProdutos);
        pedido.setTotalServicos(totalServicos);
        pedido.setTotalDesconto(totalDesconto);
        pedido.setTotalFinal(totalFinal);
    }
}