package com.debitum.assets.port.adapter.investment;


import com.debitum.assets.UnitTestBase;
import com.debitum.assets.domain.model.investment.Investment;
import com.debitum.assets.domain.model.investment.InvestmentNotificationService;
import com.debitum.assets.domain.model.investment.events.InvestmentCreated;
import com.debitum.assets.domain.model.watcher.InvestmentPublishingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static java.util.UUID.fromString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class InvestmentEventListenerTest extends UnitTestBase {

    private InvestmentEventListener listener;

    private final InvestmentPublishingService investmentPublishingService = mock(InvestmentPublishingService.class);
    private final InvestmentNotificationService investmentNotificationService = mock(InvestmentNotificationService.class);
    private final UUID EXISTING_INVESTMENT_ID = fromString("0dc6ced3-2f9c-440e-9ad5-012bbb49cb5a");
    private final Investment investment = new Investment(UUID.fromString("0dc6ced3-2f9c-440e-9ad5-012bbb49cb5a")) {
        @Override
        public UUID getId() {
            return EXISTING_INVESTMENT_ID;
        }
    };

    @Before
    public void setUp() throws Exception {
        listener = new InvestmentEventListener(
                investmentPublishingService,
                investmentNotificationService
        );
    }

    @Test
    public void givenCreatedInvestment_whenInvestmentCreatedEventPublished_thenInvestmentPublishedToBlockchain() throws JsonProcessingException {
        //given
        InvestmentCreated event = new InvestmentCreated(investment);

        //when
        listener.onEvent(event);

        //then
        verify(investmentPublishingService,
                times(1)).publishInvestment(eq(EXISTING_INVESTMENT_ID));
    }
}
