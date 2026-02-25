package pl.lotto.domain.resultchecker;

public class PlayerResultNotFoundException extends RuntimeException {

    PlayerResultNotFoundException(String message) {
        super(String.format(message));
    }
}
