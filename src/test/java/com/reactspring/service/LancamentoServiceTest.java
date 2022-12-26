package com.reactspring.service;

import com.reactspring.exception.RegraNegocioException;
import com.reactspring.model.Lancamento;
import com.reactspring.model.Usuario;
import com.reactspring.model.enums.StatusLancamento;
import com.reactspring.model.enums.TipoLancamento;
import com.reactspring.repository.LancamentoRepository;
import com.reactspring.repository.LancamentoRepositoryTest;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LancamentoServiceTest {

    @SpyBean
    LancamentoServiceImpl service;

    @MockBean
    LancamentoRepository repository;

    @Test
    public void deveSalvarUmLancamento() {
        //cenario
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doNothing().when(service).validar(lancamentoASalvar);

        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1l);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

        //execucao
        Lancamento lancamento = service.salvar(lancamentoASalvar);

        //verificacao
        Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
        Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
    }

    @Test
    public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
        //cenario
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);

        //execucao e verificacao
        Assertions.catchThrowableOfType( () -> service.salvar(lancamentoASalvar), RegraNegocioException.class );
        //garante que nao chegou nesse metodo save
        Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
    }

    @Test
    public void deveAtualizarUmLancamento() {
        //cenario
        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1l);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);

        Mockito.doNothing().when(service).validar(lancamentoSalvo);

        Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

        //execucao
        service.atualizar(lancamentoSalvo);

        //verificacao
        Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
    }

    @Test
    public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
        //cenario
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();

        //execucao e verificacao
        Assertions.catchThrowableOfType( () -> service.atualizar(lancamentoASalvar), NullPointerException.class );
        Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
    }

    @Test
    public void deveDeletarUmLancamento() {
        //cenario
        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1l);

        //execucao
        service.deletar(lancamentoSalvo);

        //verificacao
        Mockito.verify(repository).delete(lancamentoSalvo);
    }

    @Test
    public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
        //cenario
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();

        //execucao
        Assertions.catchThrowableOfType( () -> service.deletar(lancamento), NullPointerException.class);

        //verificacao
        Mockito.verify(repository, Mockito.never()).delete(lancamento);

    }

    @Test
    public void deveFiltrarLancamentos() {
        //cenario
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);

        List<Lancamento> lista = Arrays.asList(lancamento);
        Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);

        //execucao
        List<Lancamento> resultado = service.buscar(lancamento);

        //verificacoes
        Assertions
                .assertThat(resultado)
                .isNotEmpty()
                .hasSize(1)
                .contains(lancamento);
    }

    @Test
    public void deveAtualizarOStatuSDeUmLancamento() {
        //cenario
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);
        lancamento.setStatus(StatusLancamento.PENDENTE);

        StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
        Mockito.doReturn(lancamento).when(service).atualizar(lancamento);

        //execucao
        service.atualizarStatus(lancamento, novoStatus);

        //verificacoes
        Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
        Mockito.verify(service).atualizar(lancamento);

    }

    @Test
    public void deveObterUmLancamentoPorId() {
        //cenario
        Long id = 1l;

        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(id);

        Mockito.when(repository.findById(Math.toIntExact(id))).thenReturn(Optional.of(lancamento));
        //execucao
        Optional<Lancamento> resultado = service.obterPorId(id);

        //verificacoes
        Assertions.assertThat(resultado.isPresent()).isTrue();

    }

    @Test
    public void deveRetornarVazioQuandoOLancamentoNaoExiste() {
        //cenario
        Long id = 1l;

        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(id);

        Mockito.when(repository.findById(Math.toIntExact(id))).thenReturn(Optional.empty());
        //execucao
        Optional<Lancamento> resultado = service.obterPorId(id);

        //verificacoes
        Assertions.assertThat(resultado.isPresent()).isFalse();

    }

    //teste incompleto
    @Test
    public void deveLancarErrosAoValidarUmLancamento() {
        //cenario
        Lancamento lancamento = new Lancamento();

        Throwable erro = Assertions.catchThrowable( () -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma descricao valida.");

        lancamento.setDescricao("");


        erro = Assertions.catchThrowable( () -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um ano valido.");

        lancamento.setAno(2019);

        erro = Assertions.catchThrowable( () -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mes valido.");

        lancamento.setMes(12);

        erro = Assertions.catchThrowable( () -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuario.");

        lancamento.setUsuario(new Usuario());

        erro = Assertions.catchThrowable( () -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um valor valido.");

        lancamento.setValor(BigDecimal.valueOf(300));

        erro = Assertions.catchThrowable( () -> service.validar(lancamento));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um tipo de Lancamento.");

        lancamento.setTipo(TipoLancamento.RECEITA);

        //verificacao
        Mockito.verify(service, Mockito.times(1)).validar(lancamento);

    }


}
