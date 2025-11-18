package br.com.erp.desafioI.controller;

import br.com.erp.desafioI.domain.ProdutoServico;
import br.com.erp.desafioI.dto.ProdutoServicoDTO;
import br.com.erp.desafioI.service.ProdutoServicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * Endpoints REST para gerenciamento do catálogo (produto/serviço).
 */
@RestController
@RequestMapping("/api/catalogo")
public class ProdutoServicoController {
    private final ProdutoServicoService service;

    public ProdutoServicoController(ProdutoServicoService service) {
        this.service = service;
    }

    /** Cria um item de catálogo */
    @PostMapping
    public ResponseEntity<ProdutoServico> create(@Valid @RequestBody ProdutoServicoDTO dto) {
        ProdutoServico saved = service.create(dto);
        return ResponseEntity.created(URI.create("/api/catalogo/" + saved.getId())).body(saved);
    }

    /** Atualiza um item do catálogo */
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoServico> update(@PathVariable UUID id, @Valid @RequestBody ProdutoServicoDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    /** Busca item por ID */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoServico> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

    /** Lista todo o catálogo */
    @GetMapping
    public ResponseEntity<List<ProdutoServico>> list() {
        return ResponseEntity.ok(service.list());
    }

    /** Remove item por ID */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}