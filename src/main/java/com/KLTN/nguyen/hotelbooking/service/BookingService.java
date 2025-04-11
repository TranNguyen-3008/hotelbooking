package com.KLTN.nguyen.hotelbooking.service;

import com.KLTN.nguyen.hotelbooking.dto.request.BookingRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.BookingResponse;
import com.KLTN.nguyen.hotelbooking.entity.Booking;
import com.KLTN.nguyen.hotelbooking.entity.Hotel;
import com.KLTN.nguyen.hotelbooking.entity.Room;
import com.KLTN.nguyen.hotelbooking.entity.User;
import com.KLTN.nguyen.hotelbooking.mapper.BookingMapper;
import com.KLTN.nguyen.hotelbooking.mapper.HotelMapper;
import com.KLTN.nguyen.hotelbooking.mapper.RoomMapper;
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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingStatusRepository bookingStatusRepository;
    private final RoomRepository roomRepository;
    public BookingResponse addBooking(Integer userId, BookingRequest request){
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new EntityNotFoundException("User not found")
        );
        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(
                ()-> new EntityNotFoundException("Room not found")
        );
        Booking booking = Booking.builder()
                .totalPrice(room.getPrice())
                .status(bookingStatusRepository.findByCode((Status.PENDING.name())))
                .room(room)
                .endDate(request.getEndDate())
                .startDate(request.getStartDate())
                .bookingDate(LocalDate.now())
                .build();
        bookingRepository.save(booking);
        return BookingMapper.toResponseDTO(booking);
    }
    public List<BookingResponse> getBookingByUser(Integer userId, Integer pageNumber){
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new EntityNotFoundException("User not found")
        );
        Pageable page = PageRequest.of(pageNumber, 10);
        Page<Booking> bookings = bookingRepository.findAllByUser(user, page);
        return bookings.stream().map(BookingMapper::toResponseDTO).toList();
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
}
