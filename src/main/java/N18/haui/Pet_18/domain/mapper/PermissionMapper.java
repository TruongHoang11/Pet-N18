package N18.haui.Pet_18.domain.mapper;

import N18.haui.Pet_18.domain.dto.request.ReqPermissionDto;
import N18.haui.Pet_18.domain.dto.response.PermissionDto;
import N18.haui.Pet_18.domain.entity.Permission;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionDto entityToDto(Permission permission);

    PermissionDto dtoToDto(ReqPermissionDto reqPermissionDto);

    List<PermissionDto> entityToDtoList(List<Permission> permissions);

    List<PermissionDto> dtoToDtoList(List<ReqPermissionDto> reqPermissionDtoList);
}
