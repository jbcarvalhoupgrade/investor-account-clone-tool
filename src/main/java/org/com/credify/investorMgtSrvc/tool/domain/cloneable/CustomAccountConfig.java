package org.com.credify.investorMgtSrvc.tool.domain.cloneable;

import com.credify.investorMgtSrvc.data.domain.AccountConfig;
import com.credify.investorMgtSrvc.data.domain.InvestmentAccount;
import com.credify.investorMgtSrvc.data.domain.enums.CreditSource;
import com.credify.investorMgtSrvc.enums.FeeTreatment;
import com.credify.investorMgtSrvc.enums.PortfolioBackupServicerName;
import com.credify.investorMgtSrvc.enums.ServiceFeeCalculationMethodType;

import org.apache.commons.text.CaseUtils;
import org.com.credify.investorMgtSrvc.tool.domain.filereplace.FileReplaceInvestmentAccountBasedValues;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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
public class CustomAccountConfig extends AccountConfig implements CloneableInvestmentAccountBased {
    private Long investmentAccountId;
    private Long issuingBankId;
    private Long defaultSeasonerId;

    public static OffsetDateTime parseOffsetDateTime(String input) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnn");

        return Optional.ofNullable(input)
                .map(String::trim)
                .filter(value -> !value.isEmpty() && !value.equalsIgnoreCase("null"))
                .map(value -> LocalDateTime.parse(value, formatter))
                .map(value -> value.atOffset(ZoneOffset.UTC))
                .orElse(null);
    }


    public static CustomAccountConfig clone(CustomAccountConfig accountConfig, FileReplaceInvestmentAccountBasedValues replaceValues) {
        CustomAccountConfig customAccountConfig = new CustomAccountConfig();
        customAccountConfig.setInvestmentAccount(accountConfig.getInvestmentAccount());
        customAccountConfig.setCollection31to60Fee(accountConfig.getCollection31to60Fee());
        customAccountConfig.setCollection61to90Fee(accountConfig.getCollection61to90Fee());
        customAccountConfig.setCollection91to120Fee(accountConfig.getCollection91to120Fee());
        customAccountConfig.setRecoveryDebtSalesFee(accountConfig.getRecoveryDebtSalesFee());
        customAccountConfig.setRecoveryBorrowerPaymentFee(accountConfig.getRecoveryBorrowerPaymentFee());
        customAccountConfig.setRecoverySettlementsFee(accountConfig.getRecoverySettlementsFee());
        customAccountConfig.setLoanTrailingFee(accountConfig.getLoanTrailingFee());
        customAccountConfig.setSeasoningMaxAmount(accountConfig.getSeasoningMaxAmount());
        customAccountConfig.setDefaultServiceFeeRate(accountConfig.getDefaultServiceFeeRate());
        customAccountConfig.setServiceFeeEscrowRate(accountConfig.getServiceFeeEscrowRate());
        customAccountConfig.setPurchasePremium(accountConfig.getPurchasePremium());
        customAccountConfig.setDefaultPurchasePremiumRate(accountConfig.getDefaultPurchasePremiumRate());
        customAccountConfig.setDefaultMaxApr(accountConfig.getDefaultMaxApr());
        customAccountConfig.setDefaultMaxInterestRate(accountConfig.getDefaultMaxInterestRate());
        customAccountConfig.setCouponRate(accountConfig.getCouponRate());
        customAccountConfig.setAutoCollectionPre120Fee(accountConfig.getAutoCollectionPre120Fee());
        customAccountConfig.setAutoCollectionPre120FeeMax(accountConfig.getAutoCollectionPre120FeeMax());
        customAccountConfig.setAutoCollectionPost120Fee(accountConfig.getAutoCollectionPost120Fee());
        customAccountConfig.setAutoCollectionPost120FeeMax(accountConfig.getAutoCollectionPost120FeeMax());
        customAccountConfig.setAutoRecoveryBorrowerPaymentFee(accountConfig.getAutoRecoveryBorrowerPaymentFee());
        customAccountConfig.setCashReportDaily(accountConfig.getCashReportDaily());
        customAccountConfig.setCashReportWeekly(accountConfig.getCashReportWeekly());
        customAccountConfig.setCashReportMonthly(accountConfig.getCashReportMonthly());
        customAccountConfig.setCashReportQuarterly(accountConfig.getCashReportQuarterly());
        customAccountConfig.setCashReportYearly(accountConfig.getCashReportYearly());
        customAccountConfig.setLegalCostEligible(accountConfig.getLegalCostEligible());
        customAccountConfig.setDebitFeesAutomatically(accountConfig.getDebitFeesAutomatically());
        customAccountConfig.setHoldForDebtSaleClawbackPeriod(accountConfig.getHoldForDebtSaleClawbackPeriod());
        customAccountConfig.setEligibleForDebtSale(accountConfig.getEligibleForDebtSale());
        customAccountConfig.setEligibleForFraudRepurchase(accountConfig.getEligibleForFraudRepurchase());
        customAccountConfig.setEligibleForOutsource(accountConfig.getEligibleForOutsource());
        customAccountConfig.setDisableAllReports(accountConfig.getDisableAllReports());
        customAccountConfig.setCashSweepsRequireReconciliation(accountConfig.getCashSweepsRequireReconciliation());
        customAccountConfig.setIsJustInTimeFunding(accountConfig.getIsJustInTimeFunding());
        customAccountConfig.setAutoPurchaseEnabled(accountConfig.getAutoPurchaseEnabled());
        customAccountConfig.setAutoEligibleForRepossessionFees(accountConfig.getAutoEligibleForRepossessionFees());
        customAccountConfig.setAutoEligibleForAuctionFees(accountConfig.getAutoEligibleForAuctionFees());
        customAccountConfig.setSeasoningMaxDays(accountConfig.getSeasoningMaxDays());
        customAccountConfig.setPurchaseDays(accountConfig.getPurchaseDays());
        customAccountConfig.setMinimumPurchaseSeasoningDays(accountConfig.getMinimumPurchaseSeasoningDays());
        customAccountConfig.setPaymentSeasoningDays(accountConfig.getPaymentSeasoningDays());
        customAccountConfig.setDayOfFunding(accountConfig.getDayOfFunding());
        customAccountConfig.setNetSuiteSubsidiaryId(accountConfig.getNetSuiteSubsidiaryId());
        customAccountConfig.setNetSuiteClassId(accountConfig.getNetSuiteClassId());
        customAccountConfig.setDebtSaleClawbackPeriodDays(accountConfig.getDebtSaleClawbackPeriodDays());
        customAccountConfig.setChargeOffMinDays(accountConfig.getChargeOffMinDays());
        customAccountConfig.setHoldingDays(accountConfig.getHoldingDays());
        customAccountConfig.setPreviousSystemId(accountConfig.getPreviousSystemId());
        customAccountConfig.setServiceFeeCalcMethod(accountConfig.getServiceFeeCalcMethod());
        customAccountConfig.setPortfolioBackupServicerName(accountConfig.getPortfolioBackupServicerName());
        customAccountConfig.setFeeTreatment(accountConfig.getFeeTreatment());
        customAccountConfig.setAchId(accountConfig.getAchId());
        customAccountConfig.setActorId("liquibase");
        customAccountConfig.setServiceFeeEscrowStartDate(accountConfig.getServiceFeeEscrowStartDate());
        customAccountConfig.setServiceFeeEscrowEndDate(accountConfig.getServiceFeeEscrowEndDate());
        customAccountConfig.setServiceFeeLastUpdateDate(accountConfig.getServiceFeeLastUpdateDate());
        customAccountConfig.setServiceFeeRates(accountConfig.getServiceFeeRates());
        customAccountConfig.setPurchasePremiumRates(accountConfig.getPurchasePremiumRates());
        customAccountConfig.setMaxAprConfigs(accountConfig.getMaxAprConfigs());
        customAccountConfig.setPurchasePriceConfigs(accountConfig.getPurchasePriceConfigs());
        customAccountConfig.setMaxInterestRateConfigs(accountConfig.getMaxInterestRateConfigs());

        Map<String, Function<Object, Object>> parsers = Map.ofEntries(
                Map.entry("id", input -> Integer.toUnsignedLong((Integer) input)),

                Map.entry("cashReportDaily", input -> Boolean.valueOf((String) input)),
                Map.entry("cashReportWeekly", input -> Boolean.valueOf((String) input)),
                Map.entry("cashReportMonthly", input -> Boolean.valueOf((String) input)),
                Map.entry("cashReportQuarterly", input -> Boolean.valueOf((String) input)),
                Map.entry("cashReportYearly", input -> Boolean.valueOf((String) input)),
                Map.entry("legalCostEligible", input -> Boolean.valueOf((String) input)),
                Map.entry("debitFeesAutomatically", input -> Boolean.valueOf((String) input)),
                Map.entry("holdForDebtSaleClawbackPeriod", input -> Boolean.valueOf((String) input)),
                Map.entry("eligibleForDebtSale", input -> Boolean.valueOf((String) input)),
                Map.entry("eligibleForFraudRepurchase", input -> Boolean.valueOf((String) input)),
                Map.entry("eligibleForOutsource", input -> Boolean.valueOf((String) input)),
                Map.entry("disableAllReports", input -> Boolean.valueOf((String) input)),
                Map.entry("cashSweepsRequireReconciliation", input -> Boolean.valueOf((String) input)),
                Map.entry("isJustInTimeFunding", input -> Boolean.valueOf((String) input)),
                Map.entry("autoPurchaseEnabled", input -> Boolean.valueOf((String) input)),

                Map.entry("autoEligibleForRepossessionFees", input -> Boolean.valueOf((String) input)),
                Map.entry("autoEligibleForAuctionFees", input -> Boolean.valueOf((String) input)),

                Map.entry("seasoningMaxDays", input -> ((Integer) input)),
                Map.entry("purchaseDays", input -> ((Integer) input)),
                Map.entry("minimumPurchaseSeasoningDays", input -> ((Integer) input)),
                Map.entry("paymentSeasoningDays", input -> ((Integer) input)),
                Map.entry("dayOfFunding", input -> ((Integer) input)),
                Map.entry("netSuiteSubsidiaryId", input -> ((Integer) input)),
                Map.entry("netSuiteClassId", input -> ((Integer) input)),
                Map.entry("debtSaleClawbackPeriodDays", input -> ((Integer) input)),
                Map.entry("chargeOffMinDays", input -> ((Integer) input)),
                Map.entry("holdingDays", input -> ((Integer) input)),

                Map.entry("previousSystemId", input -> Integer.toUnsignedLong((Integer) input)),

                Map.entry("collection31to60Fee", input -> new BigDecimal((String) input)),
                Map.entry("collection61to90Fee", input -> new BigDecimal((String) input)),
                Map.entry("collection91to120Fee", input -> new BigDecimal((String) input)),
                Map.entry("recoveryDebtSalesFee", input -> new BigDecimal((String) input)),
                Map.entry("recoveryBorrowerPaymentFee", input -> new BigDecimal((String) input)),
                Map.entry("recoverySettlementsFee", input -> new BigDecimal((String) input)),
                Map.entry("loanTrailingFee", input -> new BigDecimal((String) input)),
                Map.entry("seasoningMaxAmount", input -> new BigDecimal((String) input)),
                Map.entry("defaultServiceFeeRate", input -> new BigDecimal((String) input)),
                Map.entry("serviceFeeEscrowRate", input -> new BigDecimal((String) input)),
                Map.entry("purchasePremium", input -> new BigDecimal((String) input)),
                Map.entry("defaultPurchasePremiumRate", input -> new BigDecimal((String) input)),
                Map.entry("defaultMaxApr", input -> new BigDecimal((String) input)),
                Map.entry("defaultMaxInterestRate", input -> new BigDecimal((String) input)),
                Map.entry("couponRate", input -> new BigDecimal((String) input)),
                Map.entry("autoCollectionPre120Fee", input -> new BigDecimal((String) input)),
                Map.entry("autoCollectionPre120FeeMax", input -> new BigDecimal((String) input)),
                Map.entry("autoCollectionPost120Fee", input -> new BigDecimal((String) input)),
                Map.entry("autoCollectionPost120FeeMax", input -> new BigDecimal((String) input)),
                Map.entry("autoRecoveryBorrowerPaymentFee", input -> new BigDecimal((String) input)),

                Map.entry("serviceFeeEscrowEndDate", input -> LocalDate.parse((String) input)),
                Map.entry("serviceFeeEscrowStartDate", input -> LocalDate.parse((String) input)),

                Map.entry("serviceFeeLastUpdateDate", input -> parseOffsetDateTime((String) input)),

                Map.entry("serviceFeeCalcMethod", input -> ServiceFeeCalculationMethodType.valueOf((String) input)),
                Map.entry("portfolioBackupServicerName", input -> PortfolioBackupServicerName.valueOf((String) input)),
                Map.entry("feeTreatment", input -> FeeTreatment.valueOf((String) input)),

                Map.entry("investmentAccount",
                        input -> InvestmentAccount
                                .builder()
                                .id(Integer.toUnsignedLong((Integer) input))
                                .build()),

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
                            field = accountConfig
                                    .getClass()
                                    .getSuperclass()
                                    .getDeclaredField(fieldName);
                        } else {
                            field = CustomAccountConfig.class.getDeclaredField(fieldName);
                        }

                        field.setAccessible(true);

                        field.set(accountConfig, Optional
                                .ofNullable(parsers.get(fieldName))
                                .map(mapperFunction -> mapperFunction.apply(value))
                                .orElse(value));

                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });

        return accountConfig;
    }

}
