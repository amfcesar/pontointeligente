package com.zeus.pontointeligente.api.dtos;

import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import com.zeus.pontointeligente.api.enums.TipoEnum;

import lombok.Data;

@Data
public class LancamentoDto {
	
	private Long id;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private String data;
	private TipoEnum tipo;
	private String descricao;
	private String localizacao;
	private Long funcionarioId;
	
	@NotEmpty(message = "Data não pode ser vazia")
	public String getData() {
		return data;
	}
	
}
