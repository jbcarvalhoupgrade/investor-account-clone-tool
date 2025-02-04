package org.com.credify.investorMgtSrvc.tool.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.com.credify.investorMgtSrvc.tool.domain.filereplace.FileReplaceInvestmentAccountValues;
import org.com.credify.investorMgtSrvc.tool.domain.cloneable.CustomInvestmentAccount;
import org.com.credify.investorMgtSrvc.tool.domain.FileType;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvestmentAccountProcessor extends FileInvestmentAccountProcessor<CustomInvestmentAccount> {

    @Override
    public CustomInvestmentAccount clone(CustomInvestmentAccount input, FileReplaceInvestmentAccountValues replaceValues) {
        return CustomInvestmentAccount.clone(input, replaceValues);
    }

    @Override
    public List<CustomInvestmentAccount> parseInputData(String contentFile, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(contentFile,
                    TypeFactory
                            .defaultInstance().constructCollectionType(List.class,
                                    CustomInvestmentAccount.class));
        } catch (JsonProcessingException e) {
            log.error(String.format("Fail parsing input file type: %s.", getProcessFileType()), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileType getProcessFileType() {
        return FileType.INVESTMENT_ACCOUNT;
    }

    @Override
    public Map<String, String> getFieldTypesMap() {
        return Map.ofEntries(
                Map.entry("id", "numeric"),
                Map.entry("institutionId", "numeric"),
                Map.entry("name", "text"),
                Map.entry("shortName", "text"),
                Map.entry("status", "text"),
                Map.entry("type", "text"),
                Map.entry("productType", "text"),
                Map.entry("productSubType", "text"),
                Map.entry("isPassthrough", "boolean"),
                Map.entry("isWarehouse", "boolean"),
                Map.entry("isWholeloan", "boolean"),
                Map.entry("isSendingWelcomeKit", "boolean"),
                Map.entry("isAssetRestructuring", "boolean"),
                Map.entry("isSecuritization", "boolean"),
                Map.entry("financialInstitutionType", "text"),
                Map.entry("buyerSubType", "text"),
                Map.entry("allocationPolicy", "text"),
                Map.entry("country", "text"),
                Map.entry("investorAssetClassName", "text"),
                Map.entry("actorId", "text"),
                Map.entry("regionStatus", "text"),
                Map.entry("extAccountNumber", "text"),
                Map.entry("extIntegrationName", "text"),
                Map.entry("allocationPercentage", "numeric"),
                Map.entry("collateralRequiredPercentage", "numeric")
        );
    }
}
