package br.com.erp.desafioI.service;

import br.com.erp.desafioI.domain.ProdutoServico;
import br.com.erp.desafioI.dto.ProdutoServicoDTO;
import br.com.erp.desafioI.repository.ProdutoServicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * Serviço de aplicação para operações de catálogo (produto/serviço).
 */
@Service
@Transactional
public class ProdutoServicoService {
    private final ProdutoServicoRepository repository;

    public ProdutoServicoService(ProdutoServicoRepository repository) {
        this.repository = repository;
    }

    /** Cria um item de catálogo a partir do DTO */
    public ProdutoServico create(ProdutoServicoDTO dto) {
        ProdutoServico entity = new ProdutoServico(null, dto.getNome(), dto.getPreco(), dto.getTipo());
        return repository.save(entity);
    }

    /** Atualiza um item de catálogo existente */
    public ProdutoServico update(UUID id, ProdutoServicoDTO dto) {
        ProdutoServico entity = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Produto/Serviço não encontrado"));
        entity.setNome(dto.getNome());
        entity.setPreco(dto.getPreco());
        entity.setTipo(dto.getTipo());
        return repository.save(entity);
    }

    @Transactional(readOnly = true)
    /** Busca um item por ID */
    public ProdutoServico get(UUID id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Produto/Serviço não encontrado"));
    }

    @Transactional(readOnly = true)
    /** Lista todo o catálogo */
    public List<ProdutoServico> list() {
        return repository.findAll();
    }

    /** Remove um item por ID */
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Produto/Serviço não encontrado");
        }
        repository.deleteById(id);
    }
}