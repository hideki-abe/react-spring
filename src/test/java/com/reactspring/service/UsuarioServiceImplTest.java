package com.reactspring.service;

import com.reactspring.model.Usuario;
import com.reactspring.repository.UsuarioRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioServiceImplTest {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    TestEntityManager entityManager;

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

        //acao
        boolean result = repository.existsByEmail("usuarioteste@email.com");

        //verificacao
        Assertions.assertThat(result).isFalse();

    }

    @Test
    public void devePersistirUmUsuarioNaBaseDeDados() {
        //cenario
        Usuario usuario = criarUsuario();

        //acao
        Usuario usuarioSalvo = repository.save(usuario);

        //verificacao
        Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
    }

    @Test
    public void deveBuscarUmUsuarioPorEmail(){
        //cenario
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);

        //acao
        Optional<Usuario> result = repository.findByEmail("usuario@email.com");

        //verificacao
        Assertions.assertThat(result.isPresent()).isTrue();

    }

    @Test
    public void deveBuscarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase(){
        //acao
        Optional<Usuario> result = repository.findByEmail("usuario@email.com");

        //verificacao
        Assertions.assertThat(result.isPresent()).isFalse();

    }

    public static Usuario criarUsuario(){
        Usuario usuario = new Usuario();
        usuario.setNome("usuario");
        usuario.setEmail("usuario@email.com");
        usuario.setSenha("senha");
        return usuario;
    }

}