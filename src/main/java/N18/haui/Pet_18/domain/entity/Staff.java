package N18.haui.Pet_18.domain.entity;

import N18.haui.Pet_18.domain.dto.common.FlagUserDateAuditing;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Entity
@Table(name = "tbl_staffs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Staff extends FlagUserDateAuditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID định danh hồ sơ nhân sự (1, 2, 3...)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // Kết nối trực tiếp sang tài khoản User chính

    private String phone;

    private String address; // Địa chỉ nhà riêng của nhân viên (phân biệt với địa chỉ nhận hàng mua sắm)

    @Column(name = "hire_date")
    private LocalDate hireDate;

    private BigDecimal salary;

    @Column(name = "experience_years")
    private Integer experienceYears;

    private BigDecimal rating;

    @Column(name = "total_reviews")
    private Integer totalReviews;

    @Column(name = "working_start")
    private LocalTime workingStart;

    @Column(name = "working_end")
    private LocalTime workingEnd;

    // Danh sách các lịch hẹn mà Staff này ĐƯỢC PHÂN CÔNG PHỤC VỤ
    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY)
    private List<Booking> bookings;

}
