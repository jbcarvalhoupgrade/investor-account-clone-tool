package org.com.credify.investorMgtSrvc.tool.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.com.credify.investorMgtSrvc.tool.domain.filereplace.FileReplaceInvestmentAccountBasedValues;
import org.com.credify.investorMgtSrvc.tool.domain.cloneable.CloneableInvestmentAccountBased;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class FileInvestmentAccountBasedProcessor<T extends CloneableInvestmentAccountBased> extends FileProcessor <T, FileReplaceInvestmentAccountBasedValues> {
    protected List<T> replaceValues(String contentFile, String replaceValuesFile, ObjectMapper objectMapper) throws JsonProcessingException {
        List<T> inputEntities = parseInputData(contentFile, objectMapper);

        List<FileReplaceInvestmentAccountBasedValues> replaceValues = parseReplaceValues(replaceValuesFile,objectMapper);

        return inputEntities
                .stream()
                .map(inputEntity -> {

                    FileReplaceInvestmentAccountBasedValues newValues = replaceValues
                            .stream()
                            .filter(toReplace -> toReplace.getInvestmentAccountId().equals(inputEntity.getInvestmentAccountId()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Missing replace entry for investmentAccount %s. You need to send at least the new id.".formatted(inputEntity.getInvestmentAccountId())));
                    return clone(inputEntity, newValues);
                }).toList();

    }

    @Override
    public List<FileReplaceInvestmentAccountBasedValues> parseReplaceValues(String replaceValuesFile, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(replaceValuesFile,
                    TypeFactory
                            .defaultInstance().constructCollectionType(List.class,
                                    FileReplaceInvestmentAccountBasedValues.class));
        } catch (JsonProcessingException e) {
            log.error(String.format("Fail parsing input file type: %s.", getProcessFileType()), e);
            throw new RuntimeException(e);
        }
    }
}
