package io.bootify.hotel_bookiing.hotel.service;

import io.bootify.hotel_bookiing.hotel.domain.Hotel;
import io.bootify.hotel_bookiing.hotel.model.HotelDTO;
import io.bootify.hotel_bookiing.hotel.repos.HotelRepository;
import io.bootify.hotel_bookiing.room.domain.Room;
import io.bootify.hotel_bookiing.room.repos.RoomRepository;
import io.bootify.hotel_bookiing.util.NotFoundException;
import io.bootify.hotel_bookiing.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    public HotelService(final HotelRepository hotelRepository,
            final RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    public List<HotelDTO> findAll() {
        final List<Hotel> hotels = hotelRepository.findAll(Sort.by("id"));
        return hotels.stream()
                .map(hotel -> mapToDTO(hotel, new HotelDTO()))
                .toList();
    }

    public HotelDTO get(final Long id) {
        return hotelRepository.findById(id)
                .map(hotel -> mapToDTO(hotel, new HotelDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final HotelDTO hotelDTO) {
        final Hotel hotel = new Hotel();
        mapToEntity(hotelDTO, hotel);
        return hotelRepository.save(hotel).getId();
    }

    public void update(final Long id, final HotelDTO hotelDTO) {
        final Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(hotelDTO, hotel);
        hotelRepository.save(hotel);
    }

    public void delete(final Long id) {
        hotelRepository.deleteById(id);
    }

    private HotelDTO mapToDTO(final Hotel hotel, final HotelDTO hotelDTO) {
        hotelDTO.setId(hotel.getId());
        hotelDTO.setName(hotel.getName());
        hotelDTO.setAdress(hotel.getAdress());
        hotelDTO.setRating(hotel.getRating());
        hotelDTO.setDescription(hotel.getDescription());
        hotelDTO.setContactNumber(hotel.getContactNumber());
        hotelDTO.setEmail(hotel.getEmail());
        hotelDTO.setWebsite(hotel.getWebsite());
        hotelDTO.setCheckInTime(hotel.getCheckInTime());
        hotelDTO.setCheckOutTime(hotel.getCheckOutTime());
        hotelDTO.setImageUrls(hotel.getImageUrls());
        hotelDTO.setCreatedAt(hotel.getCreatedAt());
        hotelDTO.setUpdatedAt(hotel.getUpdatedAt());
        return hotelDTO;
    }

    private Hotel mapToEntity(final HotelDTO hotelDTO, final Hotel hotel) {
        hotel.setName(hotelDTO.getName());
        hotel.setAdress(hotelDTO.getAdress());
        hotel.setRating(hotelDTO.getRating());
        hotel.setDescription(hotelDTO.getDescription());
        hotel.setContactNumber(hotelDTO.getContactNumber());
        hotel.setEmail(hotelDTO.getEmail());
        hotel.setWebsite(hotelDTO.getWebsite());
        hotel.setCheckInTime(hotelDTO.getCheckInTime());
        hotel.setCheckOutTime(hotelDTO.getCheckOutTime());
        hotel.setImageUrls(hotelDTO.getImageUrls());
        hotel.setCreatedAt(hotelDTO.getCreatedAt());
        hotel.setUpdatedAt(hotelDTO.getUpdatedAt());
        return hotel;
    }

    public boolean nameExists(final String name) {
        return hotelRepository.existsByNameIgnoreCase(name);
    }

    public boolean emailExists(final String email) {
        return hotelRepository.existsByEmailIgnoreCase(email);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Room hotelsRoom = roomRepository.findFirstByHotels(hotel);
        if (hotelsRoom != null) {
            referencedWarning.setKey("hotel.room.hotels.referenced");
            referencedWarning.addParam(hotelsRoom.getId());
            return referencedWarning;
        }
        final Room hotelIdRoom = roomRepository.findFirstByHotelId(hotel);
        if (hotelIdRoom != null) {
            referencedWarning.setKey("hotel.room.hotelId.referenced");
            referencedWarning.addParam(hotelIdRoom.getId());
            return referencedWarning;
        }
        return null;
    }

}
