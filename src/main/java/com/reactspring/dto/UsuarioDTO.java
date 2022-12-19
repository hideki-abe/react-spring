package com.reactspring.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Builder
public class UsuarioDTO {

    private String email;
    private String nome;
    private String senha;

}
