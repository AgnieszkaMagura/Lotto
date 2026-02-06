package pl.lotto.infrastructure.resultannouncer.controller.erro;

import org.springframework.http.HttpStatus;

public record ResultAnnouncerErrorResponse(
        String message,
        HttpStatus status) {
}
