package com.credify.investor.act.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.credify.investor.act.domain.filereplace.FileReplaceInvestmentAccountBasedValues;
import com.credify.investor.act.domain.cloneable.CustomCashSweepConfig;
import com.credify.investor.act.domain.FileType;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CashSweepConfigProcessor extends FileInvestmentAccountBasedProcessor<CustomCashSweepConfig> {

    @Override
    public CustomCashSweepConfig clone(CustomCashSweepConfig input, FileReplaceInvestmentAccountBasedValues replaceValues) {
        return CustomCashSweepConfig.clone(input, replaceValues);
    }

    @Override
    public List<CustomCashSweepConfig> parseInputData(String contentFile, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(contentFile,
                    TypeFactory
                            .defaultInstance().constructCollectionType(List.class,
                                    CustomCashSweepConfig.class));
        } catch (JsonProcessingException e) {
            log.error(String.format("Fail parsing input file type: %s.", getProcessFileType()), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileType getProcessFileType() {
        return FileType.CASH_SWEEP_CONFIG;
    }

    @Override
    public Map<String, String> getFieldTypesMap() {
        return Map.ofEntries(
                Map.entry("id", "numeric"),
                Map.entry("investmentAccountId", "numeric"),
                Map.entry("cashSweepsRequireReconciliation", "boolean"),
                Map.entry("requiresServicingAccountSegregation", "boolean"),
                Map.entry("automateCashSweeps", "boolean"),
                Map.entry("feeTreatment", "text"),
                Map.entry("cashSweepCadence", "text"),
                Map.entry("principalBankAccount", "text"),
                Map.entry("interestBankAccount", "text"),
                Map.entry("otherBankAccount", "text"),
                Map.entry("feesBankAccount", "text"),
                Map.entry("automateCashSweepsAfterDate", "date"),
                Map.entry("firstDistributionDate", "date"),
                Map.entry("distributionPercentageCap", "numeric")
        );
    }
}
