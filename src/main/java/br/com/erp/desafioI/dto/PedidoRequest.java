package br.com.erp.desafioI.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO de entrada para criação/atualização de pedidos.
 */
public class PedidoRequest {

    /** Percentual de desconto entre 0 e 100 aplicado somente sobre produtos */
    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private BigDecimal descontoPercentual;

    /** Lista de itens do pedido */
    @NotNull
    private List<ItemPedidoDTO> itens;

    public BigDecimal getDescontoPercentual() {
        return descontoPercentual;
    }

    public void setDescontoPercentual(BigDecimal descontoPercentual) {
        this.descontoPercentual = descontoPercentual;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }
}