package N18.haui.Pet_18.controller;

import N18.haui.Pet_18.base.RestApiV1;
import N18.haui.Pet_18.base.VsResponseUtil;
import N18.haui.Pet_18.constant.UrlConstant;
import N18.haui.Pet_18.domain.dto.request.ReqSetThumbnailProduct;
import N18.haui.Pet_18.domain.dto.response.CommonResponseDto;
import N18.haui.Pet_18.service.ProductImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestApiV1
public class ProductImageController {
    private final ProductImageService productImageService;

    @PostMapping(UrlConstant.ProductImages.ADD_IMAGES)
    public ResponseEntity<?> addImages(@RequestParam Long productId,
                                       @RequestParam List<MultipartFile> files) throws URISyntaxException, IOException {
        CommonResponseDto commonResponseDto = productImageService.addImages(productId, files);
        return VsResponseUtil.success(HttpStatus.OK, commonResponseDto);
    }

    @DeleteMapping(UrlConstant.ProductImages.DELETE_IMAGE)
    public ResponseEntity<?> deleteImage(@PathVariable(name = "id") Long imageId){
        CommonResponseDto commonResponseDto = productImageService.deleteImage(imageId);
        return VsResponseUtil.success(HttpStatus.OK, commonResponseDto);
    }

    // Đường dẫn ví dụ: PUT /api/v1/products/1/images/5/set-main
    @PutMapping(UrlConstant.ProductImages.SET_MAIN_IMAGE)
    public ResponseEntity<?> changeMainImage(@RequestBody @Valid ReqSetThumbnailProduct reqSetMainImage) {

        CommonResponseDto response = productImageService.changeMainImage(reqSetMainImage);

        return VsResponseUtil.success(HttpStatus.OK, response);
    }
}
