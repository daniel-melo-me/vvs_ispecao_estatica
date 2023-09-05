package com.vagas.cadastro.service;

import com.vagas.cadastro.config.request.UsuarioEditRequestDTO;
import com.vagas.cadastro.config.request.UsuarioRequestDTO;
import com.vagas.cadastro.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    Page<Usuario> listar(Pageable pageable);

    Usuario editar(Long id, UsuarioEditRequestDTO dto);

    Usuario salvarUsuario(UsuarioRequestDTO dto);

    void deletar(Long id);

    Usuario pesquisar(Long id);

    Page<Usuario> findPagedByFilters(UsuarioRequestDTO filter, Pageable pageable);

    void configurar();
}
