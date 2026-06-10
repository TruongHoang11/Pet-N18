package N18.haui.Pet_18.service;

import N18.haui.Pet_18.domain.dto.pagination.ResultPaginationDto;
import N18.haui.Pet_18.domain.dto.request.ReqCreateBooking;
import N18.haui.Pet_18.domain.dto.response.BookingDto;
import org.springframework.data.domain.Pageable;

public interface BookingService {

    BookingDto createBooking(ReqCreateBooking req);

    BookingDto getBookingById(Long id);

    ResultPaginationDto getMyBookings(Pageable pageable);

    ResultPaginationDto getBookingsByStatus(String status, Pageable pageable);

    ResultPaginationDto getAllBookings(Pageable pageable);

    BookingDto cancelBooking(Long id);

    BookingDto updateBookingStatus(Long id, String status);
}
