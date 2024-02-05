package ru.vtb.javaCourse.Task5.CorporateSettlementAccount;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.vtb.javaCourse.Task5.Service.AccountService;
import ru.vtb.javaCourse.Task5.Service.AccountServiceImpl;

@RestController
@RequestMapping("/corporate-settlement-account")
public class AccountController {

    AccountService accountService;

    @Autowired
    public AccountController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    @ResponseBody
    public AccountResponse create(@Valid @RequestBody AccountRequest accountRequestBody){
        return accountService.createProductRegister(accountRequestBody);
    }
}
