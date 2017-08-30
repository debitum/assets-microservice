package com.debitum.assets.domain.model.investment;


import com.debitum.assets.UnitTestBase;
import com.debitum.assets.resource.investment.InvestmentEntryFixture;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;

public class InvestmentEntryTest extends UnitTestBase {

    @Test
    public void givenExistingInvestment_whenRepayingLoan_thenRepaymentCalculatedInEth() {
        //given
        InvestmentEntry entry = InvestmentEntryFixture.domainFixture(
                3000d,
                Instant.now().minus(20, ChronoUnit.DAYS),
                fromString("0dc6ced3-2f9c-440e-9ad5-012bbb49cb5a"),
                fromString("0dc6ced3-2f9c-440e-9ad5-012bbb49cb5a")
        ).get(0);
        Invoice invoice = new Invoice(
                LoanType.INVOICE_FINANCING,
                Instant.now(),
                3000d,
                2.3d,
                2.3d,
                "SOME",
                2.3d,
                null,
                null,
                "BUSINES SECTOR",
                "Some invoice description",
                "Transaction",
                "RISKY");

        //when
        entry.repay(invoice);

        //then
        assertThat(entry.getRepaidAmountEth() - entry.getAmountEth()).isGreaterThan(0);
        assertThat(CoinPrice.convertEthAmountToRepresentative(entry.getRepaidAmountEth() - entry.getAmountEth())).isEqualTo(0.003526);
    }
}
