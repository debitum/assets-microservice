package com.debitum.assets.domain.model.event;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DomainEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(DomainEventPublisher.class);

    private static ApplicationEventPublisher applicationEventPublisher;
    private static final ThreadLocal<DomainEvent> lastPublishedEvent = new ThreadLocal<>();

    @Autowired(required = true)
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        if (DomainEventPublisher.applicationEventPublisher == null) {
            DomainEventPublisher.applicationEventPublisher = applicationEventPublisher;
        }
    }

    public static void publish(String message) {
        log.info("Publishing custom event. ");
        DomainEvent customSpringEvent = new DomainEvent(message);
        publish(customSpringEvent);
    }

    public static void publish(DomainEvent message) {
        Validate.notNull(
                message,
                "In case to publish event - define not null event"
        );

        log.info(
                "Publishing custom event: {}. Message: {}",
                message.getClass(),
                message.getMessage()
        );

        lastPublishedEvent.set(message);
        applicationEventPublisher.publishEvent(message);
    }

    public static class DomainEvent<D> extends ApplicationEvent {
        private D message;

        public DomainEvent(Object source,
                           D message) {
            super(source);
            this.message = message;
        }

        public DomainEvent(D message) {
            this(
                    new DomainEventPublisher(),
                    message
            );
        }

        public D getMessage() {
            return message;
        }
    }

    public static DomainEventPublisher.DomainEvent popLastEvent() {
        DomainEventPublisher.DomainEvent domainEvent = lastPublishedEvent.get();
        lastPublishedEvent.remove();
        return domainEvent;
    }
}