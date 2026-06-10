package N18.haui.Pet_18.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import N18.haui.Pet_18.constant.UrlConstant;

@Configuration
public class PermissionInterceptorConfiguration implements WebMvcConfigurer {

    @Bean
    PermissionInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
        // Lấy Bean đã được tạo ở Bước 1 ra sử dụng
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String prefix = "/api/v1";

        String[] white_list = {
                "/", "/index.html", "/favicon.ico",
                // Authentication & forgot-password (public)
                prefix + UrlConstant.Auth.REGISTER,
                prefix + UrlConstant.Auth.LOGIN,


                // Public product/service read endpoints (GET)
                prefix + UrlConstant.Product.GET_PRODUCTS,
                prefix + UrlConstant.Product.GET_PRODUCT,
                prefix + UrlConstant.PetService.GET_PET_SERVICE_DETAIL,
                prefix + UrlConstant.PetService.GET_MY_PET_SERVICES,

                // Swagger / OpenAPI
                "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**",

                // Static files / uploads
                "/storage/**", "/uploads/**", "/images/**",

                // Actuator health/info
                "/actuator/health", "/actuator/info"
        };

        registry.addInterceptor(getPermissionInterceptor())
                .excludePathPatterns(white_list); // ngoại trừ các API public không cần check quyền
    }
}
