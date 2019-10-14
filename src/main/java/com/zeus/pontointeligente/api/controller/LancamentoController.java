package com.zeus.pontointeligente.api.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.EnumUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zeus.pontointeligente.api.dtos.LancamentoDto;
import com.zeus.pontointeligente.api.entities.Funcionario;
import com.zeus.pontointeligente.api.entities.Lancamento;
import com.zeus.pontointeligente.api.enums.TipoEnum;
import com.zeus.pontointeligente.api.response.Response;
import com.zeus.pontointeligente.api.services.FuncionarioService;
import com.zeus.pontointeligente.api.services.LancamentoService;


@RestController
@RequestMapping(value="${spring.data.rest.basePath}/lancamento")
@CrossOrigin(origins="*")
public class LancamentoController {

	private static final Logger log = LoggerFactory.getLogger(LancamentoController.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private LancamentoService lancamentoService; 
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private ModelMapper modelMapper; 
	
	@Value("${pagina.quantidade_pagina}")
	private Long qtdPaginas; 
	
	@GetMapping(value = "/funcionario/{funcionarioId}")
	public ResponseEntity<Response<Page<LancamentoDto>>> listarPorFuncionarioId(
			@PathVariable("funcionarioId") Long funcionarioId, 
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {

		log.info("Buscando lançamento por ID do funcionario: {}, página: {} ", funcionarioId, pag);

		Response<Page<LancamentoDto>> response = new Response<Page<LancamentoDto>>();
		
		Pageable pg = PageRequest.of(pag,  Integer.valueOf(this.qtdPaginas.toString()),Direction.valueOf(dir), ord);
		
		Page<Lancamento> lancamentos = lancamentoService.buscaPorFuncionarioId(funcionarioId, pg);
		
		Page<LancamentoDto> lancamentoDto = lancamentos.map(item -> modelMapper.map(item, LancamentoDto.class));
		
		response.setData(lancamentoDto);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<LancamentoDto>> listarPorId(@NotEmpty @PathVariable("id") Long id) {
		
		log.info("Buncando lancamento pod ID: {}", id);
		
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		
		Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);
		
		if(!lancamento.isPresent()) {
			log.error("Lancamento não encontrado para o ID {}", id);
			response.getErros().add("Lancamento não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(modelMapper.map(lancamento.get(), LancamentoDto.class));
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping
	public ResponseEntity<Response<LancamentoDto>> adicionar(@Valid @RequestBody LancamentoDto lancamentoDto,
			BindingResult result) throws ParseException {
		
		log.info("Adicionando lançamento {} " , lancamentoDto.toString());
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		validarFuncionario(lancamentoDto, result);

		Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro ao validar lançamento {} ", result.getAllErrors());
			result.getAllErrors().forEach(err -> response.getErros().add(err.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		lancamento = lancamentoService.persistir(lancamento);
		response.setData(modelMapper.map(lancamento, LancamentoDto.class));
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<LancamentoDto>> atualizar(@NotEmpty @PathVariable("id") Long id, 
			@Valid @RequestBody LancamentoDto lancamentoDto, BindingResult result) throws ParseException {
		
		log.info("Atualiando lançamento {} ", lancamentoDto.toString());
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		validarFuncionario(lancamentoDto, result);
		lancamentoDto.setId(Optional.of(id).get());

		Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);
		
		if(result.hasErrors()) {
			
			log.error("Erro ao validar lancamento {} ", lancamentoDto.toString());
			result.getAllErrors().forEach(err -> response.getErros().add(err.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		lancamento = this.lancamentoService.persistir(lancamento);
		response.setData(modelMapper.map(lancamento, LancamentoDto.class));
		return ResponseEntity.ok().body(response);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> remover(@NotEmpty @PathVariable("id") Long id ) {
		log.info("Deletando lançamento de id {}", id);
		Response<String> response = new Response<String>();
		Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);
		
		if(!lancamento.isPresent()) {
			log.info("Id de lançamento invalido {} ", id);
			response.getErros().add("Erro ao remover lançamento de id " + id + ", o id e invalido" );
			return ResponseEntity.badRequest().body(response);
		}
		
		this.lancamentoService.remover(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	private void validarFuncionario(LancamentoDto lancamentoDto, BindingResult result) {
		if (lancamentoDto.getFuncionarioId() == null) {
			result.addError(new ObjectError("Funcionario", "Funcionário não informado"));
			return;
		}
		log.info("Validando funcionario ID {}: ", lancamentoDto.getFuncionarioId());
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(lancamentoDto.getFuncionarioId());

		if (!funcionario.isPresent()) {
			result.addError(new ObjectError("Funcionario", "Funcionario não encontrado, ID inexistente"));
		}
	}
	
	private Lancamento converterDtoParaLancamento(LancamentoDto lancamentoDto, BindingResult result)
			throws ParseException {
		Lancamento lancamento = new Lancamento();

		if (lancamentoDto.getId() != null) {
			Optional<Lancamento> lanc = this.lancamentoService.buscarPorId(lancamentoDto.getId());
			if (lanc.isPresent()) {
				lancamento = lanc.get();
			} else {
				result.addError(new ObjectError("lancamento", "Lançamento não encontrado."));
			}
		} else {
			lancamento.setFuncionario(new Funcionario());
			lancamento.getFuncionario().setId(lancamentoDto.getFuncionarioId());
		}

		lancamento.setDescricao(lancamentoDto.getDescricao());
		lancamento.setLocalizacao(lancamentoDto.getLocalizacao());
		lancamento.setData(this.dateFormat.parse(lancamentoDto.getData()));

		if (EnumUtils.isValidEnum(TipoEnum.class, lancamentoDto.getTipo().name())) {
			lancamento.setTipo(TipoEnum.valueOf(lancamentoDto.getTipo().name()));
		} else {
			result.addError(new ObjectError("tipo", "Tipo inválido."));
		}

		return lancamento;
	}
}
