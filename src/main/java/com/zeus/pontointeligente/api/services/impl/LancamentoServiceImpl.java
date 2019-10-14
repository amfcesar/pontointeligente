package com.zeus.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.zeus.pontointeligente.api.entities.Lancamento;
import com.zeus.pontointeligente.api.repositories.LancamentoRepository;
import com.zeus.pontointeligente.api.services.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	private static Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Override
	public Page<Lancamento> buscaPorFuncionarioId(Long funcionarioId, Pageable pageRequest) {
		log.info("Busca lancamento por id do funcionario {}", funcionarioId);
		return this.lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
	}

	@Override
	@Cacheable(value="lancamentoPorId")
	public Optional<Lancamento> buscarPorId(Long id) {
		log.info("Busca pelo Id do lancamento {} ", id);
		return this.lancamentoRepository.findById(id);
	}

	@Override
	@CachePut(value="lancamentoPorId")
	public Lancamento persistir(Lancamento lancamento) {
		log.info("Salva o lacamento {} ", lancamento);
		return this.lancamentoRepository.save(lancamento);
	}

	@Override
	public void remover(Long id) {
		log.info("Remove o lancamento pelo id {}", id);
		this.lancamentoRepository.deleteById(id);
	}

}
