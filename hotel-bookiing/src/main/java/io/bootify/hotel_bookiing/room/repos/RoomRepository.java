package io.bootify.hotel_bookiing.room.repos;

import io.bootify.hotel_bookiing.hotel.domain.Hotel;
import io.bootify.hotel_bookiing.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoomRepository extends JpaRepository<Room, Long> {

    Room findFirstByHotels(Hotel hotel);

    Room findFirstByHotelId(Hotel hotel);

    boolean existsByRoomNumberIgnoreCase(String roomNumber);

}
