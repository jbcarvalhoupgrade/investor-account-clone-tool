package com.credify.investor.act.domain.cloneable;

import com.credify.investorMgtSrvc.data.domain.CashSweepConfig;
import com.credify.investorMgtSrvc.data.domain.InvestmentAccount;
import com.credify.investorMgtSrvc.enums.CashSweepCadence;
import com.credify.investorMgtSrvc.enums.FeeTreatment;
import com.credify.investorMgtSrvc.enums.SweepDistributionAccountType;

import org.apache.commons.text.CaseUtils;
import com.credify.investor.act.domain.filereplace.FileReplaceInvestmentAccountBasedValues;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public class CustomCashSweepConfig extends CashSweepConfig implements CloneableInvestmentAccountBased {
    private Long investmentAccountId;

    public static CustomCashSweepConfig clone(CustomCashSweepConfig memberizationRequirements, FileReplaceInvestmentAccountBasedValues replaceValues) {
        CustomCashSweepConfig customCashSweepConfig = new CustomCashSweepConfig();

        customCashSweepConfig.setInvestmentAccount(memberizationRequirements.getInvestmentAccount());

        customCashSweepConfig.setCashSweepsRequireReconciliation(memberizationRequirements.getCashSweepsRequireReconciliation());
        customCashSweepConfig.setRequiresServicingAccountSegregation(memberizationRequirements.getRequiresServicingAccountSegregation());
        customCashSweepConfig.setAutomateCashSweeps(memberizationRequirements.getAutomateCashSweeps());
        customCashSweepConfig.setAutomateCashSweepsAfterDate(memberizationRequirements.getAutomateCashSweepsAfterDate());

        customCashSweepConfig.setDistributionPercentageCap(memberizationRequirements.getDistributionPercentageCap());
        customCashSweepConfig.setFeeTreatment(memberizationRequirements.getFeeTreatment());
        customCashSweepConfig.setCashSweepCadence(memberizationRequirements.getCashSweepCadence());
        customCashSweepConfig.setFirstDistributionDate(memberizationRequirements.getFirstDistributionDate());

        customCashSweepConfig.setPrincipalBankAccount(memberizationRequirements.getPrincipalBankAccount());
        customCashSweepConfig.setInterestBankAccount(memberizationRequirements.getInterestBankAccount());
        customCashSweepConfig.setOtherBankAccount(memberizationRequirements.getOtherBankAccount());
        customCashSweepConfig.setFeesBankAccount(memberizationRequirements.getFeesBankAccount());

        Map<String, Function<Object, Object>> parsers = Map.ofEntries(
                Map.entry("id", input -> Integer.toUnsignedLong((Integer) input)),
                Map.entry("cashSweepsRequireReconciliation", input -> Boolean.valueOf((String) input)),
                Map.entry("requiresServicingAccountSegregation", input -> Boolean.valueOf((String) input)),
                Map.entry("automateCashSweeps", input -> Boolean.valueOf((String) input)),

                Map.entry("feeTreatment", input -> FeeTreatment.valueOf((String) input)),
                Map.entry("cashSweepCadence", input -> CashSweepCadence.valueOf((String) input)),
                Map.entry("principalBankAccount", input -> SweepDistributionAccountType.valueOf((String) input)),
                Map.entry("interestBankAccount", input -> SweepDistributionAccountType.valueOf((String) input)),
                Map.entry("otherBankAccount", input -> SweepDistributionAccountType.valueOf((String) input)),
                Map.entry("feesBankAccount", input -> SweepDistributionAccountType.valueOf((String) input)),

                Map.entry("distributionPercentageCap", input -> new BigDecimal((String) input)),
                Map.entry("automateCashSweepsAfterDate", input -> LocalDate.parse((String) input)),
                Map.entry("firstDistributionDate", input -> LocalDate.parse((String) input)),

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
                            field = customCashSweepConfig
                                    .getClass()
                                    .getSuperclass()
                                    .getDeclaredField(fieldName);
                        } else {
                            field = CustomCashSweepConfig.class.getDeclaredField(fieldName);
                        }

                        field.setAccessible(true);

                        field.set(customCashSweepConfig, Optional
                                .ofNullable(parsers.get(fieldName))
                                .map(mapperFunction -> mapperFunction.apply(value))
                                .orElse(value));

                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });

        return customCashSweepConfig;

    }

}
