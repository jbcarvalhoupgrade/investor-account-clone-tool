package com.credify.investor.act.processors;

import com.credify.investor.act.domain.FileType;
import com.credify.investor.act.domain.cloneable.CustomPreAllocationConfig;
import com.credify.investor.act.domain.filereplace.FileReplaceInvestmentAccountBasedValues;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreAllocationConfigProcessor extends FileInvestmentAccountBasedProcessor<CustomPreAllocationConfig> {

    @Override
    public CustomPreAllocationConfig clone(CustomPreAllocationConfig input, FileReplaceInvestmentAccountBasedValues replaceValues) {
        return CustomPreAllocationConfig.clone(input, replaceValues);
    }

    @Override
    public List<CustomPreAllocationConfig> parseInputData(String contentFile, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(contentFile,
                    TypeFactory
                            .defaultInstance().constructCollectionType(List.class,
                                    CustomPreAllocationConfig.class));
        } catch (JsonProcessingException e) {
            log.error(String.format("Fail parsing input file type: %s.", getProcessFileType()), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileType getProcessFileType() {
        return FileType.PRE_ALLOCATION_CONFIG;
    }

    @Override
    public Map<String, String> getFieldTypesMap() {
        return Map.ofEntries(
                Map.entry("id", "numeric"),
                Map.entry("investmentAccountId", "numeric"),
                Map.entry("institutionId", "numeric"),
                Map.entry("issuingBankId", "numeric"),
                Map.entry("priority", "numeric"),
                Map.entry("weight", "numeric"),
                Map.entry("projectedAllocationEnableDate", "date"),
                Map.entry("isAllocationEnabled", "boolean"),
                Map.entry("isCompetitionPoolEnabled", "boolean"),
                Map.entry("isKeepCurrentAllocationEnabled", "boolean"),
                Map.entry("eligibleForPreAllocationOnReadyForIssueOrOpen", "boolean"),
                Map.entry("passApplicantPersonalInformationEnabled", "boolean"),
                Map.entry("assetType", "text"),
                Map.entry("productType", "text"),
                Map.entry("productSubType", "text"),
                Map.entry("investmentAccountType", "text"),
                Map.entry("allocationPolicy", "text"),
                Map.entry("allocationDisableReason", "text"),
                Map.entry("regionStatus", "text"),
                Map.entry("investmentAccountExtIntegrationName", "text"),
                Map.entry("investmentAccountExtAccountNumber", "text"),
                Map.entry("allocationDisableNotes", "text"),
                Map.entry("secondBrand", "text"),
                Map.entry("creditSources", "text")
        );
    }
}