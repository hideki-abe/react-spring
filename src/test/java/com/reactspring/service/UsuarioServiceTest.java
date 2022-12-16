package com.reactspring.service;

import com.reactspring.exception.RegraNegocioException;
import com.reactspring.model.Usuario;
import com.reactspring.repository.UsuarioRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @Autowired
    UsuarioService service;

    @Autowired
    UsuarioRepository repository;

    @Test(expected = Test.None.class)
    public void deveValidarEmail(){
        //cenario
        repository.deleteAll();

        //acao
        service.validarEmail("email@email.com");

    }

    @Test(expected = RegraNegocioException.class)
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
        //cenario
        Usuario usuario = new Usuario();
        usuario.setNome("usuario teste");
        usuario.setEmail("usuarioteste@email.com");
        repository.save(usuario);

        //acao
        service.validarEmail("usuarioteste@email.com");

    }



}