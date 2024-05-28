package ru.gb.Spring_Test_HW10.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gb.Spring_Test_HW10.dto.TransferRequest;
import ru.gb.Spring_Test_HW10.model.Account;
import ru.gb.Spring_Test_HW10.services.TransferService;

@AllArgsConstructor
@RestController
public class AccountController {

    private final TransferService transferService;



    @PostMapping("/transfer")
    public void transferMoney(
            @RequestBody TransferRequest request
    ) {
        transferService.transferMoney(
                request.getSenderAccountId(),
                request.getReceiverAccountId(),
                request.getAmount());
    }

    @GetMapping("/accounts")
    public Iterable<Account> getAllAccounts(
            @RequestParam(required = false) String name
    ) {
        if (name == null) {
            return transferService.getAllAccounts();
        } else {
            return transferService.findAccountsByName(name);
        }
    }

}
