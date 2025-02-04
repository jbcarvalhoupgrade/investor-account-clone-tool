package com.credify.investor.act.domain.cloneable;

import com.credify.enums.investor.InvestmentAccountStatus;
import com.credify.enums.product.LoanProductType;
import com.credify.investorMgtSrvc.data.domain.Institution;
import com.credify.investorMgtSrvc.data.domain.InvestmentAccount;
import com.credify.investorMgtSrvc.data.domain.enums.InvestorAssetClassName;
import com.credify.investorMgtSrvc.enums.AllocationPolicy;
import com.credify.investorMgtSrvc.enums.BuyerSubType;
import com.credify.investorMgtSrvc.enums.FinancialInstitutionType;
import com.credify.investorMgtSrvc.enums.InvestmentAccountType;
import com.credify.investorMgtSrvc.enums.LoanProductSubType;
import com.credify.investorMgtSrvc.enums.RegionStatus;

import org.apache.commons.text.CaseUtils;
import com.credify.investor.act.domain.filereplace.FileReplaceInvestmentAccountValues;

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
public class CustomInvestmentAccount extends InvestmentAccount implements CloneableIdBased {
    private Long institutionId;

    public static CustomInvestmentAccount clone(CustomInvestmentAccount investmentAccount, FileReplaceInvestmentAccountValues replaceValues) {
        CustomInvestmentAccount customInvestmentAccount = new CustomInvestmentAccount();
        customInvestmentAccount.setId(investmentAccount.getId());
        customInvestmentAccount.setInstitution(Institution.builder().id(investmentAccount.getInstitutionId()).build());
        customInvestmentAccount.setName(investmentAccount.getName());
        customInvestmentAccount.setShortName(investmentAccount.getShortName());
        customInvestmentAccount.setStatus(investmentAccount.getStatus());
        customInvestmentAccount.setType(investmentAccount.getType());
        customInvestmentAccount.setProductType(investmentAccount.getProductType());
        customInvestmentAccount.setProductSubType(investmentAccount.getProductSubType());
        customInvestmentAccount.setIsPassthrough(investmentAccount.getIsPassthrough());
        customInvestmentAccount.setIsWarehouse(investmentAccount.getIsWarehouse());
        customInvestmentAccount.setIsWholeloan(investmentAccount.getIsWholeloan());
        customInvestmentAccount.setIsSecuritization(investmentAccount.getIsSecuritization());
        customInvestmentAccount.setFinancialInstitutionType(investmentAccount.getFinancialInstitutionType());
        customInvestmentAccount.setAllocationPolicy(investmentAccount.getAllocationPolicy());
        customInvestmentAccount.setBuyerSubType(investmentAccount.getBuyerSubType());
        customInvestmentAccount.setExtIntegrationName(investmentAccount.getExtIntegrationName());
        customInvestmentAccount.setExtAccountNumber(investmentAccount.getExtAccountNumber());
        customInvestmentAccount.setIsSendingWelcomeKit(investmentAccount.getIsSendingWelcomeKit());
        customInvestmentAccount.setRegionStatus(investmentAccount.getRegionStatus());
        customInvestmentAccount.setIsAssetRestructuring(investmentAccount.getIsAssetRestructuring());
        customInvestmentAccount.setCountry(investmentAccount.getCountry());
        customInvestmentAccount.setInvestorAssetClassName(investmentAccount.getInvestorAssetClassName());
        customInvestmentAccount.setAllocationPercentage(investmentAccount.getAllocationPercentage());
        customInvestmentAccount.setCollateralRequiredPercentage(investmentAccount.getCollateralRequiredPercentage());
        customInvestmentAccount.setActorId("liquibase");

        Map<String, Function<Object, Object>> parsers = Map.ofEntries(
                Map.entry("id", input -> Integer.toUnsignedLong((Integer) input)),
                Map.entry("productType", input -> LoanProductType.valueOf((String) input)),
                Map.entry("productSubType", input -> LoanProductSubType.valueOf((String) input)),
                Map.entry("investorAssetClassName", input -> InvestorAssetClassName.valueOf((String) input)),
                Map.entry("status", input -> InvestmentAccountStatus.valueOf((String) input)),
                Map.entry("type", input -> InvestmentAccountType.valueOf((String) input)),
                Map.entry("financialInstitutionType", input -> FinancialInstitutionType.valueOf((String) input)),
                Map.entry("allocationPolicy", input -> AllocationPolicy.valueOf((String) input)),
                Map.entry("buyerSubType", input -> BuyerSubType.valueOf((String) input)),
                Map.entry("regionStatus", input -> RegionStatus.valueOf((String) input)),
                Map.entry("isSecuritization", input -> Boolean.valueOf((String) input)),
                Map.entry("isPassthrough", input -> Boolean.valueOf((String) input)),
                Map.entry("isWarehouse", input -> Boolean.valueOf((String) input)),
                Map.entry("isWholeloan", input -> Boolean.valueOf((String) input)),
                Map.entry("isSendingWelcomeKit", input -> Boolean.valueOf((String) input)),
                Map.entry("isAssetRestructuring", input -> Boolean.valueOf((String) input)),
                Map.entry("institution",
                        input -> Institution
                                .builder()
                                .id(Long.valueOf((String)input))
                                .build()),
                Map.entry("allocationPercentage", input -> new BigDecimal((String) input)),
                Map.entry("collateralRequiredPercentage", input -> new BigDecimal((String) input))
        );

        replaceValues
                .getNewValues()
                .forEach((key, value) -> {

                    String fieldName = CaseUtils.toCamelCase(key, false, '_');

                    try {
                        Field field;

                        if (!fieldName.equals("institutionId")) {
                            field = customInvestmentAccount
                                    .getClass()
                                    .getSuperclass()
                                    .getDeclaredField(fieldName);
                        } else {
                            field = CustomInvestmentAccount.class.getDeclaredField(fieldName);
                        }

                        field.setAccessible(true);

                        field.set(customInvestmentAccount, Optional
                                .ofNullable(parsers.get(fieldName))
                                .map(mapperFunction -> mapperFunction.apply(value))
                                .orElse(value));

                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });

        return customInvestmentAccount;
    }

}
