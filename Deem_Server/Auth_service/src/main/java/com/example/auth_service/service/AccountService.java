package com.example.auth_service.service;

import com.example.auth_service.models.Account;
import com.example.auth_service.models.ListLong;
import com.example.auth_service.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository repository;

    @Transactional
    public Account save(Account account) { return repository.save(account); }
    @Transactional
    public void delete(Long idAccount) { repository.deleteById(idAccount); }

    @Cacheable("account_name")
    public Account getAccount(String username) {
        return repository.findByUsername(username).orElse(null);
    }

    @Cacheable("account_id")
    public Account getAccount(Long id) { return repository.findById(id).orElse(null); }

    @Cacheable("accounts")
    public List<Account> getAccounts() {
        return repository.findAll();
    }

    @Cacheable("users_of_group")
    public List<Long> getUsersOfGroup(Long idGroup) { return repository.findUsersOfGroup(idGroup);}

    @Cacheable("topUsers")
    public List<String> getTopUniversity() {
        List<Account> accounts = repository.findTopUniversity();
        List<String> list = new ArrayList<>();
        for (Account account : accounts)
            list.add(account.getUsername());
        return list;
    }


    /*public List<Account> sort(List<Account> accounts) {
        List<Account> tops = new ArrayList<>();
        accounts.stream().sorted(Comparator.comparing(o -> o.getScore()));

        int size = Math.min(accounts.size(), 10);
        for (int j = 0; j < size; j++)
            tops.add(accounts.get(j));
        return tops;
    }*/

    @Transactional
    public void sendScore(Long idAccount, int score) {
        Optional<Account> accountOpt = repository.findById(idAccount);

        if (!accountOpt.isEmpty()) {
            Account account = accountOpt.get();
            account.addScore(score);
        }
    }

    @Transactional
    public void transferAccount(Long idStudent, Long idGroup) {
        Optional<Account> account_opt = repository.findById(idStudent);

        if (!account_opt.isEmpty()) {
            Account account = account_opt.get();
            account.setGroup_id(idGroup);
        }
    }

    public List<Integer> getListAverageValue(List<ListLong> list) {
        List<Integer> result_list = new ArrayList<>();

        for (ListLong listInteger : list) {
            List<Account> accountList = repository.findAllByIdIn(listInteger.list);

            Integer aver = 0;

            for (Account account : accountList)
                aver += account.getScore();
            if (accountList.size() != 0)
                aver /= accountList.size();

            result_list.add(aver);
        }
        return result_list;
    }
}
