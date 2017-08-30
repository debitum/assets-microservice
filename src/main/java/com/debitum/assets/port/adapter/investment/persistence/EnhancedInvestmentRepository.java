package com.debitum.assets.port.adapter.investment.persistence;

import com.debitum.assets.domain.model.investment.Investment;
import com.debitum.assets.domain.model.investment.InvestmentNotificationService;
import com.debitum.assets.domain.model.investment.InvestmentRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.debitum.assets.domain.model.investment.InvestmentStatus.PAID;
import static com.debitum.assets.domain.model.investment.InvestmentStatus.PENDING;

@Component
class EnhancedInvestmentRepository implements InvestmentRepository {
    private final SpringDataInvestmentRepository repository;
    private final SpringDataInvestmentEntryRepository entryRepository;


    public EnhancedInvestmentRepository(SpringDataInvestmentRepository repository,
                                        SpringDataInvestmentEntryRepository entryRepository) {
        this.repository = repository;
        this.entryRepository = entryRepository;
    }

    @Override
    public List<Investment> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Investment> findAll(UUID userId) {
        return repository.findAllByUserId(userId);
    }

    @Override
    public List<Investment> findAll(List<UUID> investmentIds) {
        return repository.findAll(investmentIds);
    }

    @Override
    public Investment get(UUID investmentId) {
        return repository.getOne(investmentId);
    }

    @Override
    public Investment getByContractToken(String contractToken) {
        return repository.getByContractToken(contractToken);
    }

    @Override
    public Investment save(Investment investment) {
        return repository.save(investment);
    }

    @Override
    public int deleteExpiredInvestments(Instant createdBefore) {
        List<Investment> expiredInvestment = repository.getExpiredInvestmentIds(PAID, createdBefore);

        if (CollectionUtils.isNotEmpty(expiredInvestment)) {
            entryRepository.deleteEntriesOfInvestments(
                    expiredInvestment.stream()
                            .filter(investment -> investment != null)
                            .map(Investment::getId)
                            .collect(Collectors.toList())
            );
            return repository.deleteExpiredTokens(PAID, createdBefore);
        }

        return 0;
    }


}
