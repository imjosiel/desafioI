package br.com.erp.desafioI.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entidade de catálogo que representa tanto produtos quanto serviços.
 * Diferenciação é feita pelo campo {@link #tipo}.
 */
@Entity
@Table(name = "produto_servico")
public class ProdutoServico {

    /** Identificador único UUID */
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /** Nome do item no catálogo */
    @Column(name = "nome", nullable = false)
    private String nome;

    /** Preço padrão utilizado como preço unitário em itens de pedido */
    @Column(name = "preco", nullable = false, precision = 19, scale = 2)
    private BigDecimal preco;

    /** Tipo do item (PRODUTO ou SERVICO) */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoItem tipo;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = Boolean.TRUE;

    public ProdutoServico() {
    }

    public ProdutoServico(UUID id, String nome, BigDecimal preco, TipoItem tipo) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.tipo = tipo;
    }

    public ProdutoServico(UUID id, String nome, BigDecimal preco, TipoItem tipo, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.tipo = tipo;
        this.ativo = ativo;
    }

    /**
     * Geração automática de UUID ao persistir quando não informado.
     */
    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (ativo == null) {
            ativo = Boolean.TRUE;
        }
    }

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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}