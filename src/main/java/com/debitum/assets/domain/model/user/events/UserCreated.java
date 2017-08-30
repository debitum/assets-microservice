package com.debitum.assets.domain.model.user.events;


import com.debitum.assets.domain.model.event.DomainEventPublisher;
import com.debitum.assets.domain.model.user.User;

/**
 * Domain event which is published when user is created.
 */
public class UserCreated extends DomainEventPublisher.DomainEvent<User> {
    public UserCreated(User user) {
        super(user);
    }
}
