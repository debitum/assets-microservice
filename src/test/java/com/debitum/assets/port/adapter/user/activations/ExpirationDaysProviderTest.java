package com.debitum.assets.port.adapter.user.activations;




import com.debitum.assets.IntegrationTestBase;
import org.junit.Test;

import javax.inject.Inject;


import static com.debitum.assets.domain.model.user.activations.KeyType.INITIAL_PASSWORD_SET;
import static com.debitum.assets.domain.model.user.activations.KeyType.PASSWORD_REMIND;
import static org.assertj.core.api.Assertions.assertThat;

public class ExpirationDaysProviderTest extends IntegrationTestBase {

    @Inject
    private ExpirationDaysProvider provider;

    @Test
    public void givenExpirationDaysParams_whenExtractingParamByKeyType_thenProviderReturnsExpirationInDays(){
        //when
        Integer initialPasswordSetExpirationDays = provider.getActionTokenExpirationDays(INITIAL_PASSWORD_SET);
        Integer passwordRemindExpirationDays = provider.getActionTokenExpirationDays(PASSWORD_REMIND);

        //then
        assertThat(passwordRemindExpirationDays).isEqualTo(7);
        assertThat(initialPasswordSetExpirationDays).isEqualTo(7);
    }
}
