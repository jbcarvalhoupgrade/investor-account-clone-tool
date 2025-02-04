package org.com.credify.investorMgtSrvc.tool.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.com.credify.investorMgtSrvc.tool.domain.filereplace.FileReplaceInvestmentAccountBasedValues;
import org.com.credify.investorMgtSrvc.tool.domain.cloneable.CustomAccountConfig;
import org.com.credify.investorMgtSrvc.tool.domain.FileType;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountConfigBasedProcessor extends FileInvestmentAccountBasedProcessor<CustomAccountConfig> {

    @Override
    public CustomAccountConfig clone(CustomAccountConfig input, FileReplaceInvestmentAccountBasedValues replaceValues) {
        return CustomAccountConfig.clone(input, replaceValues);
    }

    @Override
    public List<CustomAccountConfig> parseInputData(String contentFile, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(contentFile,
                    TypeFactory
                            .defaultInstance().constructCollectionType(List.class,
                                    CustomAccountConfig.class));
        } catch (JsonProcessingException e) {
            log.error(String.format("Fail parsing input file type: %s.", getProcessFileType()), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileType getProcessFileType() {
        return FileType.ACCOUNT_CONFIG;
    }

    @Override
    public Map<String, String> getFieldTypesMap() {
        return Map.ofEntries(
                Map.entry("id", "numeric"),
                Map.entry("investmentAccountId", "numeric"),
                Map.entry("collection31to60Fee", "numeric"),
                Map.entry("collection61to90Fee", "numeric"),
                Map.entry("collection91to120Fee", "numeric"),
                Map.entry("recoveryDebtSalesFee", "numeric"),
                Map.entry("recoveryBorrowerPaymentFee", "numeric"),
                Map.entry("recoverySettlementsFee", "numeric"),
                Map.entry("loanTrailingFee", "numeric"),
                Map.entry("seasoningMaxAmount", "numeric"),
                Map.entry("defaultServiceFeeRate", "numeric"),
                Map.entry("serviceFeeEscrowRate", "numeric"),
                Map.entry("purchasePremium", "numeric"),
                Map.entry("defaultPurchasePremiumRate", "numeric"),
                Map.entry("defaultMaxApr", "numeric"),
                Map.entry("defaultMaxInterestRate", "numeric"),
                Map.entry("couponRate", "numeric"),
                Map.entry("autoCollectionPre120Fee", "numeric"),
                Map.entry("autoCollectionPre120FeeMax", "numeric"),
                Map.entry("autoCollectionPost120Fee", "numeric"),
                Map.entry("autoCollectionPost120FeeMax", "numeric"),
                Map.entry("autoRecoveryBorrowerPaymentFee", "numeric"),
                Map.entry("cashReportDaily", "boolean"),
                Map.entry("cashReportWeekly", "boolean"),
                Map.entry("cashReportMonthly", "boolean"),
                Map.entry("cashReportQuarterly", "boolean"),
                Map.entry("cashReportYearly", "boolean"),
                Map.entry("legalCostEligible", "boolean"),
                Map.entry("debitFeesAutomatically", "boolean"),
                Map.entry("holdForDebtSaleClawbackPeriod", "boolean"),
                Map.entry("eligibleForDebtSale", "boolean"),
                Map.entry("eligibleForFraudRepurchase", "boolean"),
                Map.entry("eligibleForOutsource", "boolean"),
                Map.entry("disableAllReports", "boolean"),
                Map.entry("cashSweepsRequireReconciliation", "boolean"),
                Map.entry("isJustInTimeFunding", "boolean"),
                Map.entry("autoPurchaseEnabled", "boolean"),
                Map.entry("autoEligibleForRepossessionFees", "boolean"),
                Map.entry("autoEligibleForAuctionFees", "boolean"),
                Map.entry("seasoningMaxDays", "numeric"),
                Map.entry("purchaseDays", "numeric"),
                Map.entry("minimumPurchaseSeasoningDays", "numeric"),
                Map.entry("paymentSeasoningDays", "numeric"),
                Map.entry("dayOfFunding", "numeric"),
                Map.entry("netSuiteSubsidiaryId", "numeric"),
                Map.entry("netSuiteClassId", "numeric"),
                Map.entry("debtSaleClawbackPeriodDays", "numeric"),
                Map.entry("chargeOffMinDays", "numeric"),
                Map.entry("holdingDays", "numeric"),
                Map.entry("previousSystemId", "numeric"),
                Map.entry("serviceFeeEscrowStartDate", "date"),
                Map.entry("serviceFeeEscrowEndDate", "date"),
                Map.entry("serviceFeeLastUpdateDate", "date"),
                Map.entry("serviceFeeCalcMethod", "text"),
                Map.entry("portfolioBackupServicerName", "text"),
                Map.entry("feeTreatment", "text"),
                Map.entry("achId", "text"),
                Map.entry("actorId", "text")
        );
    }
}