package org.com.credify.investorMgtSrvc.tool.domain.cloneable;

import com.credify.investorMgtSrvc.data.domain.PurchasePriceConfig;
import com.credify.investorMgtSrvc.enums.ProgramDefinitionFeature;
import com.credify.investorMgtSrvc.enums.PurchasePriceInterestType;
import com.credify.investorMgtSrvc.enums.PurchasePricePrincipalType;

import org.apache.commons.text.CaseUtils;
import org.com.credify.investorMgtSrvc.tool.domain.filereplace.FileReplaceInvestmentAccountBasedValues;

import java.lang.reflect.Field;
import java.math.BigDecimal;
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
public class CustomPurchasePriceConfig extends PurchasePriceConfig implements CloneableInvestmentAccountBased {
    private Long investmentAccountId;
    private Long accountConfigId;

    public static CustomPurchasePriceConfig clone(CustomPurchasePriceConfig memberizationRequirements, FileReplaceInvestmentAccountBasedValues replaceValues) {
        CustomPurchasePriceConfig customPurchasePriceConfig = new CustomPurchasePriceConfig();

        customPurchasePriceConfig.setProgramDefinitionFeature(memberizationRequirements.getProgramDefinitionFeature());
        customPurchasePriceConfig.setPrincipalType(memberizationRequirements.getPrincipalType());
        customPurchasePriceConfig.setInterestType(memberizationRequirements.getInterestType());
        customPurchasePriceConfig.setAprRate(memberizationRequirements.getAprRate());
        customPurchasePriceConfig.setIsAprIncludeSofr(memberizationRequirements.getIsAprIncludeSofr());
        customPurchasePriceConfig.setActorId("liquibase");

        Map<String, Function<Object, Object>> parsers = Map.ofEntries(
                Map.entry("id", input -> Integer.toUnsignedLong((Integer) input)),

                Map.entry("isAprIncludeSofr", input -> Boolean.valueOf((String) input)),
                Map.entry("programDefinitionFeature", input -> ProgramDefinitionFeature.valueOf((String) input)),
                Map.entry("principalType", input -> PurchasePricePrincipalType.valueOf((String) input)),
                Map.entry("interestType", input -> PurchasePriceInterestType.valueOf((String) input)),

                Map.entry("aprRate", input -> new BigDecimal((String) input)),
                Map.entry("investmentAccountId", input -> Integer.toUnsignedLong((Integer) input))
        );

        replaceValues
                .getNewValues()
                .forEach((key, value) -> {

                    String fieldName = CaseUtils.toCamelCase(key, false, '_');

                    try {
                        Field field;

                        if (!fieldName.equals("investmentAccountId")) {
                            field = customPurchasePriceConfig
                                    .getClass()
                                    .getSuperclass()
                                    .getDeclaredField(fieldName);
                        } else {
                            field = CustomPurchasePriceConfig.class.getDeclaredField(fieldName);
                        }

                        field.setAccessible(true);

                        field.set(customPurchasePriceConfig, Optional
                                .ofNullable(parsers.get(fieldName))
                                .map(mapperFunction -> mapperFunction.apply(value))
                                .orElse(value));

                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });

        return customPurchasePriceConfig;

    }

}