package com.gianmarques.supporttracker.controller;


import com.gianmarques.supporttracker.entity.Support;
import com.gianmarques.supporttracker.exception.model.ErrorMessage;
import com.gianmarques.supporttracker.mapper.SupportMapper;
import com.gianmarques.supporttracker.mapper.dto.person.PersonRequestDto;
import com.gianmarques.supporttracker.mapper.dto.person.PersonResponseDto;
import com.gianmarques.supporttracker.mapper.dto.support.SupportListResponseDto;
import com.gianmarques.supporttracker.security.jwt.JwtUserDetails;
import com.gianmarques.supporttracker.service.SupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Support", description = "Resources to save a support and get all tickets in the system.")
@RestController
@RequestMapping("/api/v1/support")
public class SupportController {


    private final SupportService supportService;

    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }

    @Operation(summary = "Save support", description = "Resource to save support in the system. (Only admins can save).",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Create support",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PersonResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "409", description = "Conflict data",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Invalid field error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            })

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PersonResponseDto> saveSupport(@Valid @RequestBody PersonRequestDto personRequestDto) {
        Support support = supportService.addSupport(SupportMapper.toSupport(personRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(SupportMapper.toDto(support));
    }


    @Operation(summary = "Get all tickets by Support", description = "Resource to get all tickets the support accepted.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Return tickets",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SupportListResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden access",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PreAuthorize("hasRole('SUPPORT')")
    @GetMapping
    public ResponseEntity<List<SupportListResponseDto>> getAllTicketsBySupport(@AuthenticationPrincipal JwtUserDetails userDetails) {
        List<?> ticketsBySupport = supportService.getAllTickets(userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(SupportMapper.toList((List<Support>) ticketsBySupport));

    }
}
