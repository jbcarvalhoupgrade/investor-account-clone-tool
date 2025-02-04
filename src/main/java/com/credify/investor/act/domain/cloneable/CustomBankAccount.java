package com.credify.investor.act.domain.cloneable;

import com.credify.enums.investor.InvestmentBankAccountType;
import com.credify.investorMgtSrvc.data.domain.BankAccount;
import com.credify.investorMgtSrvc.data.domain.InvestmentAccount;

import org.apache.commons.text.CaseUtils;
import com.credify.investor.act.domain.filereplace.FileReplaceInvestmentAccountBasedValues;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomBankAccount extends BankAccount implements CloneableInvestmentAccountBased {
    private Long investmentAccountId;

    public static CustomBankAccount clone(CustomBankAccount bankAccount, FileReplaceInvestmentAccountBasedValues replaceValues) {
        CustomBankAccount customBankAccount = new CustomBankAccount();

        customBankAccount.setInvestmentAccount(bankAccount.getInvestmentAccount());
        customBankAccount.setHolderName(bankAccount.getHolderName());
        customBankAccount.setType(bankAccount.getType());
        customBankAccount.setDescriptor(bankAccount.getDescriptor());

        Map<String, Function<Object, Object>> parsers = Map.ofEntries(
                Map.entry("id", input -> Integer.toUnsignedLong((Integer) input)),
                Map.entry("type", input -> InvestmentBankAccountType.valueOf((String) input)),
                Map.entry("investmentAccount",
                        input -> InvestmentAccount
                                .builder()
                                .id(Integer.toUnsignedLong((Integer) input))
                                .build())
        );

        replaceValues
                .getNewValues()
                .forEach((key, value) -> {

                    String fieldName = CaseUtils.toCamelCase(key, false, '_');

                    try {
                        Field field;

                        if (!fieldName.equals("investmentAccountId")) {
                            field = customBankAccount
                                    .getClass()
                                    .getSuperclass()
                                    .getDeclaredField(fieldName);
                        } else {
                            field = CustomBankAccount.class.getDeclaredField(fieldName);
                        }

                        field.setAccessible(true);

                        field.set(customBankAccount, Optional
                                .ofNullable(parsers.get(fieldName))
                                .map(mapperFunction -> mapperFunction.apply(value))
                                .orElse(value));

                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });

        return customBankAccount;

    }

}
