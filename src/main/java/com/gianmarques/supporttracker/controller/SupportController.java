package com.gianmarques.supporttracker.controller;


import com.gianmarques.supporttracker.entity.Support;
import com.gianmarques.supporttracker.mapper.SupportMapper;
import com.gianmarques.supporttracker.mapper.dto.person.PersonRequestDto;
import com.gianmarques.supporttracker.mapper.dto.person.PersonResponseDto;
import com.gianmarques.supporttracker.mapper.dto.support.SupportListResponseDto;
import com.gianmarques.supporttracker.security.jwt.JwtUserDetails;
import com.gianmarques.supporttracker.service.SupportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/support")
public class SupportController {


    private final SupportService supportService;

    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }

    // Save support
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PersonResponseDto> saveSupport(@Valid @RequestBody PersonRequestDto personRequestDto) {
        Support support = supportService.addSupport(SupportMapper.toSupport(personRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(SupportMapper.toDto(support));
    }


    @PreAuthorize("hasRole('SUPPORT')")
    @GetMapping
    public ResponseEntity<List<SupportListResponseDto>> getAllTicketsBySupport(@AuthenticationPrincipal JwtUserDetails userDetails) {
        List<Support> ticketsBySupport = supportService.getAllTickets(userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(SupportMapper.toList(ticketsBySupport));

    }
}
