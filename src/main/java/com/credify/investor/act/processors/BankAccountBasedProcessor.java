package com.credify.investor.act.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.credify.investor.act.domain.filereplace.FileReplaceBankAccountValues;
import com.credify.investor.act.domain.cloneable.CustomBankAccount;
import com.credify.investor.act.domain.FileType;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BankAccountBasedProcessor extends FileBankAccountProcessor<CustomBankAccount> {

    @Override
    public CustomBankAccount clone(CustomBankAccount input, FileReplaceBankAccountValues replaceValues) {
        return CustomBankAccount.clone(input, replaceValues);
    }

    @Override
    public List<CustomBankAccount> parseInputData(String contentFile, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(contentFile,
                    TypeFactory
                            .defaultInstance().constructCollectionType(List.class,
                                    CustomBankAccount.class));
        } catch (JsonProcessingException e) {
            log.error(String.format("Fail parsing input file type: %s.", getProcessFileType()), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileType getProcessFileType() {
        return FileType.BANK_ACCOUNT;
    }

    @Override
    public Map<String, String> getFieldTypesMap() {
        return Map.ofEntries(
                Map.entry("id", "numeric"),
                Map.entry("investmentAccountId", "numeric"),
                Map.entry("holderName", "text"),
                Map.entry("type", "text"),
                Map.entry("descriptor", "text")
        );
    }
}
