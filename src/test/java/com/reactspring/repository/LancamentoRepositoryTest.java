package com.reactspring.repository;

import com.reactspring.model.Lancamento;
import com.reactspring.model.enums.StatusLancamento;
import com.reactspring.model.enums.TipoLancamento;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

//TESTES DE INTEGRACAO DO REPOSITORIO DE LANCAMENTOS
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LancamentoRepositoryTest {

    @Autowired
    LancamentoRepository repository;

    @Autowired
    TestEntityManager entityManager;

    public static Lancamento criarLancamento() {
        return Lancamento.builder()
                .ano(2019)
                .mes(1)
                .descricao("lancamento qualquer")
                .valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now())
                .build();
    }

    private Lancamento criarEPersistirUmLancamento() {
        Lancamento lancamento = criarLancamento();
        lancamento = entityManager.persist(lancamento);
        return lancamento;
    }

    @Test
    public void deveSalvarUmLancamento() {
        Lancamento lancamento = criarEPersistirUmLancamento();
        assertThat(lancamento.getId()).isNotNull();
    }

    @Test
    public void deveDeletarUmLancamento() {
        Lancamento lancamento = criarEPersistirUmLancamento();

        entityManager.find(Lancamento.class, lancamento.getId());

        repository.delete(lancamento);

        Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
        assertThat(lancamentoInexistente).isNull();
    }

    @Test
    public void deveAtualizarUmLancamento() {
        Lancamento lancamento = criarEPersistirUmLancamento();
        lancamento.setAno(2018);
        lancamento.setDescricao("Teste Atualizar");
        lancamento.setStatus(StatusLancamento.CANCELADO);

        repository.save(lancamento);

        Lancamento lancamentoAtt = entityManager.find(Lancamento.class, lancamento.getId());
        assertThat(lancamentoAtt.getAno()).isEqualTo(2018);
        assertThat(lancamentoAtt.getDescricao()).isEqualTo("Teste Atualizar");
        assertThat(lancamentoAtt.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
    }

    @Test
    public void deveBuscarUmLancamentoPorId() {
        Lancamento lancamento = criarEPersistirUmLancamento();

        Optional<Lancamento> lancamentoEncontrado = repository.findById(Math.toIntExact(lancamento.getId()));
        assertThat(lancamentoEncontrado.isPresent()).isTrue();
    }



}