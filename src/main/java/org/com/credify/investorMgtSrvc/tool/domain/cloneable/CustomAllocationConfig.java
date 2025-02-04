package org.com.credify.investorMgtSrvc.tool.domain.cloneable;

import com.credify.investorMgtSrvc.data.domain.AllocationConfig;
import com.credify.investorMgtSrvc.data.domain.enums.CreditSource;
import com.credify.investorMgtSrvc.data.domain.InvestmentAccount;
import com.credify.investorMgtSrvc.data.domain.enums.AssetType;
import com.credify.investorMgtSrvc.enums.AllocationDisableReason;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.text.CaseUtils;
import org.com.credify.investorMgtSrvc.tool.domain.filereplace.FileReplaceInvestmentAccountBasedValues;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomAllocationConfig extends AllocationConfig implements CloneableInvestmentAccountBased {
    private Long investmentAccountId;
    private Long issuingBankId;
    private Long defaultSeasonerId;

    public static CustomAllocationConfig clone(CustomAllocationConfig allocationConfig, FileReplaceInvestmentAccountBasedValues replaceValues) {
        CustomAllocationConfig customAllocationConfig = new CustomAllocationConfig();

        customAllocationConfig.setInvestmentAccount(allocationConfig.getInvestmentAccount());

        customAllocationConfig.setIsAllocationEnabled(allocationConfig.getIsAllocationEnabled());
        customAllocationConfig.setIsCompetitionPoolEnabled(allocationConfig.getIsCompetitionPoolEnabled());
        customAllocationConfig.setIsBeneficiaryAllocationEnabled(allocationConfig.getIsBeneficiaryAllocationEnabled());
        customAllocationConfig.setIsKeepCurrentAllocationEnabled(allocationConfig.getIsKeepCurrentAllocationEnabled());
        customAllocationConfig.setEligibleForPreAllocationOnReadyForIssueOrOpen(allocationConfig.getEligibleForPreAllocationOnReadyForIssueOrOpen());

        customAllocationConfig.setPriority(allocationConfig.getPriority());
        customAllocationConfig.setWeight(allocationConfig.getWeight());

        customAllocationConfig.setIssuingBank(allocationConfig.getIssuingBank());
        customAllocationConfig.setDefaultSeasonerAccount(allocationConfig.getDefaultSeasonerAccount());

        customAllocationConfig.setMonthlyTargetPct(allocationConfig.getMonthlyTargetPct());
        customAllocationConfig.setExpectedApplicationFalloutPct(allocationConfig.getExpectedApplicationFalloutPct());
        customAllocationConfig.setPortfolioMixTargetAmount(allocationConfig.getPortfolioMixTargetAmount());
        customAllocationConfig.setMonthlyDollarCapAmount(allocationConfig.getMonthlyDollarCapAmount());
        customAllocationConfig.setMonthlyTargetDollarAmount(allocationConfig.getMonthlyTargetDollarAmount());

        customAllocationConfig.setProjectedAllocationEnableDate(allocationConfig.getProjectedAllocationEnableDate());

        customAllocationConfig.setAllocationDisableNotes(allocationConfig.getAllocationDisableNotes());
        customAllocationConfig.setAllocationDisableReason(allocationConfig.getAllocationDisableReason());
        customAllocationConfig.setSecondBrand(allocationConfig.getSecondBrand());

        customAllocationConfig.setAssetType(allocationConfig.getAssetType());
        customAllocationConfig.setCreditSources(allocationConfig.getCreditSources());
        customAllocationConfig.setActorId("liquibase");

        Map<String, Function<Object, Object>> parsers = Map.ofEntries(
                Map.entry("id", input -> Integer.toUnsignedLong((Integer) input)),

                Map.entry("isAllocationEnabled", input -> Boolean.valueOf((String) input)),
                Map.entry("isCompetitionPoolEnabled", input -> Boolean.valueOf((String) input)),
                Map.entry("isBeneficiaryAllocationEnabled", input -> Boolean.valueOf((String) input)),
                Map.entry("isKeepCurrentAllocationEnabled", input -> Boolean.valueOf((String) input)),
                Map.entry("eligibleForPreAllocationOnReadyForIssueOrOpen", input -> Boolean.valueOf((String) input)),

                Map.entry("priority", input -> Integer.valueOf((String) input)),
                Map.entry("weight", input -> Integer.valueOf((String) input)),

                Map.entry("monthlyTargetPct", input -> new BigDecimal((String) input)),
                Map.entry("expectedApplicationFalloutPct", input -> new BigDecimal((String) input)),
                Map.entry("portfolioMixTargetAmount", input -> new BigDecimal((String) input)),
                Map.entry("monthlyDollarCapAmount", input -> new BigDecimal((String) input)),
                Map.entry("monthlyTargetDollarAmount", input -> new BigDecimal((String) input)),

                Map.entry("projectedAllocationEnableDate", input -> LocalDate.parse((String) input)),

                Map.entry("assetType", input -> AssetType.valueOf((String) input)),
                Map.entry("allocationDisableReason", input -> AllocationDisableReason.valueOf((String) input)),

                Map.entry("investmentAccount",
                        input -> InvestmentAccount
                                .builder()
                                .id(Integer.toUnsignedLong((Integer) input))
                                .build()),
                Map.entry("issuingBank",
                        input -> Optional.ofNullable(input).map(id -> InvestmentAccount
                                        .builder()
                                        .id(Long.valueOf((String) id))
                                        .build())
                                .orElse(null)
                ),
                Map.entry("defaultSeasonerAccount",
                        input -> Optional.ofNullable(input).map(id -> InvestmentAccount
                                        .builder()
                                        .id(Long.valueOf((String) id))
                                        .build())
                                .orElse(null)
                ),
                Map.entry("creditSources", input ->
                        ((List<String>)input)
                                .stream()
                                .map(CreditSource::valueOf)
                                .collect(Collectors.toSet()))
        );

        replaceValues
                .getNewValues()
                .forEach((key, value) -> {

                    String fieldName = CaseUtils.toCamelCase(key, false, '_');

                    try {
                        Field field;

                        if (!fieldName.equals("investmentAccountId")) {
                            field = allocationConfig
                                    .getClass()
                                    .getSuperclass()
                                    .getDeclaredField(fieldName);
                        } else {
                            field = CustomAllocationConfig.class.getDeclaredField(fieldName);
                        }

                        field.setAccessible(true);

                        field.set(allocationConfig, Optional
                                .ofNullable(parsers.get(fieldName))
                                .map(mapperFunction -> mapperFunction.apply(value))
                                .orElse(value));

                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });

        return allocationConfig;
    }

    public String getCreditSourcesAsString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(getCreditSources());
    }
}
