package com.zeus.pontointeligente.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zeus.pontointeligente.api.entities.Lancamento;

public interface LancamentoService {

	/**
	 * 
	 * @param funcionarioId
	 * @param pageRequest
	 * @return
	 */

	Page<Lancamento> buscaPorFuncionarioId(Long funcionarioId, Pageable pageRequest);

	/**
	 * 
	 * @param id
	 * @return
	 */

	Optional<Lancamento> buscarPorId(Long id);

	/**
	 * 
	 * @param lancamento
	 * @return
	 */
	Lancamento persistir(Lancamento lancamento);

	/**
	 * 
	 * @param id
	 */
	void remover(Long id);
}
