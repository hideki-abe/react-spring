package com.reactspring.service;

import com.reactspring.model.Usuario;
import com.reactspring.repository.UsuarioRepository;
import org.assertj.core.api.Assertions;
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
public class UsuarioServiceImplTest {

    @Autowired
    UsuarioRepository repository;

    @Test
    public void deveVerificarAExistenciaDeUmEmail() {
        //cenario
        Usuario usuario = new Usuario();
        usuario.setNome("usuario teste");
        usuario.setEmail("usuarioteste@email.com");
        repository.save(usuario);

        //acao /execucao
        boolean result = repository.existsByEmail("usuarioteste@email.com");

        //verificacao
        Assertions.assertThat(result).isTrue();

    }

    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {

        //cenario
        repository.deleteAll();

        //acao
        boolean result = repository.existsByEmail("usuarioteste@email.com");

        //verificacao
        Assertions.assertThat(result).isFalse();

    }

}