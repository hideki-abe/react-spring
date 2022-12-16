package com.reactspring.service;

import com.reactspring.model.Usuario;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);
    Usuario salvarUsuario(Usuario usuario);
    void validarEmail(String email);
}
