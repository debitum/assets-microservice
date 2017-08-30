package com.debitum.assets.port.adapter.user.persistence;



import com.debitum.assets.domain.model.user.UserStatus;

import java.util.List;
import java.util.Optional;

public class UserFilter {

    private Optional<String> name = Optional.empty();

    private Optional<List<UserStatus>> states = Optional.empty();

    public void setName(String name) {
        this.name = Optional.ofNullable(name);
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<List<UserStatus>> getStates() {
        return states;
    }

    public void setStates(List<UserStatus> status) {
        this.states = Optional.ofNullable(status);
    }

}
