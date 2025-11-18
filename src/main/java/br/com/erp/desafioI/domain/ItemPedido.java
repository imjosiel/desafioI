package br.com.erp.desafioI.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Item de pedido vinculado a um {@link Pedido} e a um {@link ProdutoServico}.
 */
@Entity
@Table(name = "item_pedido")
public class ItemPedido {

    /** Identificador único UUID do item */
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /** Pedido ao qual o item pertence */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    /** Produto/Serviço selecionado para o item */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_servico_id", nullable = false)
    private ProdutoServico produtoServico;

    /** Quantidade do item no pedido */
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    /** Preço unitário aplicado no item (copiado do catálogo) */
    @Column(name = "preco_unitario", nullable = false, precision = 19, scale = 2)
    private BigDecimal precoUnitario;

    public ItemPedido() {
    }

    public ItemPedido(UUID id, Pedido pedido, ProdutoServico produtoServico, Integer quantidade, BigDecimal precoUnitario) {
        this.id = id;
        this.pedido = pedido;
        this.produtoServico = produtoServico;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    /**
     * Geração automática de UUID ao persistir quando não informado.
     */
    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public ProdutoServico getProdutoServico() {
        return produtoServico;
    }

    public void setProdutoServico(ProdutoServico produtoServico) {
        this.produtoServico = produtoServico;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}