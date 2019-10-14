package com.zeus.pontointeligente.api.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zeus.pontointeligente.api.dtos.CadastroPFDto;
import com.zeus.pontointeligente.api.entities.Empresa;
import com.zeus.pontointeligente.api.entities.Funcionario;
import com.zeus.pontointeligente.api.enums.PerfilEnum;
import com.zeus.pontointeligente.api.response.Response;
import com.zeus.pontointeligente.api.services.EmpresaService;
import com.zeus.pontointeligente.api.services.FuncionarioService;
import com.zeus.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping(path = "${spring.data.rest.basePath}/cadastrar-pf")
public class CadastroPFController {
	
	private static Logger log = LoggerFactory.getLogger(CadastroPFController.class);

	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@PostMapping
	public ResponseEntity<Response<CadastroPFDto>> cadastrar(@Valid @RequestBody CadastroPFDto cadastroPFDto,
			BindingResult result) {

		log.info("Cadatrando PF {}", cadastroPFDto.toString());
		Response<CadastroPFDto> response = new Response<CadastroPFDto>();

		validarDadosExistentes(cadastroPFDto, result);
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPFDto);

		if (result.hasErrors()) {
			log.error("Erro ao validar dados de pessoas fisica {}", result.getAllErrors());
			result.getAllErrors().forEach(err -> response.getErros().add(err.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
		empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
		this.funcionarioService.persitir(funcionario);

		response.setData(this.converterCadastroPFDto(funcionario));
		return ResponseEntity.ok(response);
	}

	private void validarDadosExistentes(CadastroPFDto dto, BindingResult result) {

		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(dto.getCnpj());
		if (!empresa.isPresent()) {
			result.addError(new ObjectError("Empresa", "Empresa não cadastrada!"));
		}

		this.funcionarioService.buscarPorCpf(dto.getCpf())
				.ifPresent(fun -> result.addError(new ObjectError("Funcionario", "CPF já cadastrado")));
		this.funcionarioService.buscarPorEmail(dto.getEmail())
				.ifPresent(fun -> result.addError(new ObjectError("Funcionario", "Email já cadastrado")));
	}

	private Funcionario converterDtoParaFuncionario(CadastroPFDto dto) {
		Funcionario funcionario = new Funcionario();

		funcionario.setNome(dto.getNome());
		funcionario.setEmail(dto.getEmail());
		funcionario.setCpf(dto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(dto.getSenha()));
		funcionario.getQtdHorasAlmocoOpt().ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(qtdHorasAlmoco));
		funcionario.getQtdHorasTrabalhoDiaOpt()
				.ifPresent(qtdHorasTrabalhadasDia -> funcionario.setQtdHorasTrabalhoDia(qtdHorasTrabalhadasDia));
		funcionario.getValorHoraOpt().ifPresent(valorHora -> dto.setValorHora(Optional.ofNullable(valorHora)));

		return funcionario;
	}

	private CadastroPFDto converterCadastroPFDto(Funcionario fun) {

		CadastroPFDto dto = new CadastroPFDto();
		dto.setId(fun.getId());
		dto.setNome(fun.getNome());
		dto.setEmail(fun.getEmail());
		dto.setCpf(fun.getCpf());
		dto.setCnpj(fun.getEmpresa().getCnpj());
		fun.getQtdHorasTrabalhoDiaOpt()
				.ifPresent(qtd -> dto.setQtdHorasTrabalhadoDia(Optional.of(Float.toString(qtd))));
		fun.getQtdHorasAlmocoOpt().ifPresent(qtd -> dto.setQtdHorasAlmoco(Optional.of(Float.toString(qtd))));
		fun.getValorHoraOpt().ifPresent(qtd -> dto.setValorHora(Optional.of(qtd)));

		return dto;
	}

}
