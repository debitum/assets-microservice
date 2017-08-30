package com.debitum.assets.resource.investment;


import java.util.UUID;

public class InvestmentFixture {

    public static InvestmentDTO fixture(UUID userId) {
        InvestmentDTO dto = new InvestmentDTO();
        dto.setUserId(userId);
        return dto;
    }

}
