package br.com.erp.desafioI.repository;

import br.com.erp.desafioI.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

/**
 * Repositório Spring Data para CRUD de {@link br.com.erp.desafioI.domain.Pedido}.
 */
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
}