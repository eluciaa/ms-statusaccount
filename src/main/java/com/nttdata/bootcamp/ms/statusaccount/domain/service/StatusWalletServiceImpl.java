package com.nttdata.bootcamp.ms.statusaccount.domain.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Movement;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Wallet;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.WalletDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.WalletResponse;
import com.nttdata.bootcamp.ms.statusaccount.enums.TypeCurrency;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.BankAccountRestClient;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.CreditRestClient;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.CustomerRestClient;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.DebitRestClient;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.TransactionsRestClient;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.WalletRestClient;

import reactor.core.publisher.Mono;

@Service
public class StatusWalletServiceImpl implements  StatusWalletService{

    @Autowired
    BankAccountRestClient bankAccountRestClient;

    @Autowired
    CreditRestClient creditRestClient;

    @Autowired
    CustomerRestClient customerRestClient;
    
    @Autowired
    TransactionsRestClient transactionsRestClient;
    
    @Autowired
    DebitRestClient debitRestClient;
    
    @Autowired
    WalletRestClient walletRestClient;

	@Override
	public Mono<WalletResponse> createWallet(WalletDto wallet) {
		WalletResponse response = new WalletResponse();
		response.setMessage("Error creating debit card");
		response.setWalletDto(wallet);
		if(wallet.getDebitId()!=null) {
			response.setMessage("Debit card does not exist");
			wallet.setAmount(0f);
			wallet.setAvailableBalance(0f);
			response.setWalletDto(wallet);
			return debitRestClient.getFindId(wallet.getDebitId()).flatMap(deb -> {
				wallet.setAvailableBalance(deb.getAvailableBalance());
				wallet.setAmount(deb.getAvailableBalance());
				return walletRestClient.createWallet(wallet).flatMap(w -> {
					response.setMessage("Wallet created");
					return Mono.just(response);
				});
			}).defaultIfEmpty(response);
			
		}else {
			wallet.setAvailableBalance(0f);
			wallet.setAmount(0f);
			return walletRestClient.createWallet(wallet).flatMap(w -> {
				response.setMessage("Wallet created");
				return Mono.just(response);
			});
		}
	}
	
	@Override
	public Mono<WalletResponse> walletDeposit(WalletDto wallet) {
		Wallet wal = new Wallet();
		wal.setId(wallet.getWalletId());
		wal.setIdDebito(wallet.getDebitId());
		wal.setSaldo(wallet.getAmount());
		WalletResponse response = new WalletResponse();
		Date date = new Date();
		response.setMessage("Wallet not exist");
		response.setWalletDto(wallet);
		float newMount= wallet.getAmount();
		return walletRestClient.getFindId(wallet.getWalletId()).flatMap(w -> {
			response.setWalletDto(wallet);
			if(w.getIdDebito()!=null) {
				return debitRestClient.getFindId(w.getIdDebito()).flatMap(deb -> {
					wallet.setDebitId(deb.getId());
					Float mount=deb.getAvailableBalance()+wallet.getAmount();
					deb.setAvailableBalance(mount);
					return debitRestClient.updateDebit(deb).flatMap(upd -> {
						return bankAccountRestClient.getFindId(upd.getAccountId().get(0)).flatMap(account -> {
							account.setAccountBalance(mount);
							return bankAccountRestClient.updateAccount(account).flatMap(x -> {
								response.setMessage("successful Deposit");
								Movement movement = new Movement();
								movement.setAccountId(deb.getId());
								movement.setAmount(newMount);
								movement.setCurrency(TypeCurrency.SOLES);
								movement.setCustomerId(deb.getCustomerId());
								movement.setProductType("DEBIT CARD");
								movement.setTransactionDate(date);
								movement.setTransactionType("DEPOSIT");
								return walletRestClient.walletDeposit(wallet).flatMap(w1 -> {
									wallet.setAmount(w1.getSaldo());
									wallet.setAvailableBalance(w1.getSaldo());
									response.setWalletDto(wallet);
									return transactionsRestClient.saveTransaction(movement).flatMap(t1 -> {
										movement.setAccountId(x.getAccountId());
										movement.setProductType(x.getAccountType());
										return transactionsRestClient.saveTransaction(movement).flatMap(t2 -> {
											movement.setAccountId(w.getId());
											movement.setProductType("WALLET");
											return transactionsRestClient.saveTransaction(movement).flatMap(t3 -> {
												return Mono.just(response);
											});
										});
									});
								});
							});
						});
					});
				});
			}else {
				return walletRestClient.walletDeposit(wallet).flatMap(w1 -> {
					wallet.setAmount(w1.getSaldo());
					wallet.setAvailableBalance(w1.getSaldo());
					response.setWalletDto(wallet);
					response.setMessage("successful Deposit");
					Movement movement = new Movement();
					movement.setAccountId(w1.getId());
					movement.setAmount(newMount);
					movement.setCurrency(TypeCurrency.SOLES);
					movement.setCustomerId(w.getTelefonoAsociado());
					movement.setProductType("WALLET");
					movement.setTransactionDate(date);
					movement.setTransactionType("DEPOSIT");
					return transactionsRestClient.saveTransaction(movement).flatMap(t1 -> {
						return Mono.just(response);
					});
				});
			}			
		}).defaultIfEmpty(response);
	}
	
	@Override
	public Mono<WalletResponse> walletPay(WalletDto wallet) {
		Wallet wal = new Wallet();
		wal.setId(wallet.getWalletId());
		wal.setIdDebito(wallet.getDebitId());
		wal.setSaldo(wallet.getAmount());
		WalletResponse response = new WalletResponse();
		Date date = new Date();
		response.setMessage("Wallet not exist");
		response.setWalletDto(wallet);
		float newMount= wallet.getAmount();
		return walletRestClient.getFindId(wallet.getWalletId()).flatMap(w -> {
			response.setWalletDto(wallet);
			if(w.getSaldo()<wallet.getAmount()) {
				response.setMessage("You don't have enough balance");
				return Mono.just(response);
			}
			if(w.getIdDebito()!=null) {
				return debitRestClient.getFindId(w.getIdDebito()).flatMap(deb -> {
					Float mount=deb.getAvailableBalance()-wallet.getAmount();
					deb.setAvailableBalance(mount);
					return debitRestClient.updateDebit(deb).flatMap(upd -> {
						return bankAccountRestClient.getFindId(upd.getAccountId().get(0)).flatMap(account -> {
							account.setAccountBalance(mount);
							return bankAccountRestClient.updateAccount(account).flatMap(x -> {
								response.setMessage("successful Pay");
								Movement movement = new Movement();
								movement.setAccountId(deb.getId());
								movement.setAmount(newMount);
								movement.setCurrency(TypeCurrency.SOLES);
								movement.setCustomerId(deb.getCustomerId());
								movement.setProductType("DEBIT CARD");
								movement.setTransactionDate(date);
								movement.setTransactionType("PAY");
								return walletRestClient.walletPay(wallet).flatMap(w1 -> {
									wallet.setDebitId(deb.getId());
									wallet.setAmount(w1.getSaldo());
									wallet.setAvailableBalance(w1.getSaldo());
									response.setWalletDto(wallet);
									return transactionsRestClient.saveTransaction(movement).flatMap(t1 -> {
										movement.setAccountId(x.getAccountId());
										movement.setProductType(x.getAccountType());
										return transactionsRestClient.saveTransaction(movement).flatMap(t2 -> {
											movement.setAccountId(w.getId());
											movement.setProductType("WALLET");
											return transactionsRestClient.saveTransaction(movement).flatMap(t3 -> {
												return Mono.just(response);
											});
										});
									});
								});
							});
						});
					});
				});
			}else {
				return walletRestClient.walletPay(wallet).flatMap(w1 -> {
					wallet.setAmount(w1.getSaldo());
					wallet.setAvailableBalance(w1.getSaldo());
					response.setWalletDto(wallet);
					response.setMessage("successful PAY");
					Movement movement = new Movement();
					movement.setAccountId(w1.getId());
					movement.setAmount(newMount);
					movement.setCurrency(TypeCurrency.SOLES);
					movement.setCustomerId(w.getTelefonoAsociado());
					movement.setProductType("WALLET");
					movement.setTransactionDate(date);
					movement.setTransactionType("PAY");
					return transactionsRestClient.saveTransaction(movement).flatMap(t1 -> {
						return Mono.just(response);
					});
				});
			}			
		}).defaultIfEmpty(response);
	}
	
}
