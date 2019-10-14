package com.zeus.pontointeligente.api.repositories;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.zeus.pontointeligente.api.constants.ConstantsQuerys;
import com.zeus.pontointeligente.api.entities.Lancamento;

@NamedQueries({
		@NamedQuery(name = "LancamentoRepository.findByFuncionarioId", query = ConstantsQuerys.LANCAMENTO_REPOSITORY_FIND_BY_FUNCIONARIOID) })
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	List<Lancamento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId);

	Page<Lancamento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId, Pageable page);
}
