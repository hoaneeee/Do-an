package com.example.E_Ticket.service.impl;

import com.example.E_Ticket.dto.CartView;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class CartSessionService {
    public static final String CART_KEY = "CART";

    public CartView get(HttpSession session){
        CartView c = (CartView) session.getAttribute(CART_KEY);
        if (c == null){
            c = new CartView();
            session.setAttribute(CART_KEY, c);
        }
        return c;
    }

    public void clear(HttpSession session){
        session.removeAttribute(CART_KEY);
    }
}
