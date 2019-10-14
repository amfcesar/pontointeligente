package com.zeus.pontointeligente.api.repositories;

import com.zeus.pontointeligente.api.entities.Empresa;
import com.zeus.pontointeligente.api.entities.Funcionario;
import com.zeus.pontointeligente.api.entities.Lancamento;
import com.zeus.pontointeligente.api.enums.PerfilEnum;
import com.zeus.pontointeligente.api.enums.TipoEnum;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {
	
	@Autowired
	EmpresaRepository empresaRepository;

	@Autowired
	FuncionarioRepository funcionarioRepository;
	
	@Autowired
	LancamentoRepository lacamentoRepository;
	
	private Long funcionarioId;

	@Before
	public void setUp() throws Exception {
		Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());

		Funcionario funcionario = this.funcionarioRepository.save(obterDadosFuncionarios(empresa));

		this.funcionarioId = funcionario.getId();

		this.lacamentoRepository.save(obterDadosLancamento(funcionario));
		this.lacamentoRepository.save(obterDadosLancamento(funcionario));

	}

	@After
	public void tearDown() {
		this.empresaRepository.deleteAll();
	}

	@Test
	public void testBuscarLancamentoPorFuncionarioId() {
		List<Lancamento> lancamentos = this.lacamentoRepository.findByFuncionarioId(funcionarioId);
		assertEquals(2, lancamentos.size());
	}
	
	@Test
	public void testBuscarLancamentosPorFuncionarioIdPaginado() {
		Page<Lancamento> lancamentos = this.lacamentoRepository.findByFuncionarioId(funcionarioId,new PageRequest(1,10));
		assertEquals(2, lancamentos.getTotalElements());
	}

	private Empresa obterDadosEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa do Dia");
		empresa.setCnpj("123456");
		return empresa;
	}

	private Funcionario obterDadosFuncionarios(Empresa empresa) {
		Funcionario funcionario = new Funcionario();

		funcionario.setNome("Fulano de Tal");
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha("111");
		funcionario.setCpf("1111");
		funcionario.setEmail("email@email.com");
		funcionario.setEmpresa(empresa);

		return funcionario;

	}

	private Lancamento obterDadosLancamento(Funcionario funcionario) {
		Lancamento lancamento = new Lancamento();

		lancamento.setData(new Date());
		lancamento.setTipo(TipoEnum.INICIO_ALMOCO);
		lancamento.setFuncionario(funcionario);

		return lancamento;
	}
	
}