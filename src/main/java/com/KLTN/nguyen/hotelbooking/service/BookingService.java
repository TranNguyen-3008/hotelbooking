package com.KLTN.nguyen.hotelbooking.service;

import com.KLTN.nguyen.hotelbooking.dto.request.BookingRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.BookingResponse;
import com.KLTN.nguyen.hotelbooking.dto.response.HotelRevenue;
import com.KLTN.nguyen.hotelbooking.entity.*;
import com.KLTN.nguyen.hotelbooking.mapper.BookingMapper;
import com.KLTN.nguyen.hotelbooking.mapper.PaymentMethodRepository;
import com.KLTN.nguyen.hotelbooking.repository.BookingRepository;
import com.KLTN.nguyen.hotelbooking.repository.BookingStatusRepository;
import com.KLTN.nguyen.hotelbooking.repository.RoomRepository;
import com.KLTN.nguyen.hotelbooking.repository.UserRepository;
import com.KLTN.nguyen.hotelbooking.util.Status;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final BookingStatusRepository bookingStatusRepository;
    private final RoomRepository roomRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    public BookingResponse createBooking(BookingRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));
        BookingStatus status = bookingStatusRepository.findByCode(Status.PENDING.name());
        PaymentMethod paymentMethod = paymentMethodRepository.findByMethodName("CAST");
        Booking booking = Booking.builder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .bookingDate(LocalDate.now())
                .room(room)
                .totalPrice(request.getPrice())
                .user(user)
                .status(status)
                .paymentMethod(paymentMethod)
                .build();
        booking = bookingRepository.save(booking);

        return BookingMapper.toResponseDTO(booking);
    }
    public BookingResponse MomoBooking(BookingRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() ->{
                    return userService.createUserOnlyEmail(request.getEmail());
                }
                );
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));
        BookingStatus status = bookingStatusRepository.findByCode(Status.PENDING.name());
        PaymentMethod paymentMethod = paymentMethodRepository.findByMethodName("MOMO");
        Booking booking = Booking.builder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .bookingDate(LocalDate.now())
                .room(room)
                .totalPrice(request.getPrice())
                .user(user)
                .status(status)
                .paymentMethod(paymentMethod)
                .build();
        booking = bookingRepository.save(booking);

        return BookingMapper.toResponseDTO(booking);
    }
    public List<BookingResponse> getBookingByUser(Integer userId, Integer pageNumber) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
        Pageable page = PageRequest.of(pageNumber, 10);
        Page<Booking> bookingsPage = bookingRepository.findAllByUser(user, page);
        return bookingsPage.stream().map(BookingMapper::toResponseDTO).toList();
    }
    public BookingResponse cancelBooking(Integer id){
        Booking booking = bookingRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Booking not found")
        );
        booking.setStatus(bookingStatusRepository.findByCode((Status.CANCEL.name())));
        bookingRepository.save(booking);
        return BookingMapper.toResponseDTO(booking);
    }
    public BookingResponse acceptBooking(Integer id){
        Booking booking = bookingRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Booking not found")
        );
        booking.setStatus(bookingStatusRepository.findByCode((Status.ACCEPT.name())));
        bookingRepository.save(booking);
        return BookingMapper.toResponseDTO(booking);
    }
    public Page<BookingResponse> getBookingsByHotel(Integer hotelId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("bookingDate").descending());
        Page<Booking> bookings = bookingRepository.findBookingsByHotelId(hotelId, pageable);
        return bookings.map(BookingMapper::toResponseDTO);
    }
    public Page<BookingResponse> getBookingsByHotelAndStatus(Integer hotelId, String status, Pageable pageable) {
        if (status == null || status.equals("Tất cả")) {
            return bookingRepository.findBookingsByHotelId(hotelId, pageable).map(BookingMapper::toResponseDTO);
        } else {
            return bookingRepository.findByHotelIdAndStatus(hotelId, status, pageable).map(BookingMapper::toResponseDTO);
        }
    }
    public List<BookingResponse> getBookingsForCalendar(Integer hotelId, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<Booking> bookings = bookingRepository.findBookingsInDateRange(hotelId, start, end);

        return bookings.stream().map(BookingMapper::toResponseDTO).toList();
    }
    public List<HotelRevenue> getRevenueByEndDate(Integer hotelId, LocalDate startDate, LocalDate endDate) {
        return bookingRepository.getRevenueByEndDateFiltered(hotelId, startDate, endDate);
    }
    public BookingResponse updateBookingStatus(Integer bookingId, String status) {
        BookingStatus bookingStatus = bookingStatusRepository.findByStatus(status);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()-> new EntityNotFoundException("not found"));
        booking.setStatus(bookingStatus);
        bookingRepository.save(booking);
        return BookingMapper.toResponseDTO(booking);
    }
}
