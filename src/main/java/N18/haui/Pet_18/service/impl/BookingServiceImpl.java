package N18.haui.Pet_18.service.impl;

import N18.haui.Pet_18.constant.BookingStatus;
import N18.haui.Pet_18.domain.dto.pagination.ResultPaginationDto;
import N18.haui.Pet_18.domain.dto.request.ReqCreateBooking;
import N18.haui.Pet_18.domain.dto.response.BookingDto;
import N18.haui.Pet_18.domain.entity.Booking;
import N18.haui.Pet_18.domain.entity.BookingDetail;
import N18.haui.Pet_18.domain.entity.PetService;
import N18.haui.Pet_18.domain.entity.User;
import N18.haui.Pet_18.domain.mapper.BookingMapper;
import N18.haui.Pet_18.exception.BadRequestException;
import N18.haui.Pet_18.exception.NotFoundException;
import N18.haui.Pet_18.repository.BookingDetailRepository;
import N18.haui.Pet_18.repository.BookingRepository;
import N18.haui.Pet_18.repository.PetServiceRepository;
import N18.haui.Pet_18.repository.UserRepository;
import N18.haui.Pet_18.service.BookingService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
// @RequiredArgsConstructor
@Slf4j
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final PetServiceRepository petServiceRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDto createBooking(ReqCreateBooking req) {
        log.info("[BOOKING] Starting to create booking for user: {}", req.getUserId());

        // ============ VALIDATION 1: Check start time < end time ============
        if (req.getStartTime().isAfter(req.getEndTime())) {
            throw new BadRequestException("[BOOKING] Start time must be before end time");
        }
        LocalDateTime startDateTime = LocalDateTime.of(req.getBookingDate(), req.getStartTime());
        // LocalDateTime endDateTime = LocalDateTime.of(req.getBookingDate(), req.getEndTime());

    
        // ============ VALIDATION 2: Check if start time and end time are in future ============
        if (startDateTime.isBefore(java.time.LocalDateTime.now())) {
            throw new BadRequestException("[BOOKING] Booking start time cannot be in the past");
        }

        // ============ VALIDATION 3: Check user exists ============
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new NotFoundException("[BOOKING] User not found with ID: " + req.getUserId()));
        log.info("[BOOKING] User found: {}", user.getName());

        // ============ VALIDATION 4: Check all services exist and enabled ============
        List<PetService> services = new ArrayList<>();
        for (Long serviceId : req.getServiceIds()) {
            PetService service = petServiceRepository.findById(serviceId)
                    .orElseThrow(() -> new NotFoundException("[BOOKING] Service not found with ID: " + serviceId));

            if (!N18.haui.Pet_18.constant.Status.ENABLE.equals(service.getStatus())) {
                throw new BadRequestException("[BOOKING] Service is disabled: " + service.getName());
            }

            services.add(service);
        }
        log.info("[BOOKING] All services validated. Total services: {}", services.size());

        // ============ VALIDATION 5: Check total duration ============
        long totalDurationMin = services.stream()
                .mapToLong(s -> s.getDurationMin())
                .sum();

        Duration availableDuration = Duration.between(req.getStartTime(), req.getEndTime());
        long availableMinutes = availableDuration.toMinutes();

        if (totalDurationMin > availableMinutes) {
            throw new BadRequestException(
                    String.format("[BOOKING] Total service duration (%d min) exceeds available time window (%d min)",
                            totalDurationMin, availableMinutes)
            );
        }
        log.info("[BOOKING] Duration validation passed. Total: {} min, Available: {} min",
                totalDurationMin, availableMinutes);

        // ============ CREATE BOOKING ============
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setStartTime(req.getStartTime());
        booking.setEndTime(req.getEndTime());
        booking.setStatus(BookingStatus.PENDING);  // Set initial status as PENDING
        if (req.getPetId() != null) {
            // Validate pet if provided
            booking.setPet(null);  // Will be set if pet service supports it
        }

        Booking savedBooking = bookingRepository.save(booking);
        log.info("[BOOKING] Booking created with ID: {}", savedBooking.getId());

        // ============ CREATE BOOKING DETAILS ============
        List<BookingDetail> bookingDetails = new ArrayList<>();
        for (PetService service : services) {
            BookingDetail detail = BookingDetail.builder()
                    .booking(savedBooking)
                    .service(service)
                    .build();
            bookingDetails.add(detail);
        }

        bookingDetailRepository.saveAll(bookingDetails);
        log.info("[BOOKING] Booking details saved. Count: {}", bookingDetails.size());

        // ============ RETURN RESPONSE ============
        savedBooking.setBookingDetails(bookingDetails);  // Set services for mapping
        BookingDto response = bookingMapper.toDto(savedBooking);
        log.info("[BOOKING] Booking created successfully with ID: {}", savedBooking.getId());

        return response;
    }

    @Override
    public BookingDto getBookingById(Long id) {
        log.info("[BOOKING] Getting booking with ID: {}", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("[BOOKING] Booking not found with ID: " + id));

        return bookingMapper.toDto(booking);
    }

    @Override
    public ResultPaginationDto getMyBookings(Pageable pageable) {
        log.info("[BOOKING] Getting my bookings with pagination");

        // Get current user (you should implement this in UserService)
        // For now, assuming we can get from context
        Long userId = 1L;  // Placeholder - should get from SecurityContext

        Page<Booking> page = bookingRepository.findByUserId(userId, pageable);
        List<BookingDto> dtos = page.getContent().stream()
                .map(bookingMapper::toDto)
                .toList();

        return buildPaginationResponse(dtos, page);
    }

    @Override
    public ResultPaginationDto getBookingsByStatus(String status, Pageable pageable) {
        log.info("[BOOKING] Getting bookings by status: {}", status);

        try {
            BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
            Page<Booking> page = bookingRepository.findByStatus(bookingStatus, pageable);
            List<BookingDto> dtos = page.getContent().stream()
                    .map(bookingMapper::toDto)
                    .toList();

            return buildPaginationResponse(dtos, page);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("[BOOKING] Invalid status: " + status);
        }
    }

    @Override
    public ResultPaginationDto getAllBookings(Pageable pageable) {
        log.info("[BOOKING] Getting all bookings with pagination");

        Page<Booking> page = bookingRepository.findAll(pageable);
        List<BookingDto> dtos = page.getContent().stream()
                .map(bookingMapper::toDto)
                .toList();

        return buildPaginationResponse(dtos, page);
    }

    @Override
    @Transactional
    public BookingDto cancelBooking(Long id) {
        log.info("[BOOKING] Canceling booking with ID: {}", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("[BOOKING] Booking not found with ID: " + id));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("[BOOKING] Booking is already cancelled");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BadRequestException("[BOOKING] Cannot cancel completed booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking updatedBooking = bookingRepository.save(booking);
        log.info("[BOOKING] Booking cancelled successfully");

        return bookingMapper.toDto(updatedBooking);
    }

    @Override
    @Transactional
    public BookingDto updateBookingStatus(Long id, String status) {
        log.info("[BOOKING] Updating booking status with ID: {} to {}", id, status);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("[BOOKING] Booking not found with ID: " + id));

        try {
            BookingStatus newStatus = BookingStatus.valueOf(status.toUpperCase());
            booking.setStatus(newStatus);
            Booking updatedBooking = bookingRepository.save(booking);
            log.info("[BOOKING] Booking status updated successfully");

            return bookingMapper.toDto(updatedBooking);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("[BOOKING] Invalid status: " + status);
        }
    }

    private <T> ResultPaginationDto buildPaginationResponse(List<T> data, Page<?> page) {
        ResultPaginationDto response = new ResultPaginationDto();
        response.setResult(data);

        ResultPaginationDto.Meta meta = new ResultPaginationDto.Meta();
        meta.setPage(page.getNumber());
        meta.setPageSize(page.getSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        response.setMeta(meta);

        return response;
    }
}
