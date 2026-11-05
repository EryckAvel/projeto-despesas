package com.eryckavel.repository;

import com.eryckavel.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    List<Despesa> findByUsuarioId(Long idUsuario);

    Optional<Despesa> findByIdAndUsuarioId(Long id, Long idUsuario);
}