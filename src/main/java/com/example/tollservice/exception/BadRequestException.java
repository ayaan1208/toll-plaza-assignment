package com.example.tollservice.exception;

import com.example.tollservice.dto.RouteDto;
import com.example.tollservice.dto.TollPlazaDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private final RouteDto route;
    private final List<TollPlazaDto> tollPlazas;

    public BadRequestException(String message) {
        super(message);
        this.route = null;
        this.tollPlazas = null;
    }

    public BadRequestException(String message, RouteDto route, List<TollPlazaDto> tollPlazas) {
        super(message);
        this.route = route;
        this.tollPlazas = tollPlazas;
    }

    public RouteDto getRoute() {
        return route;
    }

    public List<TollPlazaDto> getTollPlazas() {
        return tollPlazas;
    }
}
