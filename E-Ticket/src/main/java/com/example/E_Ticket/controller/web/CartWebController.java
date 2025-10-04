package com.example.E_Ticket.controller.web;

import com.example.E_Ticket.dto.CartDtos;
import com.example.E_Ticket.dto.CartView;
import com.example.E_Ticket.entity.Event;
import com.example.E_Ticket.entity.TicketType;
import com.example.E_Ticket.exception.BusinessException;
import com.example.E_Ticket.repository.EventRepository;
import com.example.E_Ticket.repository.TicketTypeRepository;
import com.example.E_Ticket.service.impl.CartSessionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Validated
public class CartWebController {

    private final CartSessionService cartSession;
    private final TicketTypeRepository ticketTypeRepo;
    private final EventRepository eventRepo;
    // Nếu đã có PricingService/InventoryService, có thể @Autowired vào đây để thay giá/hold thực.

    /** Trang giỏ hàng */
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model){
        CartView cart = cartSession.get(session);
        model.addAttribute("cart", cart);
        return "cart/index";
    }

    /** Thêm vé (không ghế) vào giỏ */
    @PostMapping(path="/cart/add", consumes="application/json")
    @ResponseBody
    public Map<String,Object> add(@Valid @RequestBody CartDtos.AddToCartReq req, HttpSession session){
        if (req.items().isEmpty()) throw new BusinessException("Danh sách vé trống");

        // Lấy toàn bộ ticket types
        List<Long> ids = req.items().stream().map(CartDtos.AddToCartReq.Item::ticketTypeId).toList();
        List<TicketType> types = ticketTypeRepo.findAllById(ids);
        if (types.size() != ids.size()) throw new BusinessException("Ticket type không tồn tại");

        // Kiểm tra tất cả thuộc 1 event (đơn giản hoá), và event không có seatmap
        Long eventId = types.get(0).getEvent().getId();
        Event ev = eventRepo.findById(eventId).orElseThrow(() -> new BusinessException("Event không tồn tại"));
        boolean sameEvent = types.stream().allMatch(t -> t.getEvent().getId().equals(eventId));
        if (!sameEvent) throw new BusinessException("Các loại vé phải thuộc cùng 1 sự kiện");
        if (ev.getSeatMap() != null) throw new BusinessException("Sự kiện có sơ đồ ghế – hãy chọn chỗ");

        // Gộp vào giỏ (merge qty nếu trùng ticketType)
        CartView cart = cartSession.get(session);
        for (var i : req.items()){
            TicketType t = types.stream().filter(x -> x.getId().equals(i.ticketTypeId())).findFirst().get();

            // (Nếu có PricingService.previewPrice(...) thì thay t.getPrice() bằng giá tính)
            Number price = t.getPrice() == null ? 0L : t.getPrice();

            CartView.Line line = cart.lines.stream()
                    .filter(l -> l.ticketTypeId.equals(t.getId()))
                    .findFirst().orElse(null);
            if (line == null){
                line = new CartView.Line();
                line.ticketTypeId = t.getId();
                line.eventId = ev.getId();
                line.eventTitle = ev.getTitle();
                line.ticketTypeName = t.getName();
                line.unitPrice = (long) price;
                line.qty = 0;
                cart.lines.add(line);
            }
            line.qty += i.qty();
        }

        // Với InventoryService: ở đây bạn có thể gọi holdTickets(eventId, req.items(), sessionId)
        // để tạm giữ. Lưu thêm holdId/ttl vào session. (Sẽ bổ sung ở bước nâng cao.)

        return Map.of("ok", true);
    }

    /** Xoá 1 dòng khỏi giỏ */
    @PostMapping("/cart/remove")
    public String remove(@RequestParam Long ticketTypeId, HttpSession session){
        CartView cart = cartSession.get(session);
        cart.lines.removeIf(l -> l.ticketTypeId.equals(ticketTypeId));
        return "redirect:/cart";
    }

    /** Xoá toàn bộ giỏ */
    @PostMapping("/cart/clear")
    public String clear(HttpSession session){
        cartSession.clear(session);
        return "redirect:/cart";
    }
}
