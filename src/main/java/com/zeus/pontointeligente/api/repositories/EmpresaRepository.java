package com.zeus.pontointeligente.api.repositories;


import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.zeus.pontointeligente.api.constants.ConstantsQuerys;
import com.zeus.pontointeligente.api.entities.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

	@Transactional(readOnly = true)
	Empresa findByCnpj(String cnpj);
	
	@Query(value = ConstantsQuerys.EMPRESA_FIND_ALL)
	List<Empresa> findAll(Sort sort);

}
