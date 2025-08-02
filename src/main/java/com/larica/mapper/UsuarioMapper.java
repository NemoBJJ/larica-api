package com.larica.mapper;

import com.larica.dto.UsuarioDTO;
import com.larica.entity.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioDTO toDTO(Usuario usuario);
    Usuario toEntity(UsuarioDTO dto);
}