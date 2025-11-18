package br.com.erp.desafioI.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * DTO para criação/atualização de itens de pedido.
 */
public class ItemPedidoDTO {
    private UUID id;

    /** ID do produto/serviço associado ao item */
    @NotNull
    private UUID produtoServicoId;

    /** Quantidade mínima 1 */
    @NotNull
    @Min(1)
    private Integer quantidade;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProdutoServicoId() {
        return produtoServicoId;
    }

    public void setProdutoServicoId(UUID produtoServicoId) {
        this.produtoServicoId = produtoServicoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}