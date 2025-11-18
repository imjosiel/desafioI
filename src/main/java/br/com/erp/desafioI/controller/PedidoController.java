package br.com.erp.desafioI.controller;

import br.com.erp.desafioI.domain.ItemPedido;
import br.com.erp.desafioI.domain.Pedido;
import br.com.erp.desafioI.dto.ItemPedidoDTO;
import br.com.erp.desafioI.dto.PedidoRequest;
import br.com.erp.desafioI.dto.PedidoResponse;
import br.com.erp.desafioI.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Endpoints REST para criação e gestão de pedidos e itens.
 */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    /** Cria um pedido com itens e desconto opcional */
    @PostMapping
    public ResponseEntity<PedidoResponse> create(@Valid @RequestBody PedidoRequest request) {
        Pedido saved = service.create(request);
        return ResponseEntity.created(URI.create("/api/pedidos/" + saved.getId())).body(toResponse(saved));
    }

    /** Atualiza itens e desconto de um pedido */
    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponse> update(@PathVariable UUID id, @Valid @RequestBody PedidoRequest request) {
        Pedido updated = service.update(id, request);
        return ResponseEntity.ok(toResponse(updated));
    }

    /** Busca pedido por ID */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> get(@PathVariable UUID id) {
        Pedido pedido = service.get(id);
        return ResponseEntity.ok(toResponse(pedido));
    }

    /** Lista pedidos */
    @GetMapping
    public ResponseEntity<List<PedidoResponse>> list() {
        List<PedidoResponse> responses = new ArrayList<>();
        for (Pedido p : service.list()) {
            responses.add(toResponse(p));
        }
        return ResponseEntity.ok(responses);
    }

    /** Remove pedido por ID */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /** Adiciona item ao pedido */
    @PostMapping("/{pedidoId}/itens")
    public ResponseEntity<PedidoResponse> addItem(@PathVariable UUID pedidoId, @Valid @RequestBody ItemPedidoDTO dto) {
        service.addItem(pedidoId, dto);
        Pedido pedido = service.get(pedidoId);
        return ResponseEntity.ok(toResponse(pedido));
    }

    /** Atualiza item do pedido */
    @PutMapping("/{pedidoId}/itens/{itemId}")
    public ResponseEntity<PedidoResponse> updateItem(@PathVariable UUID pedidoId, @PathVariable UUID itemId, @Valid @RequestBody ItemPedidoDTO dto) {
        service.updateItem(pedidoId, itemId, dto);
        Pedido pedido = service.get(pedidoId);
        return ResponseEntity.ok(toResponse(pedido));
    }

    /** Remove item do pedido */
    @DeleteMapping("/{pedidoId}/itens/{itemId}")
    public ResponseEntity<PedidoResponse> deleteItem(@PathVariable UUID pedidoId, @PathVariable UUID itemId) {
        service.deleteItem(pedidoId, itemId);
        Pedido pedido = service.get(pedidoId);
        return ResponseEntity.ok(toResponse(pedido));
    }

    /** Converte entidade de pedido para DTO de resposta (inclui itens e totais) */
    private PedidoResponse toResponse(Pedido pedido) {
        PedidoResponse resp = new PedidoResponse();
        resp.setId(pedido.getId());
        resp.setDataCriacao(pedido.getDataCriacao());
        resp.setDescontoPercentual(pedido.getDescontoPercentual());
        resp.setTotalProdutos(pedido.getTotalProdutos());
        resp.setTotalServicos(pedido.getTotalServicos());
        resp.setTotalDesconto(pedido.getTotalDesconto());
        resp.setTotalFinal(pedido.getTotalFinal());
        List<PedidoResponse.ItemPedidoDetalhe> itens = new ArrayList<>();
        for (ItemPedido i : pedido.getItens()) {
            PedidoResponse.ItemPedidoDetalhe d = new PedidoResponse.ItemPedidoDetalhe();
            d.setId(i.getId());
            d.setProdutoServicoId(i.getProdutoServico().getId());
            d.setNome(i.getProdutoServico().getNome());
            d.setTipo(i.getProdutoServico().getTipo().name());
            d.setQuantidade(i.getQuantidade());
            d.setPrecoUnitario(i.getPrecoUnitario());
            itens.add(d);
        }
        resp.setItens(itens);
        return resp;
    }
}