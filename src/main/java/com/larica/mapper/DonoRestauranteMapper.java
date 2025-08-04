// src/main/java/com/larica/mapper/DonoRestauranteMapper.java
package com.larica.mapper;

import com.larica.dto.DonoRestauranteDTO;
import com.larica.entity.DonoRestaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DonoRestauranteMapper {

    // Mapeia automaticamente: id, nome, email, telefone -> se existirem no DTO
    DonoRestauranteDTO toDTO(DonoRestaurante dono);

    // Ignora os campos que não existem no DTO (evita o erro do MapStruct)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    DonoRestaurante toEntity(DonoRestauranteDTO dto);
}
