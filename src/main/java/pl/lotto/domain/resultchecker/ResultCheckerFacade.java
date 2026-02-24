package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numbergenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.dto.PlayersDto;
import pl.lotto.domain.resultchecker.dto.PlayerDto;

import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultchecker.ResultCheckerMapper.mapPlayersToResults;
import static pl.lotto.domain.resultchecker.ResultCheckerMapper.mapToPlayerDto;

@AllArgsConstructor
public class ResultCheckerFacade {

    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;
    NumberReceiverFacade numberReceiverFacade;
    PlayerRepository playerRepository;
    WinnersRetriever winnerGenerator;


    public PlayersDto generateResults() {
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate();
        List<Ticket> tickets = ResultCheckerMapper.mapFromTicketDto(allTicketsByDate);
        WinningNumbersDto winningNumbersDto = winningNumbersGeneratorFacade.generateWinningNumbers();
        Set<Integer> winningNumbers = winningNumbersDto.getWinningNumbers();
        if (winningNumbers == null || winningNumbers.isEmpty()) {
            return PlayersDto.builder()
                    .message("Winners failed to retrieve")
                    .build();
        }
        List<Player> players = winnerGenerator.retrieveWinners(tickets, winningNumbers);
        playerRepository.saveAll(players);
        return PlayersDto.builder()
                .results(mapPlayersToResults(players))
                .message("Winners succeeded to retrieve")
                .build();
    }

    public PlayerDto findByTicketId(String ticketId) {
        Player player = playerRepository.findById(ticketId).orElse(null);
        if (player == null) {
            List<String> ticketIds = numberReceiverFacade.retrieveAllTicketsByNextDrawDate()
                    .stream()
                    .map(TicketDto::hash)
                    .toList();
            boolean isTicketWaitingForDraw = ticketIds.stream()
                    .anyMatch(hashFromList -> ticketIds.contains(ticketId));
            if (isTicketWaitingForDraw) {
                throw new PlayerResultNotFoundException("Your ticket is waiting for the draw, please come back after Saturday 12:00 p.m.");
            }
            throw new PlayerResultNotFoundException(ticketId);
        }
        return mapToPlayerDto(player);
    }
}
