package io.bootify.hotel_bookiing.room.service;

import io.bootify.hotel_bookiing.hotel.domain.Hotel;
import io.bootify.hotel_bookiing.hotel.repos.HotelRepository;
import io.bootify.hotel_bookiing.reservation.domain.Reservation;
import io.bootify.hotel_bookiing.reservation.repos.ReservationRepository;
import io.bootify.hotel_bookiing.room.domain.Room;
import io.bootify.hotel_bookiing.room.model.RoomDTO;
import io.bootify.hotel_bookiing.room.repos.RoomRepository;
import io.bootify.hotel_bookiing.util.NotFoundException;
import io.bootify.hotel_bookiing.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ReservationRepository reservationRepository;

    public RoomService(final RoomRepository roomRepository, final HotelRepository hotelRepository,
            final ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<RoomDTO> findAll() {
        final List<Room> rooms = roomRepository.findAll(Sort.by("id"));
        return rooms.stream()
                .map(room -> mapToDTO(room, new RoomDTO()))
                .toList();
    }

    public RoomDTO get(final Long id) {
        return roomRepository.findById(id)
                .map(room -> mapToDTO(room, new RoomDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RoomDTO roomDTO) {
        final Room room = new Room();
        mapToEntity(roomDTO, room);
        return roomRepository.save(room).getId();
    }

    public void update(final Long id, final RoomDTO roomDTO) {
        final Room room = roomRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(roomDTO, room);
        roomRepository.save(room);
    }

    public void delete(final Long id) {
        roomRepository.deleteById(id);
    }

    private RoomDTO mapToDTO(final Room room, final RoomDTO roomDTO) {
        roomDTO.setId(room.getId());
        roomDTO.setHotel(room.getHotel());
        roomDTO.setRoomNumber(room.getRoomNumber());
        roomDTO.setType(room.getType());
        roomDTO.setPrice(room.getPrice());
        roomDTO.setCapacity(room.getCapacity());
        roomDTO.setDescription(room.getDescription());
        roomDTO.setImageUrls(room.getImageUrls());
        roomDTO.setAvailable(room.getAvailable());
        roomDTO.setCreatedAt(room.getCreatedAt());
        roomDTO.setUpdatedAt(room.getUpdatedAt());
        roomDTO.setHotels(room.getHotels() == null ? null : room.getHotels().getId());
        roomDTO.setHotelId(room.getHotelId() == null ? null : room.getHotelId().getId());
        return roomDTO;
    }

    private Room mapToEntity(final RoomDTO roomDTO, final Room room) {
        room.setHotel(roomDTO.getHotel());
        room.setRoomNumber(roomDTO.getRoomNumber());
        room.setType(roomDTO.getType());
        room.setPrice(roomDTO.getPrice());
        room.setCapacity(roomDTO.getCapacity());
        room.setDescription(roomDTO.getDescription());
        room.setImageUrls(roomDTO.getImageUrls());
        room.setAvailable(roomDTO.getAvailable());
        room.setCreatedAt(roomDTO.getCreatedAt());
        room.setUpdatedAt(roomDTO.getUpdatedAt());
        final Hotel hotels = roomDTO.getHotels() == null ? null : hotelRepository.findById(roomDTO.getHotels())
                .orElseThrow(() -> new NotFoundException("hotels not found"));
        room.setHotels(hotels);
        final Hotel hotelId = roomDTO.getHotelId() == null ? null : hotelRepository.findById(roomDTO.getHotelId())
                .orElseThrow(() -> new NotFoundException("hotelId not found"));
        room.setHotelId(hotelId);
        return room;
    }

    public boolean roomNumberExists(final String roomNumber) {
        return roomRepository.existsByRoomNumberIgnoreCase(roomNumber);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Room room = roomRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Reservation roomIdReservation = reservationRepository.findFirstByRoomId(room);
        if (roomIdReservation != null) {
            referencedWarning.setKey("room.reservation.roomId.referenced");
            referencedWarning.addParam(roomIdReservation.getId());
            return referencedWarning;
        }
        final Reservation roomsReservation = reservationRepository.findFirstByRooms(room);
        if (roomsReservation != null) {
            referencedWarning.setKey("room.reservation.rooms.referenced");
            referencedWarning.addParam(roomsReservation.getId());
            return referencedWarning;
        }
        return null;
    }

}
