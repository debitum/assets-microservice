package com.debitum.assets.domain.model.security;


/**
 * Data guardian provider.
 */
public class DataGuardianProvider {

    private static final DataGuardian guardian = new DataGuardian();

    private DataGuardianProvider() {
    }

    /**
     * Singleton data guardian.
     *
     * @return the data guardian
     */
    public static DataGuardian guardian() {
        return guardian;
    }

}
