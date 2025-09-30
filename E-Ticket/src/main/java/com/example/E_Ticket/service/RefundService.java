package com.example.E_Ticket.service;

import com.example.E_Ticket.dto.RefundDto;
import com.example.E_Ticket.dto.RefundCreateReq;

import java.util.List;

public interface RefundService {
    RefundDto create(RefundCreateReq req, String adminEmail);
    List<RefundDto> listByOrder(Long orderId);
    RefundDto approve(Long refundId, String adminEmail, String note);
    RefundDto reject(Long refundId, String adminEmail, String note);
    RefundDto markPaid(Long refundId, String adminEmail, String note);
}
