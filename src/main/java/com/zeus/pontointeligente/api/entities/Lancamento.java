package com.zeus.pontointeligente.api.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.zeus.pontointeligente.api.enums.TipoEnum;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "lancamento")
public class Lancamento extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date data;
	private String descricao;
	private String localizacao;
	private TipoEnum tipo;
	private Funcionario funcionario;

	public Lancamento() {

	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data")
	public Date getData() {
		return data;
	}

	@Column(name = "descricao")
	public String getDescricao() {
		return descricao;
	}

	@Column(name = "localizacao")
	public String getLocalizacao() {
		return localizacao;
	}

	@Column(name = "tipo")
	@Enumerated(EnumType.STRING)
	public TipoEnum getTipo() {
		return tipo;
	}

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Funcionario.class)
	public Funcionario getFuncionario() {
		return funcionario;
	}

}
