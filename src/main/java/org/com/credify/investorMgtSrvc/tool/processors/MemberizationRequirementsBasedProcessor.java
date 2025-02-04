package org.com.credify.investorMgtSrvc.tool.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.com.credify.investorMgtSrvc.tool.domain.filereplace.FileReplaceInvestmentAccountBasedValues;
import org.com.credify.investorMgtSrvc.tool.domain.cloneable.CustomMemberizationRequirements;
import org.com.credify.investorMgtSrvc.tool.domain.FileType;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberizationRequirementsBasedProcessor extends FileInvestmentAccountBasedProcessor<CustomMemberizationRequirements> {

    @Override
    public CustomMemberizationRequirements clone(CustomMemberizationRequirements input, FileReplaceInvestmentAccountBasedValues replaceValues) {
        return CustomMemberizationRequirements.clone(input, replaceValues);
    }

    @Override
    public List<CustomMemberizationRequirements> parseInputData(String contentFile, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(contentFile,
                    TypeFactory
                            .defaultInstance().constructCollectionType(List.class,
                                    CustomMemberizationRequirements.class));
        } catch (JsonProcessingException e) {
            log.error(String.format("Fail parsing input file type: %s.", getProcessFileType()), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileType getProcessFileType() {
        return FileType.MEMBERIZATION_REQUIREMENTS;
    }

    @Override
    public Map<String, String> getFieldTypesMap() {
        return Map.ofEntries(
                Map.entry("id", "numeric"),
                Map.entry("investmentAccountId", "numeric"),
                Map.entry("membershipRequired", "boolean"),
                Map.entry("membershipTemplateType", "text"),
                Map.entry("membershipFormFields", "text"),
                Map.entry("formType", "text")
        );
    }
}
