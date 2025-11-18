package br.com.erp.desafioI.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO de saída para pedidos, com totais e detalhes dos itens.
 */
public class PedidoResponse {
    private UUID id;
    private LocalDateTime dataCriacao;
    private BigDecimal descontoPercentual;
    private BigDecimal totalProdutos;
    private BigDecimal totalServicos;
    private BigDecimal totalDesconto;
    private BigDecimal totalFinal;
    private List<ItemPedidoDetalhe> itens;

    /** Detalhe de item no pedido, exibido na resposta */
    public static class ItemPedidoDetalhe {
        private UUID id;
        private UUID produtoServicoId;
        private String nome;
        private String tipo;
        private Integer quantidade;
        private BigDecimal precoUnitario;

        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public UUID getProdutoServicoId() { return produtoServicoId; }
        public void setProdutoServicoId(UUID produtoServicoId) { this.produtoServicoId = produtoServicoId; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
        public BigDecimal getPrecoUnitario() { return precoUnitario; }
        public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public BigDecimal getDescontoPercentual() { return descontoPercentual; }
    public void setDescontoPercentual(BigDecimal descontoPercentual) { this.descontoPercentual = descontoPercentual; }
    public BigDecimal getTotalProdutos() { return totalProdutos; }
    public void setTotalProdutos(BigDecimal totalProdutos) { this.totalProdutos = totalProdutos; }
    public BigDecimal getTotalServicos() { return totalServicos; }
    public void setTotalServicos(BigDecimal totalServicos) { this.totalServicos = totalServicos; }
    public BigDecimal getTotalDesconto() { return totalDesconto; }
    public void setTotalDesconto(BigDecimal totalDesconto) { this.totalDesconto = totalDesconto; }
    public BigDecimal getTotalFinal() { return totalFinal; }
    public void setTotalFinal(BigDecimal totalFinal) { this.totalFinal = totalFinal; }
    public List<ItemPedidoDetalhe> getItens() { return itens; }
    public void setItens(List<ItemPedidoDetalhe> itens) { this.itens = itens; }
}