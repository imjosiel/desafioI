package br.com.erp.desafioI.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidade de pedido contendo itens e totais calculados.
 * O desconto é aplicado apenas sobre o total de produtos.
 */
@Entity
@Table(name = "pedido")
public class Pedido {

    /** Identificador único UUID do pedido */
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /** Data/hora da criação do pedido */
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    /** Percentual de desconto aplicado sobre produtos (0 a 100) */
    @Column(name = "desconto_percentual", precision = 5, scale = 2)
    private BigDecimal descontoPercentual;

    /** Soma dos subtotais de itens de tipo PRODUTO */
    @Column(name = "total_produtos", precision = 19, scale = 2)
    private BigDecimal totalProdutos;

    /** Soma dos subtotais de itens de tipo SERVICO */
    @Column(name = "total_servicos", precision = 19, scale = 2)
    private BigDecimal totalServicos;

    /** Valor do desconto calculado sobre produtos */
    @Column(name = "total_desconto", precision = 19, scale = 2)
    private BigDecimal totalDesconto;

    /** Total final: (totalProdutos - totalDesconto) + totalServicos */
    @Column(name = "total_final", precision = 19, scale = 2)
    private BigDecimal totalFinal;

    /** Itens do pedido, com cascade para persistir/remover junto ao pedido */
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido() {
    }

    public Pedido(UUID id, LocalDateTime dataCriacao, BigDecimal descontoPercentual) {
        this.id = id;
        this.dataCriacao = dataCriacao;
        this.descontoPercentual = descontoPercentual;
    }

    /**
     * Geração automática de UUID e data de criação quando não informados.
     */
    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public BigDecimal getDescontoPercentual() {
        return descontoPercentual;
    }

    public void setDescontoPercentual(BigDecimal descontoPercentual) {
        this.descontoPercentual = descontoPercentual;
    }

    public BigDecimal getTotalProdutos() {
        return totalProdutos;
    }

    public void setTotalProdutos(BigDecimal totalProdutos) {
        this.totalProdutos = totalProdutos;
    }

    public BigDecimal getTotalServicos() {
        return totalServicos;
    }

    public void setTotalServicos(BigDecimal totalServicos) {
        this.totalServicos = totalServicos;
    }

    public BigDecimal getTotalDesconto() {
        return totalDesconto;
    }

    public void setTotalDesconto(BigDecimal totalDesconto) {
        this.totalDesconto = totalDesconto;
    }

    public BigDecimal getTotalFinal() {
        return totalFinal;
    }

    public void setTotalFinal(BigDecimal totalFinal) {
        this.totalFinal = totalFinal;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }
}