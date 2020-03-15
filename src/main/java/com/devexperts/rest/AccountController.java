package com.devexperts.rest;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devexperts.account.Account;
import com.devexperts.service.AccountService;
import com.devexperts.service.exception.HttpParametersException;
import com.devexperts.service.exception.UserAccountNotFoundException;

@RestController
@RequestMapping("/api")
public class AccountController extends AbstractAccountController {

	private AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@PostMapping(path = "/operations/transfer")
	public ResponseEntity<Void> transfer(@RequestParam long sourceId, @RequestParam long targetId, double amount) {

		verifyParameters(sourceId, targetId, amount);
		
		Account source = accountService.getAccount(sourceId);
		Account target = accountService.getAccount(targetId);
		verifyAccount(source);
		verifyAccount(target);

		accountService.transfer(source, target, amount);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	private void verifyParameters(long sourceId, long targetId, double amount) {
		if(sourceId <= 0 || targetId <= 0 || amount<=0 ) {
			throw new HttpParametersException();
		}
		
	}

	private void verifyAccount(Account source) {
		if (Objects.isNull(source)) {
			throw new UserAccountNotFoundException("Account not foud for id: " + source.getAccountKey());
		}
	}
}
