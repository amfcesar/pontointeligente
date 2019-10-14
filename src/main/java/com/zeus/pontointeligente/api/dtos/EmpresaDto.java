package com.zeus.pontointeligente.api.dtos;

import com.zeus.pontointeligente.api.anotations.ValidaNome;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmpresaDto {
	private Long id;
	@ValidaNome
	private String razaoSocial;
	private String cnpj;
}
