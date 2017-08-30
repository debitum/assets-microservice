package com.debitum.assets.resource.user;


import com.debitum.assets.RandomValueHelper;
import com.debitum.assets.domain.model.user.UserStatus;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserDTOFixture {



    public static UserDTO userWith(UUID id) {
        UserDTO newUser = new UserDTO();
        newUser.setId(id);
        newUser.setLogin(RandomValueHelper.randomLogin());
        newUser.setName(RandomValueHelper.randomString());
        newUser.setCompany(RandomValueHelper.randomString());
        newUser.setPhone(RandomValueHelper.randomString());

        newUser.setStatus(UserStatus.ACTIVE);

        return newUser;
    }

    public static UserDTO newUser() {
        return userWith(null);
    }
}
