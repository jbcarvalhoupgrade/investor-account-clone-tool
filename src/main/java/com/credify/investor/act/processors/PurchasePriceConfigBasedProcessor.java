package com.credify.investor.act.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.credify.investor.act.domain.filereplace.FileReplaceInvestmentAccountBasedValues;
import com.credify.investor.act.domain.cloneable.CustomPurchasePriceConfig;
import com.credify.investor.act.domain.FileType;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PurchasePriceConfigBasedProcessor extends FileInvestmentAccountBasedProcessor<CustomPurchasePriceConfig> {

    @Override
    public CustomPurchasePriceConfig clone(CustomPurchasePriceConfig input, FileReplaceInvestmentAccountBasedValues replaceValues) {
        return CustomPurchasePriceConfig.clone(input, replaceValues);
    }

    @Override
    public List<CustomPurchasePriceConfig> parseInputData(String contentFile, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(contentFile,
                    TypeFactory
                            .defaultInstance().constructCollectionType(List.class,
                                    CustomPurchasePriceConfig.class));
        } catch (JsonProcessingException e) {
            log.error(String.format("Fail parsing input file type: %s.", getProcessFileType()), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileType getProcessFileType() {
        return FileType.PURCHASE_PRICE_CONFIG;
    }

    @Override
    public Map<String, String> getFieldTypesMap() {
        return Map.ofEntries(
                Map.entry("id", "numeric"),
                Map.entry("accountConfig", "numeric"),
                Map.entry("programDefinitionFeature", "text"),
                Map.entry("principalType", "text"),
                Map.entry("interestType", "text"),
                Map.entry("isAprIncludeSofr", "boolean"),
                Map.entry("aprRate", "numeric"),
                Map.entry("actorId", "text")
        );
    }
}