package N18.haui.Pet_18.domain.mapper;

import N18.haui.Pet_18.domain.entity.Pet;
import N18.haui.Pet_18.domain.dto.response.PetDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PetMapper {

    PetDto toDto(Pet pet);

    List<PetDto> toDtoList(List<Pet> pets);
}