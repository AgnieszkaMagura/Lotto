package pl.lotto.domain.resultannouncer.dto;

import java.io.Serializable;

public record ResultAnnouncerResponseDto(
        ResponseDto responseDto,
        String message) implements Serializable {
}
