package com.zeus.pontointeligente.api.controller;

import java.util.Date;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zeus.pontointeligente.api.dtos.CadastroPJDto;
import com.zeus.pontointeligente.api.entities.Empresa;
import com.zeus.pontointeligente.api.entities.Funcionario;
import com.zeus.pontointeligente.api.enums.PerfilEnum;
import com.zeus.pontointeligente.api.response.Response;
import com.zeus.pontointeligente.api.services.EmpresaService;
import com.zeus.pontointeligente.api.services.FuncionarioService;
import com.zeus.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("${spring.data.rest.basePath}/cadastrar-pj")
@CrossOrigin(origins = "*")
public class CadastroPJController {

	private static final Logger log = LoggerFactory.getLogger(CadastroPJController.class);

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private EmpresaService empresaService;

	@PostMapping
	public ResponseEntity<Response<CadastroPJDto>> cadastrar(@Valid @RequestBody final CadastroPJDto cadastroPJDto,
			BindingResult result) {
		log.info("Castrando PJ {} ", cadastroPJDto.toString());
		Response<CadastroPJDto> response = new Response<CadastroPJDto>();

		validarDadosExistentes(cadastroPJDto, result);
		Empresa empresa = this.converterDtoParaEmpresa(cadastroPJDto);
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPJDto);

		if (result.hasErrors()) {
			log.error("Erro ao validar dados de cadastro PJ {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.empresaService.presist(empresa);
		funcionario.setEmpresa(empresa);
		this.funcionarioService.persitir(funcionario);

		response.setData(this.converterCadastroPJDto(funcionario));
		return ResponseEntity.ok(response);
	}

	/**
	 * Valida se CNPJ, CPF ou email já estão cadastrados no sistema.
	 * 
	 * @param dto
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPJDto dto, BindingResult result) {
		this.empresaService.buscarPorCnpj(dto.getCnpj())
				.ifPresent(emp -> result.addError(new ObjectError("Empresa", "Empresa já existe!")));
		this.funcionarioService.buscarPorCpf(dto.getCpf())
				.ifPresent(fun -> result.addError(new ObjectError("Funcionario", "CPF já existe!")));
		this.funcionarioService.buscarPorEmail(dto.getEmail())
				.ifPresent(fun -> result.addError(new ObjectError("Funcionario", "Email já existe!")));

	}

	/**
	 * Converte Dto para empresas.
	 * 
	 * @param dto
	 * @return
	 */
	private Empresa converterDtoParaEmpresa(CadastroPJDto dto) {
		Empresa empresa = new Empresa();
		empresa.setDataCriacao(new Date());

		empresa.setCnpj(dto.getCnpj());
		empresa.setRazaoSocial(dto.getRazaoSocial());

		return empresa;
	}

	/**
	 * Converte Dto para funcionarios.
	 * 
	 * @param dto
	 * @return
	 */
	private Funcionario converterDtoParaFuncionario(CadastroPJDto dto) {
		Funcionario funcionario = new Funcionario();

		funcionario.setNome(dto.getNome());
		funcionario.setEmail(dto.getEmail());
		funcionario.setCpf(dto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setDataCriacao(new Date());
	    funcionario.setSenha(PasswordUtils.gerarBCrypt(dto.getSenha()));
		return funcionario;
	}

	/**
	 * Converte a classe funcionario para um CadastroPJDto.
	 *
	 * @param fun
	 * @return
	 */
	private CadastroPJDto converterCadastroPJDto(Funcionario fun) {

		return CadastroPJDto.builder()
							.id(fun.getId())
							.nome(fun.getNome())
							.email(fun.getEmail())
							.cpf(fun.getCpf())
							.razaoSocial(fun.getEmpresa().getRazaoSocial())
							.cnpj(fun.getEmpresa().getCnpj())
							.build();
	}

}
