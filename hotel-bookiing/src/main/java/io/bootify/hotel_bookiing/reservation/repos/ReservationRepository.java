package io.bootify.hotel_bookiing.reservation.repos;

import io.bootify.hotel_bookiing.reservation.domain.Reservation;
import io.bootify.hotel_bookiing.room.domain.Room;
import io.bootify.hotel_bookiing.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation findFirstByRoomId(Room room);

    Reservation findFirstByRooms(Room room);

    Reservation findFirstByUsers(User user);

    Reservation findFirstByUsersId(User user);

}
