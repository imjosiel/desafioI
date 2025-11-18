package br.com.erp.desafioI.repository;

import br.com.erp.desafioI.domain.ProdutoServico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

/**
 * Repositório Spring Data para CRUD de {@link br.com.erp.desafioI.domain.ProdutoServico}.
 */
public interface ProdutoServicoRepository extends JpaRepository<ProdutoServico, UUID> {
}