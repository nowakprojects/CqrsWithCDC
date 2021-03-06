package com.gonnect.debezium.kafka.bank.account.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class DebitCard {

    @Id
    @Getter private String id;
    private BigDecimal initialLimit;
    private BigDecimal usedLimit = BigDecimal.ZERO;

    public DebitCard(BigDecimal limit) {
        this.initialLimit = limit;
        this.id = UUID.randomUUID().toString();
    }

    public void withdraw(BigDecimal amount) {
        if (thereIsMoneyToWithdraw(amount)) {
            usedLimit = usedLimit.add(amount);
        } else {
            throw new InSufficientMoneyException(id, amount, availableBalance());
        }
    }

    public BigDecimal availableBalance() {
        return initialLimit.subtract(usedLimit);
    }

    private boolean thereIsMoneyToWithdraw(BigDecimal amount) {
        return availableBalance().compareTo(amount) >= 0;
    }

}
