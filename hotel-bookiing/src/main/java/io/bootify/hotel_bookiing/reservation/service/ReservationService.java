package io.bootify.hotel_bookiing.reservation.service;

import io.bootify.hotel_bookiing.reservation.domain.Reservation;
import io.bootify.hotel_bookiing.reservation.model.ReservationDTO;
import io.bootify.hotel_bookiing.reservation.repos.ReservationRepository;
import io.bootify.hotel_bookiing.room.domain.Room;
import io.bootify.hotel_bookiing.room.repos.RoomRepository;
import io.bootify.hotel_bookiing.user.domain.User;
import io.bootify.hotel_bookiing.user.repos.UserRepository;
import io.bootify.hotel_bookiing.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public ReservationService(final ReservationRepository reservationRepository,
            final RoomRepository roomRepository, final UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public List<ReservationDTO> findAll() {
        final List<Reservation> reservations = reservationRepository.findAll(Sort.by("id"));
        return reservations.stream()
                .map(reservation -> mapToDTO(reservation, new ReservationDTO()))
                .toList();
    }

    public ReservationDTO get(final Long id) {
        return reservationRepository.findById(id)
                .map(reservation -> mapToDTO(reservation, new ReservationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ReservationDTO reservationDTO) {
        final Reservation reservation = new Reservation();
        mapToEntity(reservationDTO, reservation);
        return reservationRepository.save(reservation).getId();
    }

    public void update(final Long id, final ReservationDTO reservationDTO) {
        final Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reservationDTO, reservation);
        reservationRepository.save(reservation);
    }

    public void delete(final Long id) {
        reservationRepository.deleteById(id);
    }

    private ReservationDTO mapToDTO(final Reservation reservation,
            final ReservationDTO reservationDTO) {
        reservationDTO.setId(reservation.getId());
        reservationDTO.setRoom(reservation.getRoom());
        reservationDTO.setUser(reservation.getUser());
        reservationDTO.setCheckInDate(reservation.getCheckInDate());
        reservationDTO.setCheckOutDate(reservation.getCheckOutDate());
        reservationDTO.setNumberOfGuests(reservation.getNumberOfGuests());
        reservationDTO.setTotalPrice(reservation.getTotalPrice());
        reservationDTO.setStatus(reservation.getStatus());
        reservationDTO.setCreatedAt(reservation.getCreatedAt());
        reservationDTO.setUpdatedAt(reservation.getUpdatedAt());
        reservationDTO.setRoomId(reservation.getRoomId() == null ? null : reservation.getRoomId().getId());
        reservationDTO.setRooms(reservation.getRooms() == null ? null : reservation.getRooms().getId());
        reservationDTO.setUsers(reservation.getUsers() == null ? null : reservation.getUsers().getId());
        reservationDTO.setUsersId(reservation.getUsersId() == null ? null : reservation.getUsersId().getId());
        return reservationDTO;
    }

    private Reservation mapToEntity(final ReservationDTO reservationDTO,
            final Reservation reservation) {
        reservation.setRoom(reservationDTO.getRoom());
        reservation.setUser(reservationDTO.getUser());
        reservation.setCheckInDate(reservationDTO.getCheckInDate());
        reservation.setCheckOutDate(reservationDTO.getCheckOutDate());
        reservation.setNumberOfGuests(reservationDTO.getNumberOfGuests());
        reservation.setTotalPrice(reservationDTO.getTotalPrice());
        reservation.setStatus(reservationDTO.getStatus());
        reservation.setCreatedAt(reservationDTO.getCreatedAt());
        reservation.setUpdatedAt(reservationDTO.getUpdatedAt());
        final Room roomId = reservationDTO.getRoomId() == null ? null : roomRepository.findById(reservationDTO.getRoomId())
                .orElseThrow(() -> new NotFoundException("roomId not found"));
        reservation.setRoomId(roomId);
        final Room rooms = reservationDTO.getRooms() == null ? null : roomRepository.findById(reservationDTO.getRooms())
                .orElseThrow(() -> new NotFoundException("rooms not found"));
        reservation.setRooms(rooms);
        final User users = reservationDTO.getUsers() == null ? null : userRepository.findById(reservationDTO.getUsers())
                .orElseThrow(() -> new NotFoundException("users not found"));
        reservation.setUsers(users);
        final User usersId = reservationDTO.getUsersId() == null ? null : userRepository.findById(reservationDTO.getUsersId())
                .orElseThrow(() -> new NotFoundException("usersId not found"));
        reservation.setUsersId(usersId);
        return reservation;
    }

}
