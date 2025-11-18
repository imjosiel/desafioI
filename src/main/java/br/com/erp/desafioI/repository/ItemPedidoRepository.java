package br.com.erp.desafioI.repository;

import br.com.erp.desafioI.domain.ItemPedido;
import br.com.erp.desafioI.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

/**
 * Repositório Spring Data para CRUD de {@link br.com.erp.desafioI.domain.ItemPedido}
 * e consultas auxiliares.
 */
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, UUID> {
    /** Busca itens pelo pedido associado */
    List<ItemPedido> findByPedido(Pedido pedido);
}