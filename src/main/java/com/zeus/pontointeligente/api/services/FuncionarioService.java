package com.zeus.pontointeligente.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import com.zeus.pontointeligente.api.entities.Funcionario;

public interface FuncionarioService {

	/**
	 * Busca e retorna um funcionario pelo parametro ID
	 * 
	 * @param funcionario
	 * @return
	 */

	Funcionario persitir(Funcionario funcionario);

	/**
	 * Salva a classe funcionario no banco de dados.
	 * 
	 * @param cpf
	 * @return
	 */
	Optional<Funcionario> buscarPorCpf(String cpf);

	/**
	 * Busca e retorna um funcionario pelo parametro CPF
	 * 
	 * @param email
	 * @return
	 */
	Optional<Funcionario> buscarPorEmail(String email);

	/**
	 * Busca e retorna um funcionario pelo parametro ID
	 * 
	 * @param id
	 * @return
	 */
	Optional<Funcionario> buscarPorId(Long id);

	/**
	 * Lista todos os funcionarios.
	 * 
	 * @return
	 */
	List<Funcionario> findAll(Sort sort);

}
