package com.debitum.assets.domain.model.investment;


import com.debitum.assets.domain.model.AuditedEntity;
import com.debitum.assets.domain.model.event.DomainEventPublisher;
import com.debitum.assets.domain.model.investment.events.InvestmentPaid;
import com.debitum.assets.domain.model.investment.events.InvestmentSentToBlockchain;
import com.debitum.assets.domain.model.security.UsersProperty;
import org.apache.commons.lang.Validate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.debitum.assets.domain.model.helpers.TokenGenerator.generateToken;
import static com.debitum.assets.domain.model.investment.InvestmentStatus.*;
import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

@Entity
@Table(name = "INVESTMENT")
public class Investment extends AuditedEntity implements UsersProperty {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @Column(name = "USER_ID")
    private UUID userId;

    @Column(name = "CONTRACT_TOKEN")
    private String contractToken;

    @Column(name = "TOTAL_AMOUNT")
    private Double totalAmount;

    @Column(name = "TOTAL_AMOUNT_ETH")
    private Long totalAmountEth;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "INVESTMENT_ID")
    @Fetch(FetchMode.SELECT)
    private List<InvestmentEntry> investments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private InvestmentStatus status;

    Investment() {
    }

    public Investment(UUID userId) {
        this.userId = userId;
        this.totalAmount = 0d;
        this.contractToken = generateToken();
        this.status = PENDING;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public UUID getUserId() {
        return userId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public List<InvestmentEntry> getInvestments() {
        return investments;
    }

    public InvestmentStatus getStatus() {
        return status;
    }

    public Investment resetAmount() {
        this.totalAmount = 0d;
        this.totalAmountEth = 0L;
        investments.clear();
        return this;
    }

    public String getContractToken() {
        return contractToken;
    }

    public Long getTotalAmountEth() {
        return (long) defaultIfNull(totalAmountEth, 0L);
    }

    public Investment addEntry(InvestmentEntry entry) {
        Validate.notNull(entry.getAmount(), "Amount is mandatory for investment entry");
        Validate.isTrue(entry.getAmount() > 0, "Amount must be > 0");
        investments.add(entry);
        this.totalAmount += entry.getAmount();
        return this;
    }


    public InvestmentEntry makeInvestmentEntry(UUID invoiceId, Double amount, CoinPrice coinPrice) {
        Validate.notNull(invoiceId, "Invoice identifier is mandatory for investment entry");
        Validate.notNull(amount, "Amount is mandatory for investment entry");
        Validate.isTrue(amount > 0, "Amount must be > 0");
        InvestmentEntry entry = new InvestmentEntry(invoiceId, getId(), amount, coinPrice);
        this.totalAmount += amount;
        return entry;
    }

    public Investment declareAmountInEth(CoinPrice ethPrice) {
        this.totalAmountEth = ethPrice.declareEthAmountForContract(totalAmount);
        return this;
    }

    public Investment markAsSentToBlockchain() {
        if (getStatus() == null || getStatus() == PENDING) {
            this.status = SENT_TO_BLOCKCHAIN;
            DomainEventPublisher.publish(new InvestmentSentToBlockchain(this));
        }
        return this;
    }

    public Investment markAsPaid() {
        if (getStatus() == null || getStatus() == SENT_TO_BLOCKCHAIN) {
            this.status = PAID;
            DomainEventPublisher.publish(new InvestmentPaid(this));
        }
        return this;
    }
}
