package com.zeus.pontointeligente.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.zeus.pontointeligente.api.entities.Empresa;
import com.zeus.pontointeligente.api.repositories.EmpresaRepository;
import com.zeus.pontointeligente.api.services.EmpresaService;

@Service
public class EmpresaServiceImpl implements EmpresaService {

	private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);

	@Autowired
	private EmpresaRepository empresaRepository;

	@Override
	@Cacheable(value = "empresaPorCNPJ")
	public Optional<Empresa> buscarPorCnpj(String cnpj) {
		log.info("Buscando uma empresa para o CNPJ {} ", cnpj);
		return Optional.ofNullable(this.empresaRepository.findByCnpj(cnpj));
	}

	@Override
	@CachePut(value= "empresaPorCNPJ")
	@CacheEvict(allEntries=true, value = "empresaBuscarTodos")
	public Empresa presist(Empresa empresa) {
		log.info("Persistindo empresa {}", empresa);
		return this.empresaRepository.save(empresa);
	}

	@Override
	@Cacheable(value= "empresaBuscarTodos")
	public List<Empresa> findAll(Sort sort) {
		log.info("Busca todos as empresa de forma ordenada ", sort);
		return empresaRepository.findAll(sort);
	}

}
