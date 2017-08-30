package com.debitum.assets.domain.model.user.activations;

import com.debitum.assets.domain.model.user.exception.ActionTokenAlreadyUsedException;

import javax.persistence.*;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;

import static com.debitum.assets.domain.model.user.activations.KeyType.INITIAL_PASSWORD_SET;
import static com.debitum.assets.domain.model.user.activations.KeyType.LOGIN;
import static com.debitum.assets.domain.model.user.activations.KeyType.PASSWORD_REMIND;


@Entity
@Table(name = "ACTION_TOKEN")
public class ActionToken {

    @Id
    @SequenceGenerator(
            name = "action_token_id_seq",
            sequenceName = "action_token_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "action_token_id_seq"
    )
    private Long id;

    @Column(name = "ACTIVATION_KEY")
    private String key;

    @Enumerated(EnumType.STRING)
    @Column(name = "KEY_TYPE")
    private KeyType type;

    @Column(name = "USER_ID")
    private UUID userId;

    @Column(
            name = "CREATED_TIMESTAMP_UTC",
            updatable = false
    )
    private Instant createdOn;

    @Column(
            name = "USED_TIMESTAMP_UTC",
            updatable = false
    )
    private Instant usedOn;

    ActionToken() {
    }

    private ActionToken(String key, KeyType type) {
        this.key = key;
        this.type = type;
        this.createdOn = Instant.now();
    }

    /**
     * @return action key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return user identifier for whom action token set
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * @return Instant this token was created on
     */
    public Instant getCreatedOn() {
        return createdOn;
    }

    /**
     * @return type of this action token
     */
    public KeyType getType() {
        return type;
    }

    /**
     * Set flag, that action key is used
     *
     * @return
     */
    public ActionToken useKey() {
        if (this.usedOn == null) {
            this.usedOn = Instant.now();
        } else {
            throw new ActionTokenAlreadyUsedException(this.key);
        }
        return this;
    }

    /**
     * @param userId user identifier
     * @return generates action key for initial password setup
     */
    public static final ActionToken generateInitialPasswordSetupKey(UUID userId) {
        ActionToken actionToken = new ActionToken(
                UUID.randomUUID().toString(),
                INITIAL_PASSWORD_SET
        );
        actionToken.userId = userId;
        return actionToken;
    }

    /**
     * @param userId user identifier
     * @return generates action key for password reminding
     */
    public static final ActionToken generatePasswordReminderKey(UUID userId) {
        ActionToken actionToken = new ActionToken(
                UUID.randomUUID().toString(),
                PASSWORD_REMIND
        );
        actionToken.userId = userId;
        return actionToken;
    }

    public static final ActionToken generateLoginActionToken(UUID userId) {
        ActionToken actionToken = new ActionToken(
                String.valueOf(100000 + new Random().nextInt(900000)),
                LOGIN
        );
        actionToken.userId = userId;
        return actionToken;
    }
}
