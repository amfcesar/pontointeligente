package com.zeus.pontointeligente.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import com.zeus.pontointeligente.api.entities.Empresa;

public interface EmpresaService {

	/**
	 * 
	 * Retorna uma empresa dado um Cnpj
	 * 
	 * @param cnpj
	 * @return Optional<Empresa>
	 */

	Optional<Empresa> buscarPorCnpj(String cnpj);

	/**
	 * 
	 * Cadastra uma nova empresa na base de dados.
	 * 
	 * @param empresa
	 * @return Empresa
	 */

	Empresa presist(Empresa empresa);
	
	/**
	 * Busca todas as empresas de forma ordenada.
	 */
	
	List<Empresa> findAll(Sort sort);
}
