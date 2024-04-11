package io.bootify.hotel_bookiing.room.rest;

import io.bootify.hotel_bookiing.room.model.RoomDTO;
import io.bootify.hotel_bookiing.room.service.RoomService;
import io.bootify.hotel_bookiing.util.ReferencedException;
import io.bootify.hotel_bookiing.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoomResource {

    private final RoomService roomService;

    public RoomResource(final RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        return ResponseEntity.ok(roomService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoom(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(roomService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRoom(@RequestBody @Valid final RoomDTO roomDTO) {
        final Long createdId = roomService.create(roomDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateRoom(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RoomDTO roomDTO) {
        roomService.update(id, roomDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRoom(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = roomService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
