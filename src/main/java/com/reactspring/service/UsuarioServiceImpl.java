package com.reactspring.service;

import com.reactspring.exception.RegraNegocioException;
import com.reactspring.model.Usuario;
import com.reactspring.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    private UsuarioRepository repository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository repository){
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        return null;
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if(existe) {
            throw new RegraNegocioException("Ja existe um usuario cadastrado com esse email.");
        }
    }
}
