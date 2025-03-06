package com.credify.investor.act.domain.cloneable;

import com.credify.enums.product.LoanProductType;
import com.credify.investor.act.domain.filereplace.FileReplaceInvestmentAccountBasedValues;
import com.credify.investorPreAllocation.common.enums.AssetType;
import com.credify.investorPreAllocation.core.data.domain.PreAllocationConfig;
import com.credify.preAllocationSrvc.enums.AllocationDisableReason;
import com.credify.preAllocationSrvc.enums.AllocationPolicy;
import com.credify.preAllocationSrvc.enums.InvestmentAccountType;
import com.credify.preAllocationSrvc.enums.LoanProductSubType;
import com.credify.preAllocationSrvc.enums.RegionStatus;

import org.apache.commons.text.CaseUtils;

import java.lang.reflect.Field;
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
public class CustomPreAllocationConfig extends PreAllocationConfig implements CloneableInvestmentAccountBased {
    private Long accountConfigId;

    public static CustomPreAllocationConfig clone(CustomPreAllocationConfig preAllocationConfig, FileReplaceInvestmentAccountBasedValues replaceValues) {
        CustomPreAllocationConfig customPreAllocationConfig = new CustomPreAllocationConfig();

        customPreAllocationConfig.setInvestmentAccountId(preAllocationConfig.getInvestmentAccountId());
        customPreAllocationConfig.setInstitutionId(preAllocationConfig.getInstitutionId());
        customPreAllocationConfig.setIssuingBankId(preAllocationConfig.getIssuingBankId());
        customPreAllocationConfig.setPriority(preAllocationConfig.getPriority());
        customPreAllocationConfig.setWeight(preAllocationConfig.getWeight());
        customPreAllocationConfig.setProjectedAllocationEnableDate(preAllocationConfig.getProjectedAllocationEnableDate());
        customPreAllocationConfig.setIsAllocationEnabled(preAllocationConfig.getIsAllocationEnabled());
        customPreAllocationConfig.setIsCompetitionPoolEnabled(preAllocationConfig.getIsCompetitionPoolEnabled());
        customPreAllocationConfig.setIsKeepCurrentAllocationEnabled(preAllocationConfig.getIsKeepCurrentAllocationEnabled());
        customPreAllocationConfig.setEligibleForPreAllocationOnReadyForIssueOrOpen(preAllocationConfig.getEligibleForPreAllocationOnReadyForIssueOrOpen());
        customPreAllocationConfig.setPassApplicantPersonalInformationEnabled(preAllocationConfig.getPassApplicantPersonalInformationEnabled());
        customPreAllocationConfig.setAssetType(preAllocationConfig.getAssetType());
        customPreAllocationConfig.setProductType(preAllocationConfig.getProductType());
        customPreAllocationConfig.setProductSubType(preAllocationConfig.getProductSubType());
        customPreAllocationConfig.setInvestmentAccountType(preAllocationConfig.getInvestmentAccountType());
        customPreAllocationConfig.setAllocationPolicy(preAllocationConfig.getAllocationPolicy());
        customPreAllocationConfig.setAllocationDisableReason(preAllocationConfig.getAllocationDisableReason());
        customPreAllocationConfig.setRegionStatus(preAllocationConfig.getRegionStatus());
        customPreAllocationConfig.setCreditSources(preAllocationConfig.getCreditSources());
        customPreAllocationConfig.setInvestmentAccountExtIntegrationName(preAllocationConfig.getInvestmentAccountExtIntegrationName());
        customPreAllocationConfig.setInvestmentAccountExtAccountNumber(preAllocationConfig.getInvestmentAccountExtAccountNumber());
        customPreAllocationConfig.setAllocationDisableNotes(preAllocationConfig.getAllocationDisableNotes());
        customPreAllocationConfig.setSecondBrand(preAllocationConfig.getSecondBrand());
        customPreAllocationConfig.setActorId("liquibase");

        Map<String, Function<Object, Object>> parsers = Map.ofEntries(
                Map.entry("institutionId", input -> Integer.toUnsignedLong((Integer) input)),
                Map.entry("issuingBankId", input -> Integer.toUnsignedLong((Integer) input)),
                Map.entry("investmentAccountId", input -> Integer.toUnsignedLong((Integer) input)),
                Map.entry("priority", input -> ((Integer) input)),
                Map.entry("weight", input -> ((Integer) input)),

                Map.entry("projectedAllocationEnableDate", input -> LocalDate.parse((String) input)),

                Map.entry("isAllocationEnabled", input -> Boolean.valueOf((String) input)),
                Map.entry("isCompetitionPoolEnabled", input -> Boolean.valueOf((String) input)),
                Map.entry("isKeepCurrentAllocationEnabled", input -> Boolean.valueOf((String) input)),
                Map.entry("eligibleForPreAllocationOnReadyForIssueOrOpen", input -> Boolean.valueOf((String) input)),
                Map.entry("passApplicantPersonalInformationEnabled", input -> Boolean.valueOf((String) input)),

                Map.entry("assetType", input -> AssetType.valueOf((String) input)),
                Map.entry("productType", input -> LoanProductType.valueOf((String) input)),
                Map.entry("productSubType", input -> LoanProductSubType.valueOf((String) input)),
                Map.entry("investmentAccountType", input -> InvestmentAccountType.valueOf((String) input)),
                Map.entry("allocationPolicy", input -> AllocationPolicy.valueOf((String) input)),
                Map.entry("allocationDisableReason", input -> AllocationDisableReason.valueOf((String) input)),
                Map.entry("regionStatus", input -> RegionStatus.valueOf((String) input)),

                Map.entry("creditSources", input ->
                        ((List<String>)input)
                                .stream()
                                .map(com.credify.investorPreAllocation.common.enums.CreditSource::valueOf)
                                .collect(Collectors.toSet()))
        );

        replaceValues
                .getNewValues()
                .forEach((key, value) -> {

                    String fieldName = CaseUtils.toCamelCase(key, false, '_');

                    try {
                        Field field = customPreAllocationConfig
                                .getClass()
                                .getSuperclass()
                                .getDeclaredField(fieldName);
                        field.setAccessible(true);

                        field.set(customPreAllocationConfig, Optional
                                .ofNullable(parsers.get(fieldName))
                                .map(mapperFunction -> mapperFunction.apply(value))
                                .orElse(value));

                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });

        return customPreAllocationConfig;

    }

}