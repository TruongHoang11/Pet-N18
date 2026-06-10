package N18.haui.Pet_18;

import N18.haui.Pet_18.constant.RoleConstant;
import N18.haui.Pet_18.constant.UrlConstant;
import N18.haui.Pet_18.domain.entity.Permission;
import N18.haui.Pet_18.domain.entity.Role;
import N18.haui.Pet_18.domain.entity.User;
import N18.haui.Pet_18.repository.PermissionRepository;
import N18.haui.Pet_18.repository.RoleRepository;
import N18.haui.Pet_18.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class Pet18Application {
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;

	public static void main(String[] args) {
		SpringApplication.run(Pet18Application.class, args);
	}

    @Bean
	CommandLineRunner init() {
        return args -> {
            long countPermission = permissionRepository.count();
            if(countPermission == 0){
                List<Permission> permissionList = new ArrayList<>();
                // User Module


                // Product Module
                permissionList.add(new Permission("Get all products", "/api/v1" +UrlConstant.Product.GET_PRODUCTS, "GET", "PRODUCT"));
                permissionList.add(new Permission("Get product by id", "/api/v1" +UrlConstant.Product.GET_PRODUCT, "GET", "PRODUCT"));


                // Order Module
                permissionList.add(new Permission("Create order from cart", "/api/v1" + UrlConstant.Order.CREATE_ORDER_FROM_CART, "POST", "ORDER"));
                permissionList.add(new Permission("Create order buy now", "/api/v1" + UrlConstant.Order.CREATE_ORDER_FROM_BUY_NOW, "POST", "ORDER"));
                permissionList.add(new Permission("Get my orders", "/api/v1" + UrlConstant.Order.GET_MY_ORDERS, "GET", "ORDER"));
                permissionList.add(new Permission("Get order detail", "/api/v1" + UrlConstant.Order.GET_ORDER_DETAIL, "GET", "ORDER"));
                permissionList.add(new Permission("Cancel order", "/api/v1" + UrlConstant.Order.CANCEL_ORDER, "PATCH", "ORDER"));

                // Pet Module
                permissionList.add(new Permission("Get my pets", "/api/v1" + UrlConstant.Pet.GET_MY_PETS, "GET", "PET"));
                permissionList.add(new Permission("Get pet detail", "/api/v1" + UrlConstant.Pet.GET_PET_DETAIL, "GET", "PET"));
                permissionList.add(new Permission("Create a pet", "/api/v1" + UrlConstant.Pet.CREATE_PET, "POST", "PET"));
                permissionList.add(new Permission("Update a pet", "/api/v1" + UrlConstant.Pet.UPDATE_PET, "PUT", "PET"));
                permissionList.add(new Permission("Delete a pet", "/api/v1" + UrlConstant.Pet.DELETE_PET, "DELETE", "PET"));

                // Pet Service Module
//                permissionList.add(new Permission("Get my pet services", UrlConstant.PetService.GET_MY_PET_SERVICES, "GET", "PET_SERVICE"));
//                permissionList.add(new Permission("Get pet service detail", UrlConstant.PetService.GET_PET_SERVICE_DETAIL, "GET", "PET_SERVICE"));
//                permissionList.add(new Permission("Create a pet service", UrlConstant.PetService.CREATE_PET_SERVICE, "POST", "PET_SERVICE"));
//                permissionList.add(new Permission("Update a pet service", UrlConstant.PetService.UPDATE_PET_SERVICE, "PUT", "PET_SERVICE"));
//                permissionList.add(new Permission("Delete a pet service", UrlConstant.PetService.DELETE_PET_SERVICE, "DELETE", "PET_SERVICE"));
//                permissionList.add(new Permission("Get all pet services (Admin)", UrlConstant.PetService.GET_ALL_PET_SERVICES, "GET", "PET_SERVICE"));
//                permissionList.add(new Permission("Deactivate a pet service (Admin)", UrlConstant.PetService.PATCH_DEACTIVATE_PET_SERVICE, "PATCH", "PET_SERVICE"));
//                permissionList.add(new Permission("Activate a pet service (Admin)", UrlConstant.PetService.PATCH_ACTIVATE_PET_SERVICE, "PATCH", "PET_SERVICE"));

            // Product Review Module
                permissionList.add(new Permission("Get reviews by product", "/api/v1" + UrlConstant.ProductReview.GET_REVIEWS_BY_PRODUCT, "GET", "REVIEW"));
                permissionList.add(new Permission("Create a review", "/api/v1" + UrlConstant.ProductReview.CREATE_REVIEW, "POST", "REVIEW"));
                permissionList.add(new Permission("Update a review", "/api/v1" + UrlConstant.ProductReview.UPDATE_REVIEW, "PUT", "REVIEW"));
                permissionList.add(new Permission("Delete a review", "/api/v1" + UrlConstant.ProductReview.DELETE_REVIEW, "DELETE", "REVIEW"));


                // Shipping Address Module
                permissionList.add(new Permission("Create a shipping address", "/api/v1" + UrlConstant.ShippingAddress.CREATE_SHIPPING_ADDRESS, "POST", "SHIPPING_ADDRESS"));
                permissionList.add(new Permission("Get all shipping addresses", "/api/v1" + UrlConstant.ShippingAddress.GET_SHIPPING_ADDRESSES, "GET", "SHIPPING_ADDRESS"));
                permissionList.add(new Permission("Set default shipping address", "/api/v1" + UrlConstant.ShippingAddress.SET_DEFAULT_ADDRESS, "PATCH", "SHIPPING_ADDRESS"));
                permissionList.add(new Permission("Update a shipping address", "/api/v1" + UrlConstant.ShippingAddress.UPDATE_SHIPPING_ADDRESS, "PUT", "SHIPPING_ADDRESS"));
                permissionList.add(new Permission("Delete a shipping address", "/api/v1" + UrlConstant.ShippingAddress.DELETE_SHIPPING_ADDRESS, "DELETE", "SHIPPING_ADDRESS"));

                // Cart Module
                permissionList.add(new Permission("Get cart", "/api/v1" + UrlConstant.Cart.GET_CART, "GET", "CART"));
                permissionList.add(new Permission("Delete cart", "/api/v1" + UrlConstant.Cart.DELETE_CART, "DELETE", "CART"));

                // Cart Item Module
                permissionList.add(new Permission("Add item to cart", "/api/v1" + UrlConstant.CartItem.ADD_CART_ITEM, "POST", "CART_ITEM"));
                permissionList.add(new Permission("Update cart item", "/api/v1" + UrlConstant.CartItem.UPDATE_CART_ITEM, "PUT", "CART_ITEM"));
                permissionList.add(new Permission("Delete cart item", "/api/v1" + UrlConstant.CartItem.DELETE_CART_ITEM, "DELETE", "CART_ITEM"));

                this.permissionRepository.saveAll(permissionList);
            }
            //init role
            if (roleRepository.count() == 0) {
                List<Permission> permissionList = permissionRepository.findAll();
                roleRepository.save(new Role(null, RoleConstant.ADMIN, "Role for admin"));
                roleRepository.save(new Role(RoleConstant.USER, "Role for user",true, permissionList));
                roleRepository.save(new Role(null, RoleConstant.STAFF, "Role for staff"));



            }
            //init admin
            if (userRepository.count() == 0) {
                Role role = roleRepository.findByName(RoleConstant.ADMIN).orElse(null);
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("admin@123"));
                admin.setRole(role);
                userRepository.save(admin);
            }
        };
    }
}
