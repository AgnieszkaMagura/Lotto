package pl.lotto.domain.resultannouncer.dto;

import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ResponseDto(
        @Id String hash,
        Set<Integer> numbers,
        Set<Integer> hitNumbers,
        Set<Integer> winningNumbers,
        LocalDateTime drawDate,
        boolean isWinner) implements Serializable {
}
