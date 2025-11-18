package br.com.erp.desafioI.dto;

import br.com.erp.desafioI.domain.TipoItem;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO para criação/atualização de itens do catálogo.
 */
public class ProdutoServicoDTO {
    private UUID id;

    /** Nome do produto/serviço */
    @NotBlank
    private String nome;

    /** Preço positivo obrigatório */
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal preco;

    /** Tipo obrigatório: PRODUTO ou SERVICO */
    @NotNull
    private TipoItem tipo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public TipoItem getTipo() {
        return tipo;
    }

    public void setTipo(TipoItem tipo) {
        this.tipo = tipo;
    }
}