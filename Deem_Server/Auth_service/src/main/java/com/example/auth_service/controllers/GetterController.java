package com.example.auth_service.controllers;

import com.example.auth_service.models.Account;
import com.example.auth_service.models.Group;
import com.example.auth_service.service.AccountService;
import com.example.auth_service.service.AccountServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/getAuth")
public class GetterController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountServiceClient accountServiceClient;

    @GetMapping("/getMyAccount")
    public Account getMyAccount(Principal principal) {
        Account account = accountService.getAccount(principal.getName());
        account.setGroup(accountServiceClient.findGroupById(account.getGroup_id()));
        return account;
    }

    /** Заметка для дто
     * Скрывает данные: баллы
     */
    @GetMapping("/getAccounts")
    public List<Account> getAccounts() {
        List<Account> accounts = accountService.getAccounts();
        List<Group> groups = accountServiceClient.getGroups();
        System.out.println(groups.size());
        for (Account account : accounts)
            for (Group group : groups)
            if (account.getGroup_id() == group.getId()) {
                account.setGroup(group);
                break;
            }

        return accounts;
    }
}