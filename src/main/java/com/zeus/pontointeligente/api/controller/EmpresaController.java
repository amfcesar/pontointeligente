package com.zeus.pontointeligente.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotEmpty;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zeus.pontointeligente.api.dtos.EmpresaDto;
import com.zeus.pontointeligente.api.entities.Empresa;
import com.zeus.pontointeligente.api.response.Response;
import com.zeus.pontointeligente.api.services.EmpresaService;

@RestController
@RequestMapping("${spring.data.rest.basePath}/empresa")
@CrossOrigin(origins = "*")
public class EmpresaController {

	private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);

	@Autowired
	EmpresaService empresaService;
	
	@Autowired
	private ModelMapper modelMapper;

	@GetMapping(path = "/cnpj/{cnpj}")
	public ResponseEntity<Response<EmpresaDto>> buscaPorCnpj(@NotEmpty @PathVariable("cnpj") String cnpj) {

		log.info("Buscando empresa por CNPJ: {}", cnpj);

		Response<EmpresaDto> response = new Response<EmpresaDto>();
		Optional<Empresa> empresa = empresaService.buscarPorCnpj(cnpj);

		if (!empresa.isPresent()) {
			log.info("Empresa não encontrada para o CNPJ {}", cnpj);
			response.getErros().add("Empresa não encontrada para o CNPJ " + cnpj);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData( modelMapper.map(empresa.get(), EmpresaDto.class));
		return ResponseEntity.ok(response);
	}

	@GetMapping(path = "/empresas")
	public ResponseEntity<List<EmpresaDto>> listarTodasEmpresas() {
		log.info("Buncando todas as empresas cadastradas!");
		List<EmpresaDto> listDto = new ArrayList<EmpresaDto>();
		empresaService.findAll(Sort.by("razaoSocial").ascending())
				.forEach(emp -> listDto.add(modelMapper.map(emp, EmpresaDto.class)));
		
		return ResponseEntity.ok(listDto);
	}
}
