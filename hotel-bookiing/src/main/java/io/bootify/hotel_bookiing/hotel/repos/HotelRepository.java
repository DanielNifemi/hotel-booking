package io.bootify.hotel_bookiing.hotel.repos;

import io.bootify.hotel_bookiing.hotel.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HotelRepository extends JpaRepository<Hotel, Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByEmailIgnoreCase(String email);

}
