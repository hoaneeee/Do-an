package com.example.E_Ticket.service.impl;

import com.example.E_Ticket.dto.RefundDto;
import com.example.E_Ticket.dto.RefundCreateReq;
import com.example.E_Ticket.entity.AuditLog;
import com.example.E_Ticket.entity.Order;
import com.example.E_Ticket.entity.Refund;
import com.example.E_Ticket.exception.BusinessException;
import com.example.E_Ticket.exception.NotFoundException;
import com.example.E_Ticket.mapper.RefundMapper;
import com.example.E_Ticket.repository.AuditLogRepository;
import com.example.E_Ticket.repository.OrderRepository;
import com.example.E_Ticket.repository.RefundRepository;
import com.example.E_Ticket.service.RefundService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {
    private final RefundRepository refundRepo;
    private final OrderRepository orderRepo;
    private final AuditLogRepository auditRepo;

    @Override
    @Transactional
    public RefundDto create(RefundCreateReq req, String adminEmail) {
        Order order = orderRepo.findById(req.orderId())
                .orElseThrow(() -> new NotFoundException("Order not found"));
        if (order.getStatus() != Order.Status.PAID) {
            throw new BusinessException("Only PAID order can be refunded");
        }
        if (req.amount().compareTo(order.getTotal()) > 0) {
            throw new BusinessException("Refund amount cannot exceed order total");
        }
        Refund r = Refund.builder()
                .order(order).amount(req.amount()).reason(req.reason())
                .status(Refund.Status.PENDING).createdBy(adminEmail)
                .build();
        r = refundRepo.save(r);

        audit("REFUND_CREATE", adminEmail, "Refund", r.getId(),
                "order="+order.getId()+", amount="+req.amount());
        return RefundMapper.toDto(r);
    }

    @Override
    public List<RefundDto> listByOrder(Long orderId) {
        return refundRepo.findByOrder_Id(orderId).stream().map(RefundMapper::toDto).toList();
    }

    @Override
    @Transactional
    public RefundDto approve(Long refundId, String adminEmail, String note) {
        Refund r = get(refundId);
        if (r.getStatus() != Refund.Status.PENDING)
            throw new BusinessException("Only PENDING can be approved");
        r.setStatus(Refund.Status.APPROVED); r.setNote(note);
        refundRepo.save(r);
        audit("REFUND_APPROVE", adminEmail, "Refund", r.getId(), note);
        return RefundMapper.toDto(r);
    }

    @Override
    @Transactional
    public RefundDto reject(Long refundId, String adminEmail, String note) {
        Refund r = get(refundId);
        if (r.getStatus() != Refund.Status.PENDING)
            throw new BusinessException("Only PENDING can be rejected");
        r.setStatus(Refund.Status.REJECTED); r.setNote(note);
        refundRepo.save(r);
        audit("REFUND_REJECT", adminEmail, "Refund", r.getId(), note);
        return RefundMapper.toDto(r);
    }

    @Override
    @Transactional
    public RefundDto markPaid(Long refundId, String adminEmail, String note) {
        Refund r = get(refundId);
        if (r.getStatus() != Refund.Status.APPROVED)
            throw new BusinessException("Only APPROVED can be marked PAID");
        r.setStatus(Refund.Status.PAID); r.setNote(note);
        refundRepo.save(r);

        // (tuỳ mô hình) Có thể tạo bút toán hạch toán ở đây.
        audit("REFUND_PAID", adminEmail, "Refund", r.getId(), note);
        return RefundMapper.toDto(r);
    }

    private Refund get(Long id){
        return refundRepo.findById(id).orElseThrow(() -> new NotFoundException("Refund not found"));
    }

    private void audit(String action, String actor, String type, Long id, String details){
        auditRepo.save(AuditLog.builder()
                .actor(actor).action(action).entityType(type).entityId(id).details(details).build());
    }
}
