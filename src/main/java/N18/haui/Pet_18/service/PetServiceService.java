package N18.haui.Pet_18.service;

import N18.haui.Pet_18.domain.dto.pagination.ResultPaginationDto;
import N18.haui.Pet_18.domain.dto.request.ReqCreateService;
import N18.haui.Pet_18.domain.dto.request.ReqUpdateService;
import N18.haui.Pet_18.domain.dto.response.ServiceDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PetServiceService {

    ServiceDto createService(ReqCreateService req);

    ServiceDto updateService(ReqUpdateService req);

    ServiceDto getServiceById(Long id);

    ResultPaginationDto getAllServices(Pageable pageable);

    ResultPaginationDto searchServices(String keyword, Pageable pageable);

    ResultPaginationDto getServicesByCategory(Long categoryId, Pageable pageable);

    void deleteService(Long id);

    List<ServiceDto> getTopServices(Integer limit);
}
