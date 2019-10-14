package com.zeus.pontointeligente.api.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import com.zeus.pontointeligente.api.anotations.ValidaNome;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CadastroPJDto {

	private Long id;
	private String nome;
	private String email;
	private String senha;
	private String cpf;
	private String razaoSocial;
	private String cnpj;

	@NotEmpty(message = "Nome não pode ser vazio.")
	//@Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
	@ValidaNome
	public String getNome() {
		return nome;
	}

	@NotEmpty(message = "Email não pode ser vazio.")
	@Length(min = 3, max = 200, message = "Email deve conter entre 3 e 200 caracteres.")
	@Email(message = "Email invalido!")
	public String getEmail() {
		return email;
	}

	@NotEmpty(message = "Senha não pode ser vazia.")
	public String getSenha() {
		return senha;
	}

	@NotEmpty(message = "Cpf não pode ser vazio.")
	@CPF(message = "Cpf invalido!")
	public String getCpf() {
		return cpf;
	}

	@NotEmpty(message = "Razão social não pode ser vazio.")
	@Length(message = "Rozão social deve conter entre 5 e 200 caracteres.")
	public String getRazaoSocial() {
		return razaoSocial;
	}

	@NotEmpty(message = "Cnpj não pode ser vazio")
	@CNPJ(message = "CNPJ invalido")
	public String getCnpj() {
		return cnpj;
	}

}
