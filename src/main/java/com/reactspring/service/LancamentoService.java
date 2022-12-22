package com.reactspring.service;

import com.reactspring.model.Lancamento;
import com.reactspring.model.enums.StatusLancamento;

import java.util.List;

public interface LancamentoService {

    Lancamento salvar(Lancamento lancamento);
    Lancamento atualizar(Lancamento lancamento);
    void deletar(Lancamento lancamento);
    List<Lancamento> buscar(Lancamento lancamentoFiltro);
    void atualizarStatus(Lancamento lancamento, StatusLancamento status);

}
