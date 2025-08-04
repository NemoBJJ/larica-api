package com.larica.mapper;

import com.larica.dto.RestauranteComDonoDTO;
import com.larica.dto.RestauranteDTO;
import com.larica.entity.Restaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = DonoRestauranteMapper.class)
public interface RestauranteMapper {
    RestauranteMapper INSTANCE = Mappers.getMapper(RestauranteMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "endereco", source = "endereco")
    @Mapping(target = "telefone", source = "telefone")
    RestauranteDTO toDTO(Restaurante restaurante);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "endereco", source = "endereco")
    @Mapping(target = "telefone", source = "telefone")
    @Mapping(target = "dono", source = "donoRestaurante")
    RestauranteComDonoDTO toComDonoDTO(Restaurante restaurante);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "endereco", source = "endereco")
    @Mapping(target = "telefone", source = "telefone")
    @Mapping(target = "donoRestaurante", ignore = true) // Ignora o dono na conversão inversa
    Restaurante toEntity(RestauranteDTO dto);
}