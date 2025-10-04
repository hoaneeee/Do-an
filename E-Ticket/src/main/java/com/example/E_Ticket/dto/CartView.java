package com.example.E_Ticket.dto;

import java.util.ArrayList;
import java.util.List;

/** Dùng cho view giỏ hàng */
public class CartView {
    public static class Line {
        public Long ticketTypeId;
        public Long eventId;
        public String eventTitle;
        public String ticketTypeName;
        public long unitPrice; // VND
        public int qty;

        public long lineTotal(){ return unitPrice * qty; }
    }

    public List<Line> lines = new ArrayList<>();
    public long grandTotal(){
        return lines.stream().mapToLong(Line::lineTotal).sum();
    }
}
