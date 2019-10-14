package com.zeus.pontointeligente.api.constants;

public interface ConstantsQuerys {
	
	public String LANCAMENTO_REPOSITORY_FIND_BY_FUNCIONARIOID = "SELECT lanc FROM Lancamento lanc "
																	+ "WHERE lanc.funcionario.id = :funcionarioId ";
	
	public static final String EMPRESA_FIND_ALL = "SELECT emp FROM Empresa emp";
}
