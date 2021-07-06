package com.example.recetascocina.dao;

import com.example.recetascocina.dto.Usuario;

public interface UsuariosDAO {
    Usuario save(Usuario usuario);
    boolean getUser(String nickname);
    Usuario getUser();
}
