package ru.gb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gb.Spring_Test_HW10.exceptions.AccountNotFoundException;
import ru.gb.Spring_Test_HW10.model.Account;
import ru.gb.Spring_Test_HW10.repositories.AccountRepository;
import ru.gb.Spring_Test_HW10.services.TransferService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransferServiceWithAnnotationsUnitTests {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private TransferService transferService;

    @Test
    public void moneyTransferHappyFlow() {
        // Arrange: начальные условия для тестирования
        Account sender = new Account();
        sender.setId(1);
        sender.setAmount(new BigDecimal(1000));

        Account destination = new Account();
        destination.setId(2);
        destination.setAmount(new BigDecimal(1000));

        given(accountRepository.findById(sender.getId()))
                .willReturn(Optional.of(sender));

        given(accountRepository.findById(destination.getId()))
                .willReturn(Optional.of(destination));

        // Act: проверяемое действие
        transferService.transferMoney(1, 2, new BigDecimal(100));

        // Assert: проверка результата тестирования
        verify(accountRepository).changeAmount(1, new BigDecimal(900));
        verify(accountRepository).changeAmount(2, new BigDecimal(1100));
    }

    @Test
    public void moneyTransferDestinationAccountNotFoundFlow() {
        // Arrange: начальные условия для тестирования
        Account sender = new Account();                                          // Данные по счету отправителя
        sender.setId(1);                                                         // № счета
        sender.setAmount(new BigDecimal(1000));                              // Количество средств на счете

        given(accountRepository.findById(1L))                                    // Перевод средств
                .willReturn(Optional.of(sender));

        given(accountRepository.findById(2L))                                    // Счет получателя не найден
                .willReturn(Optional.empty());

        // Act: проверяемое действие (перевод 100 у.е. с одного счета на другой)
        assertThrows(
                AccountNotFoundException.class,
                () -> transferService.transferMoney(1, 2, new BigDecimal(100))
        );

        // Assert: проверка результата тестирования
        verify(accountRepository, never())                                        // Денежные средства не переведены
                .changeAmount(anyLong(), any());                                  // Баланс не изменился
    }
}