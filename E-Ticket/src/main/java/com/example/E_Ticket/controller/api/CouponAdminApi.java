package com.example.E_Ticket.controller.api;

import com.example.E_Ticket.dto.CouponDto;
import com.example.E_Ticket.dto.CouponUpsertReq;
import com.example.E_Ticket.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/v1/coupons")
@RequiredArgsConstructor
public class CouponAdminApi {
    private final CouponService service;

    @GetMapping
    public List<CouponDto> list() {
        return service.list();
    }

    @PostMapping
    public CouponDto create( @Valid @RequestBody CouponUpsertReq r) {
        return service.create(r);
    }

    @PutMapping("/{id}")
    public CouponDto update(@PathVariable Long id,@Valid @RequestBody CouponUpsertReq r) {
        return service.update(id, r);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // test/preview
    @PostMapping("/validate")
    public Map<String, Object> validate(@RequestBody Map<String, String> body) {
        CouponDto c = service.validate(body.get("code"));
        return Map.of("ok", true, "coupon", c);
    }
}
