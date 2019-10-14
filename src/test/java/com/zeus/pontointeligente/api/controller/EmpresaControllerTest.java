package com.zeus.pontointeligente.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.zeus.pontointeligente.api.entities.Empresa;
import com.zeus.pontointeligente.api.services.EmpresaService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class EmpresaControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private EmpresaService empresaService;

	private static final String API_EMPRESA_CNPJ = "/api/empresa/cnpj/";
	private static final String API_EMPRESA_EMPRESAS = "/api/empresa/empresas";
	private static final Long ID = Long.valueOf(1);
	private static final String CNPJ = "51463645000100";
	private static final String RAZAO_SOCIAL = "Empresa XYZ";	
	
	@Test
	@WithMockUser
	public void testBuscaEmpresaCNPJInvalido() throws Exception {
		BDDMockito.given(this.empresaService.buscarPorCnpj(Mockito.anyString())).willReturn(Optional.empty());
		
		mvc.perform(MockMvcRequestBuilders.get(API_EMPRESA_CNPJ + CNPJ)
										  .accept(MediaType.APPLICATION_JSON))
										  .andExpect(status().isBadRequest())
										  .andExpect(jsonPath("$.erros").isNotEmpty())
										  .andDo(print());
	}

	@Test
	@WithMockUser(username="cesar", password="123", roles="ADMIN")
	public void testBustaEmpresaCNPJValido() throws Exception {
		
		BDDMockito.given(this.empresaService.buscarPorCnpj(CNPJ)).willReturn(Optional.of(obterDadosEmpresa()));
		
		mvc.perform(MockMvcRequestBuilders.get(API_EMPRESA_CNPJ + CNPJ)
										  .accept(MediaType.APPLICATION_JSON))
										  .andExpect(status().isOk())
										  .andExpect(jsonPath("$.data.id").value(ID))
										  .andExpect(jsonPath("$.data.razaoSocial").value(RAZAO_SOCIAL))
										  .andExpect(jsonPath("$.data.cnpj").value(CNPJ))
										  .andExpect(jsonPath("$.erros").isEmpty())
										  .andDo(print());
										  
	}
	
	@Test
	@WithMockUser
	public void testTodasAsEmpresas() throws Exception {
		
		BDDMockito.given(this.empresaService.findAll(Sort.by("razaoSocial"))).willReturn(Arrays.asList(obterDadosEmpresa()));
		
		mvc.perform(MockMvcRequestBuilders.get(API_EMPRESA_EMPRESAS)
										  .accept(MediaType.APPLICATION_JSON))
										  .andExpect(status().isOk())
										  .andExpect(jsonPath("$[0]").isNotEmpty())
								//		  .andExpect(model().attribute("body", Matchers.hasSize(1)))
										  .andDo(print());
		
	}

	private Empresa obterDadosEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setId(ID);
		empresa.setRazaoSocial(RAZAO_SOCIAL);
		empresa.setCnpj(CNPJ);
		return empresa;
	}

}
