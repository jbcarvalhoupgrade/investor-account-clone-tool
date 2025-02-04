package org.com.credify.investorMgtSrvc.tool.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.com.credify.investorMgtSrvc.tool.domain.filereplace.FileReplaceInvestmentAccountValues;
import org.com.credify.investorMgtSrvc.tool.domain.cloneable.CloneableIdBased;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class FileInvestmentAccountProcessor<T extends CloneableIdBased> extends FileProcessor <T, FileReplaceInvestmentAccountValues> {
    protected List<T> replaceValues(String contentFile, String replaceValuesFile, ObjectMapper objectMapper) throws JsonProcessingException {
        List<T> inputEntities = parseInputData(contentFile, objectMapper);

        List<FileReplaceInvestmentAccountValues> replaceValues = parseReplaceValues(replaceValuesFile,objectMapper);

        return inputEntities
                .stream()
                .map(inputEntity -> {

                    FileReplaceInvestmentAccountValues newValues = replaceValues
                            .stream()
                            .filter(toReplace -> toReplace.getId().equals(inputEntity.getId()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Missing replace entry for investmentAccount %s. You need to send at least the new id.".formatted(inputEntity.getId())));
                    return clone(inputEntity, newValues);
                }).toList();

    }

    @Override
    public List<FileReplaceInvestmentAccountValues> parseReplaceValues(String replaceValuesFile, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(replaceValuesFile,
                    TypeFactory
                            .defaultInstance().constructCollectionType(List.class,
                                    FileReplaceInvestmentAccountValues.class));
        } catch (JsonProcessingException e) {
            log.error(String.format("Fail parsing input file type: %s.", getProcessFileType()), e);
            throw new RuntimeException(e);
        }
    }
}
