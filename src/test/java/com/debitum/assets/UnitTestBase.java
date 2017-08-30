package com.debitum.assets;


import com.debitum.assets.domain.model.event.DomainEventPublisher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

public abstract class UnitTestBase {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    private static final ThreadLocal<DomainEventPublisher.DomainEvent> publishedEvents = new ThreadLocal<>();

    @Before
    public void initMockDomainEventPublisher() throws Exception {
        new DomainEventPublisher().setApplicationEventPublisher(new ApplicationEventPublisher() {
            @Override
            public void publishEvent(ApplicationEvent event) {
                publishedEvents.set((DomainEventPublisher.DomainEvent) event);
            }

            @Override
            public void publishEvent(Object event) {

            }
        });
    }

    @After
    public void tearDown() throws Exception {
        publishedEvents.remove();
    }

    protected static DomainEventPublisher.DomainEvent popLastEvent() {
        return DomainEventPublisher.popLastEvent();
    }

}