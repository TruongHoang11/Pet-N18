package N18.haui.Pet_18.domain.entity;

import N18.haui.Pet_18.constant.BookingStatus;
import N18.haui.Pet_18.domain.dto.common.FlagUserDateAuditing;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_bookings")
public class Booking extends FlagUserDateAuditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;

    @Column(name = "actual_price",precision = 10, scale = 2)
    private BigDecimal actualPrice; // gia thuc te thu cua khạc


    @Column(name = "start_time")
    private LocalDateTime startTime; // Khách hẹn lúc mấy giờ, ngày nào

    @Column(name = "end_time")
    private LocalDateTime endTime; // Dự kiến xong lúc mấy giờ để hệ thống chặn không cho người khác đặt trùng

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Khách hàng đặt lịch

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<PetService> services;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff; // Nhân viên thực hiện lịch hẹn đó




}
