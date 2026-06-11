package N18.haui.Pet_18.repository;


import N18.haui.Pet_18.domain.entity.User;
import N18.haui.Pet_18.exception.NotFoundException;
import N18.haui.Pet_18.security.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {


    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findById(String id);

    default User getUser(UserPrincipal currentUser) {
        return findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found with email: " + currentUser.getUsername()));
    }

    boolean existsByEmail(String email);


    @Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.role r LEFT JOIN FETCH r.permissions WHERE u.id = :id")
    User findByIdWithFullInfor(String id);
}
