package com.debitum.assets.domain.model.helpers;


import com.debitum.assets.UnitTestBase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenGeneratorTest extends UnitTestBase {

    @Test
    public void givenNothing_whenGeneratingToken_thenNotEmptyTokenGenerated(){
        //given nothing

        //when
        String token = TokenGenerator.generateToken();

        //then
        assertThat(token).isNotNull();
    }
}
