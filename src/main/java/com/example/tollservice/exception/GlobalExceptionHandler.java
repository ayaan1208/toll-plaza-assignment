package com.example.tollservice.exception;

import com.example.tollservice.dto.RouteDto;
import com.example.tollservice.dto.TollPlazaDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex) {
        if (ex.getRoute() != null && ex.getTollPlazas() != null) {
            return ResponseEntity.badRequest().body(
                new ErrorWithRouteResponse(ex.getMessage(), ex.getRoute(), ex.getTollPlazas())
            );
        }
        return ResponseEntity.badRequest().body(
            new ErrorResponse(ex.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllUnhandled(Exception ex) {
        return ResponseEntity.internalServerError().body(
            new ErrorResponse("Something went wrong")
        );
    }

    record ErrorResponse(String error) {}
    record ErrorWithRouteResponse(String error, RouteDto route, List<TollPlazaDto> tollPlazas) {}
}
