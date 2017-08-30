package com.debitum.assets.domain.model.user.events;


import com.debitum.assets.domain.model.event.DomainEventPublisher;
import com.debitum.assets.domain.model.user.User;

/**
 * Domain event which is published when user activation link resend activated.
 */
public class ActivationLinkResendActivated extends DomainEventPublisher.DomainEvent<User> {
    public ActivationLinkResendActivated(User user) {
        super(user);
    }
}
