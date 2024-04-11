package io.bootify.hotel_bookiing.reservation.controller;

import io.bootify.hotel_bookiing.reservation.model.ReservationDTO;
import io.bootify.hotel_bookiing.reservation.model.ReservationStatus;
import io.bootify.hotel_bookiing.reservation.service.ReservationService;
import io.bootify.hotel_bookiing.room.domain.Room;
import io.bootify.hotel_bookiing.room.repos.RoomRepository;
import io.bootify.hotel_bookiing.user.domain.User;
import io.bootify.hotel_bookiing.user.repos.UserRepository;
import io.bootify.hotel_bookiing.util.CustomCollectors;
import io.bootify.hotel_bookiing.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public ReservationController(final ReservationService reservationService,
            final RoomRepository roomRepository, final UserRepository userRepository) {
        this.reservationService = reservationService;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("statusValues", ReservationStatus.values());
        model.addAttribute("roomIdValues", roomRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Room::getId, Room::getHotel)));
        model.addAttribute("roomsValues", roomRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Room::getId, Room::getHotel)));
        model.addAttribute("usersValues", userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getUsername)));
        model.addAttribute("usersIdValues", userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getUsername)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("reservations", reservationService.findAll());
        return "reservation/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("reservation") final ReservationDTO reservationDTO) {
        return "reservation/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("reservation") @Valid final ReservationDTO reservationDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reservation/add";
        }
        reservationService.create(reservationDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reservation.create.success"));
        return "redirect:/reservations";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("reservation", reservationService.get(id));
        return "reservation/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("reservation") @Valid final ReservationDTO reservationDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reservation/edit";
        }
        reservationService.update(id, reservationDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reservation.update.success"));
        return "redirect:/reservations";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        reservationService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("reservation.delete.success"));
        return "redirect:/reservations";
    }

}
