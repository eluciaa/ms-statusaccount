package com.nttdata.bootcamp.ms.statusaccount.domain.service;

import com.nttdata.bootcamp.ms.statusaccount.domain.dto.WalletDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.WalletResponse;

import reactor.core.publisher.Mono;

public interface StatusWalletService {

	Mono<WalletResponse> createWallet(WalletDto wallet);

	Mono<WalletResponse> walletDeposit(WalletDto wallet);

	Mono<WalletResponse> walletPay(WalletDto wallet);


}
