package org.com.credify.investorMgtSrvc.tool.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.com.credify.investorMgtSrvc.tool.domain.filereplace.FileReplaceInvestmentAccountBasedValues;
import org.com.credify.investorMgtSrvc.tool.domain.cloneable.CustomAllocationConfig;
import org.com.credify.investorMgtSrvc.tool.domain.FileType;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AllocationConfigBasedProcessor extends FileInvestmentAccountBasedProcessor<CustomAllocationConfig> {

    @Override
    public CustomAllocationConfig clone(CustomAllocationConfig input, FileReplaceInvestmentAccountBasedValues replaceValues) {
        return CustomAllocationConfig.clone(input, replaceValues);
    }

    @Override
    public List<CustomAllocationConfig> parseInputData(String contentFile, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(contentFile,
                    TypeFactory
                            .defaultInstance().constructCollectionType(List.class,
                                    CustomAllocationConfig.class));
        } catch (JsonProcessingException e) {
            log.error(String.format("Fail parsing input file type: %s.", getProcessFileType()), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FileReplaceInvestmentAccountBasedValues> parseReplaceValues(String replaceValuesFile, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(replaceValuesFile,
                    TypeFactory.defaultInstance().constructCollectionType(List.class,
                            FileReplaceInvestmentAccountBasedValues.class));
        } catch (JsonProcessingException e) {
            log.error(String.format("Fail parsing input file type: %s.", getProcessFileType()), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileType getProcessFileType() {
        return FileType.ALLOCATION_CONFIG;
    }

    @Override
    public Map<String, String> getFieldTypesMap() {
        return Map.ofEntries(
                Map.entry("id", "numeric"),
                Map.entry("investmentAccountId", "numeric"),
                Map.entry("issuingBank", "numeric"),
                Map.entry("defaultSeasonerAccount", "numeric"),
                Map.entry("priority", "numeric"),
                Map.entry("weight", "numeric"),
                Map.entry("monthlyTargetPct", "numeric"),
                Map.entry("expectedApplicationFalloutPct", "numeric"),
                Map.entry("portfolioMixTargetAmount", "numeric"),
                Map.entry("monthlyDollarCapAmount", "numeric"),
                Map.entry("monthlyTargetDollarAmount", "numeric"),
                Map.entry("allocationDisableNotes", "text"),
                Map.entry("secondBrand", "text"),
                Map.entry("assetType", "text"),
                Map.entry("allocationDisableReason", "text"),
                Map.entry("creditSources", "text"),
                Map.entry("projectedAllocationEnableDate", "date"),
                Map.entry("isAllocationEnabled", "boolean"),
                Map.entry("isCompetitionPoolEnabled", "boolean"),
                Map.entry("isBeneficiaryAllocationEnabled", "boolean"),
                Map.entry("isKeepCurrentAllocationEnabled", "boolean"),
                Map.entry("eligibleForPreAllocationOnReadyForIssueOrOpen", "boolean")
        );
    }
}
