package com.zeus.pontointeligente.api.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Setter;

@Entity
@Setter
@Table(name = "empresa")
public class Empresa extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	private String razaoSocial;
	private String cnpj;
	private List<Funcionario> funcionarios;

	public Empresa() {
	}

	@Column(name = "razao_social", nullable = false)
	public String getRazaoSocial() {
		return razaoSocial;
	}

	@Column(name = "cnpj", nullable = false)
	public String getCnpj() {
		return cnpj;
	}

	@OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

}
