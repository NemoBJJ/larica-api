package com.larica.mapper;

import com.larica.dto.DonoRestauranteDTO;
import com.larica.entity.DonoRestaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = UsuarioMapper.class)
public interface DonoRestauranteMapper {
    DonoRestauranteMapper INSTANCE = Mappers.getMapper(DonoRestauranteMapper.class);

    @Mapping(target = "usuario", source = "usuario")
    DonoRestauranteDTO toDTO(DonoRestaurante donoRestaurante);
    
    DonoRestaurante toEntity(DonoRestauranteDTO dto);
}