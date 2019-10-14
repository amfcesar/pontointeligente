package com.zeus.pontointeligente.api.dtos;

import java.math.BigDecimal;
import java.util.Optional;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import com.zeus.pontointeligente.api.anotations.ValidaNome;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadastroPFDto {

	private Long id;
	private String nome;
	private String email;
	private String senha;
	private String cpf;
	private Optional<BigDecimal> valorHora = Optional.empty();
	private Optional<String> qtdHorasTrabalhadoDia = Optional.empty();
	private Optional<String> qtdHorasAlmoco = Optional.empty();
	private String cnpj;

	@NotEmpty(message = "Nome não pode ser vazio.")
	//@Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
	@ValidaNome
	public String getNome() {
		return nome;
	}

	@NotEmpty(message = "Email não pode ser vazio.")
	@Length(min = 3, max = 200, message = "Email deve conter entre 3 e 200 caracteres.")
	@Email(message = "Email inválido.")
	public String getEmail() {
		return email;
	}

	@NotEmpty(message = "Senha não pode ser vazio.")
	@Length(min = 3, max = 200, message = "Senha deve conter entre 3 e 200 caracteres.")
	public String getSenha() {
		return senha;
	}

	@NotEmpty(message = "Nome não pode ser vazio.")
	@CPF(message = "CPF inválido.")
	public String getCpf() {
		return cpf;
	}
	@NotEmpty(message = "CNPJ não pode ser vazio.")
	@CNPJ(message = "CNPJ inválido.")
	public String getCnpj() {
		return cnpj;
	}

}

