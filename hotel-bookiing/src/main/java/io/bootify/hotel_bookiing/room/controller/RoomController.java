package io.bootify.hotel_bookiing.room.controller;

import io.bootify.hotel_bookiing.hotel.domain.Hotel;
import io.bootify.hotel_bookiing.hotel.repos.HotelRepository;
import io.bootify.hotel_bookiing.room.model.RoomDTO;
import io.bootify.hotel_bookiing.room.model.RoomType;
import io.bootify.hotel_bookiing.room.service.RoomService;
import io.bootify.hotel_bookiing.util.CustomCollectors;
import io.bootify.hotel_bookiing.util.ReferencedWarning;
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
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final HotelRepository hotelRepository;

    public RoomController(final RoomService roomService, final HotelRepository hotelRepository) {
        this.roomService = roomService;
        this.hotelRepository = hotelRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("typeValues", RoomType.values());
        model.addAttribute("hotelsValues", hotelRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Hotel::getId, Hotel::getName)));
        model.addAttribute("hotelIdValues", hotelRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Hotel::getId, Hotel::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("rooms", roomService.findAll());
        return "room/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("room") final RoomDTO roomDTO) {
        return "room/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("room") @Valid final RoomDTO roomDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "room/add";
        }
        roomService.create(roomDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("room.create.success"));
        return "redirect:/rooms";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("room", roomService.get(id));
        return "room/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("room") @Valid final RoomDTO roomDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "room/edit";
        }
        roomService.update(id, roomDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("room.update.success"));
        return "redirect:/rooms";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = roomService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            roomService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("room.delete.success"));
        }
        return "redirect:/rooms";
    }

}
