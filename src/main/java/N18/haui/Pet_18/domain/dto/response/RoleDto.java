package N18.haui.Pet_18.domain.dto.response;

import java.util.List;

public class RoleDto {

    private Long id;
    private String name;        // Ví dụ: ROLE_ADMIN, ROLE_USER
    private String description; // Mô tả: Quản trị viên hệ thống...
    private boolean activeFlag; // Trạng thái kích hoạt (kế thừa từ lớp Auditing)

    private List<PermissionDto> permissions;
}
