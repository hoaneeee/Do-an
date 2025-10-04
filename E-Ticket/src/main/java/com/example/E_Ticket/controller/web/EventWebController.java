package com.example.E_Ticket.controller.web;

import com.example.E_Ticket.dto.EventDto;
import com.example.E_Ticket.dto.TicketTypeDto;
import com.example.E_Ticket.entity.Event;
import com.example.E_Ticket.entity.TicketType;
import com.example.E_Ticket.exception.NotFoundException;
import com.example.E_Ticket.mapper.EventMapper;
import com.example.E_Ticket.mapper.TicketTypeMapper;
import com.example.E_Ticket.repository.EventRepository;
import com.example.E_Ticket.repository.TicketHoldRepository;
import com.example.E_Ticket.repository.TicketTypeRepository;
import com.example.E_Ticket.service.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EventWebController {

    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;

    /** Detail: /events/{slug} */
    @GetMapping("/events/{slug}")
    public String detail(@PathVariable String slug, Model model) {
        Event e = eventRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        EventDto dto = EventMapper.toDto(e);

        // Lấy các loại vé của event
        List<TicketType> types = ticketTypeRepository.findByEventId(e.getId());
        List<TicketTypeDto> typeDtos = types.stream().map(TicketTypeMapper::toDto).toList();

        model.addAttribute("e", dto);
        model.addAttribute("types", typeDtos);
        model.addAttribute("hasSeatMap", dto.seatMapId() != null);

        return "events/detail";
    }
}
