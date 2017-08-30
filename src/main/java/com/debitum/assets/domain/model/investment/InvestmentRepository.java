package com.debitum.assets.domain.model.investment;


import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface InvestmentRepository {
    List<Investment> findAll();

    List<Investment> findAll(UUID userId);

    List<Investment> findAll(List<UUID> investmentIds);

    Investment get(UUID investmentId);

    Investment getByContractToken(String contractToken);

    Investment save(Investment investment);

    int deleteExpiredInvestments(Instant createdBefore);
}
