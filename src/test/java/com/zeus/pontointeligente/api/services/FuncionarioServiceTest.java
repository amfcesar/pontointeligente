package com.zeus.pontointeligente.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.zeus.pontointeligente.api.entities.Funcionario;
import com.zeus.pontointeligente.api.repositories.FuncionarioRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioServiceTest {

	@MockBean
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private FuncionarioService funcionarioService;

	@Before
	public void setUp() {
		BDDMockito.given(this.funcionarioRepository.save(Mockito.any(Funcionario.class))).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepository.findByCpf(Mockito.anyString())).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepository.findByEmail(Mockito.anyString())).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepository.findById(Mockito.anyLong()))
				.willReturn(Optional.of(new Funcionario()));
		
		//Mockito.when(this.funcionarioRepository.save(Mockito.any())).thenReturn(new Funcionario());
		
	}

	@Test
	public void testSalvarFuncionario() {
		Funcionario funcionario = this.funcionarioService.persitir(new Funcionario());
		assertNotNull(funcionario);
	}

	@Test
	public void testBuscaFuncionarioPorId() {
		Optional<Funcionario> funcioario = this.funcionarioService.buscarPorId(1L);
		assertTrue(funcioario.isPresent());
	}

	@Test
	public void testBuscaFuncionarioPorEmail() {
		Funcionario funcionario = this.funcionarioRepository.findByEmail("cesar@email.com");
		assertNotNull(funcionario);
	}

	@Test
	public void testBuscaFuncionarioPorCpf() {
		Funcionario funcionario = this.funcionarioRepository.findByCpf("123");
		assertNotNull(funcionario);
	}
}
