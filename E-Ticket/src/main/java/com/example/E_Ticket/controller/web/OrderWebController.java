/*
package com.example.E_Ticket.controller.web;

import com.example.E_Ticket.entity.Order;
import com.example.E_Ticket.exception.NotFoundException;
import com.example.E_Ticket.repository.OrderRepository;
import com.example.E_Ticket.repository.RefundRepository;
import com.example.E_Ticket.repository.UserRepository;
import com.example.E_Ticket.service.impl.OrderRefundPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderWebController {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final RefundRepository refundRepo;
    private final OrderRefundPolicy policy;

    */
/*list *//*

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String myOrders(Authentication auth, Model model){
        var user = userRepo.findByEmail(auth.getName()).orElseThrow();
        var list = orderRepo.findAllByUser_IdOrderByCreatedAtDesc(user.getId());
        model.addAttribute("orders", list);
        return "orders/list";
    }

    */
/*detail *//*

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{code}")
    public String detail(@PathVariable String code, Authentication auth, Model model){
        var user = userRepo.findByEmail(auth.getName()).orElseThrow();
        Order o = orderRepo.findByCodeAndUser_Id(code, user.getId())
                .orElseThrow(() -> new NotFoundException("Order not found"));

        model.addAttribute("order", o);
        model.addAttribute("refunds", refundRepo.findByOrder_Id(o.getId()));

        boolean refundable = policy.isRefundable(o) && refundRepo.findByOrder_Id(o.getId()).isEmpty();
        model.addAttribute("refundable", refundable);

        return "orders/detail";
    }
}
*/
package com.example.E_Ticket.controller.web;

import com.example.E_Ticket.entity.Order;
import com.example.E_Ticket.exception.NotFoundException;
import com.example.E_Ticket.repository.OrderRepository;
import com.example.E_Ticket.repository.RefundRepository;
import com.example.E_Ticket.repository.UserRepository;
import com.example.E_Ticket.service.impl.OrderRefundPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderWebController {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final RefundRepository refundRepo;
    private final OrderRefundPolicy policy;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String myOrders(Authentication auth, Model model){
        var user = userRepo.findByEmail(auth.getName()).orElseThrow();
        var list = orderRepo.findAllByUser_IdOrderByCreatedAtDesc(user.getId());
        model.addAttribute("orders", list);
        return "orders/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{code}")
    public String detail(@PathVariable String code, Authentication auth, Model model){
        var user = userRepo.findByEmail(auth.getName()).orElseThrow();
        Order o = orderRepo.findByCodeAndUser_Id(code, user.getId())
                .orElseThrow(() -> new NotFoundException("Order not found"));

        model.addAttribute("order", o);
        model.addAttribute("refunds", refundRepo.findByOrder_Id(o.getId()));
        boolean refundable = policy.isRefundable(o) && refundRepo.findByOrder_Id(o.getId()).isEmpty();
        model.addAttribute("refundable", refundable);

        return "orders/detail";
    }
}
