package com.zeus.pontointeligente.api.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.zeus.pontointeligente.api.entities.Empresa;
import com.zeus.pontointeligente.api.entities.Funcionario;
import com.zeus.pontointeligente.api.enums.PerfilEnum;
import com.zeus.pontointeligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioRepositoryTest {

	@Autowired
	FuncionarioRepository funcionarioRepository;
	
	@Autowired
	EmpresaRepository empresaRepository;

	private static final String EMAIL = "teste@gmail.com";
	private static final String CPF = "123456789";
	
	@Before
	public void setUp() throws Exception {
		Empresa empresa = empresaRepository.save(obterDadosDaEmpresa());
		funcionarioRepository.save(obterDadosFuncionario(empresa));
	}

	@After
	public final void tearDown() {
		funcionarioRepository.deleteAll();
	}
	
	@Test
	public void testBuscaPorEmail() {
		
		Funcionario func = funcionarioRepository.findByEmail(EMAIL);
		assertEquals(EMAIL, func.getEmail());
	}

	@Test
	public void testBuscaPorCpf() {

		Funcionario func = funcionarioRepository.findByCpf(CPF);
		assertEquals(CPF, func.getCpf());
	}

	@Test
	public void testBuscaPorCpfOrEmail() {

		Funcionario func = funcionarioRepository.findByCpfOrEmail(CPF, EMAIL);
		assertNotNull(func);
	}

	@Test
	public void testBuscaPorCpfOrEmailComEmailInvalido() {
		Funcionario func = funcionarioRepository.findByCpfOrEmail(CPF, "invalido@gmail.com");
		assertNotNull(func);
	}

	@Test
	public void testBuscaPorCpfOrEmailComCpfInvalido() {
		Funcionario func = funcionarioRepository.findByCpfOrEmail("2222", EMAIL);
		assertNotNull(func);
	}

	private Funcionario obterDadosFuncionario(Empresa empresa) {

		Funcionario funcionario = new Funcionario();

		funcionario.setNome("Cesar");
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt("12345"));
		funcionario.setCpf(CPF);
		funcionario.setEmail(EMAIL);
		funcionario.setEmpresa(empresa);

		return funcionario;
	}

	private Empresa obterDadosDaEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa Teste");
		empresa.setCnpj("123456");
		return empresa;
	}

}
