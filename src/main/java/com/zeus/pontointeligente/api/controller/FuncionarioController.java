package com.zeus.pontointeligente.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.zeus.pontointeligente.api.dtos.FuncionarioDto;
import com.zeus.pontointeligente.api.entities.Funcionario;
import com.zeus.pontointeligente.api.response.Response;
import com.zeus.pontointeligente.api.services.FuncionarioService;
import com.zeus.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("${spring.data.rest.basePath}/funcionario")
@CrossOrigin(origins="*")
public class FuncionarioController {
	
	private static final Logger log = LoggerFactory.getLogger(FuncionarioController.class);
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<FuncionarioDto>> atualizar(@NotEmpty @PathVariable("id") Long id,
			@Valid @RequestBody FuncionarioDto funcionarioDto, BindingResult result ) {
		
		log.info("Atualiando funcionario {} ", funcionarioDto.toString());
		Response<FuncionarioDto> response = new Response<FuncionarioDto>();
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(id);
		
		if(!funcionario.isPresent()) {
			result.addError(new ObjectError("Funcionario", "Funcionario não encontrado "));
		}
		
		this.atualizarDadosFuncionarios(funcionario.get(), funcionarioDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro ao validar os dados do funcionario {} ", result.getAllErrors());
			result.getAllErrors().forEach(err -> response.getErros().add(err.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.funcionarioService.persitir(funcionario.get());
		response.setData(modelMapper.map(funcionario.get(), FuncionarioDto.class));
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/funcionarios")
	public ResponseEntity<List<FuncionarioDto>> listaFuncionarios() {
		
		List<FuncionarioDto> listaDto = new ArrayList<FuncionarioDto>();
		this.funcionarioService.findAll(Sort.by("nome")).forEach(fun -> {
			listaDto.add(modelMapper.map(fun, FuncionarioDto.class));
		});
		return ResponseEntity.ok(listaDto);
	}
	
	
	private void atualizarDadosFuncionarios(Funcionario funcionario, FuncionarioDto funcionarioDto,
			BindingResult result) {

		funcionario.setNome(funcionarioDto.getNome());
		if (!funcionario.getEmail().equals(funcionarioDto.getEmail())) {
			this.funcionarioService.buscarPorEmail(funcionarioDto.getEmail())
					.ifPresent(func -> result.addError(new ObjectError("email", "Email já existe")));
			funcionario.setEmail(funcionarioDto.getEmail());
		}
		
		funcionarioDto.getQtdHorasAlmoco()
				.ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		
		funcionarioDto.getQtdHorasTrabalhoDia()
				.ifPresent(qtdHorasTrabalho -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalho)));
		
		funcionarioDto.getValorHora()
				.ifPresent(valorHora -> funcionario.setValorHora(valorHora));
		
		if(!Strings.isNullOrEmpty(funcionarioDto.getSenha())) {
			funcionario.setSenha(PasswordUtils.gerarBCrypt(funcionarioDto.getSenha()));
		}
		
	}
	
}
