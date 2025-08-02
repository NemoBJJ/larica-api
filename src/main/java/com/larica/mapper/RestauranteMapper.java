package com.larica.mapper;

import com.larica.dto.*;
import com.larica.entity.Restaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = UsuarioMapper.class)
public interface RestauranteMapper {
    RestauranteMapper INSTANCE = Mappers.getMapper(RestauranteMapper.class);

    RestauranteDTO toDTO(Restaurante restaurante);
    
    @Mapping(target = "dono", source = "donoRestaurante.usuario")
    RestauranteComDonoDTO toComDonoDTO(Restaurante restaurante);
    
    Restaurante toEntity(RestauranteDTO dto);
}