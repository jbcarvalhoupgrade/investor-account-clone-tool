package com.credify.investor.act;

import com.credify.investor.act.processors.PreAllocationConfigProcessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.credify.investor.act.domain.FileSet;
import com.credify.investor.act.domain.FileType;
import com.credify.investor.act.processors.AccountConfigProcessor;
import com.credify.investor.act.processors.AllocationConfigProcessor;
import com.credify.investor.act.processors.BankAccountProcessor;
import com.credify.investor.act.processors.CashSweepConfigProcessor;
import com.credify.investor.act.processors.InvestmentAccountProcessor;
import com.credify.investor.act.processors.MemberizationRequirementsProcessor;
import com.credify.investor.act.processors.PurchasePriceConfigProcessor;
import com.credify.investor.act.serializer.OffsetDateTimeDeserializer;
import com.credify.investor.act.serializer.OffsetDateTimeSerializer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    private static final Map<FileType, FileSet> FILES_TO_PROCESS
            = Map.of(
                    FileType.INVESTMENT_ACCOUNT, FileSet.builder().dataFile("investmentAccount.json").replaceFile("investmentAccountReplaceValues.json").build(),
                    FileType.BANK_ACCOUNT, FileSet.builder().dataFile("bankAccount.json").replaceFile("bankAccountReplaceValues.json").build(),
                    FileType.MEMBERIZATION_REQUIREMENTS, FileSet.builder().dataFile("memberizationRequirements.json").replaceFile("memberizationRequirementsReplaceValues.json").build(),
                    FileType.CASH_SWEEP_CONFIG, FileSet.builder().dataFile("cashSweepConfig.json").replaceFile("cashSweepConfigReplaceValues.json").build(),
                    FileType.ALLOCATION_CONFIG, FileSet.builder().dataFile("allocationConfig.json").replaceFile("allocationConfigReplaceValues.json").build(),
                    FileType.ACCOUNT_CONFIG, FileSet.builder().dataFile("accountConfig.json").replaceFile("accountConfigReplaceValues.json").build(),
                    FileType.PURCHASE_PRICE_CONFIG, FileSet.builder().dataFile("purchasePriceConfig.json").replaceFile("purchasePriceConfigReplaceValues.json").build(),
                    FileType.PRE_ALLOCATION_CONFIG, FileSet.builder().dataFile("preAllocationConfig.json").replaceFile("preAllocationConfigReplaceValues.json").build()
            );

    public static void main(String[] args) {
            InvestmentAccountProcessor investmentAccountProcessor = new InvestmentAccountProcessor();
            BankAccountProcessor bankAccountProcessor = new BankAccountProcessor();
            MemberizationRequirementsProcessor memberizationRequirementsProcessor = new MemberizationRequirementsProcessor();
            CashSweepConfigProcessor cashSweepConfigProcessor = new CashSweepConfigProcessor();
            AllocationConfigProcessor allocationConfigProcessor = new AllocationConfigProcessor();
            AccountConfigProcessor accountConfigProcessor = new AccountConfigProcessor();
            PurchasePriceConfigProcessor purchasePriceConfigProcessor = new PurchasePriceConfigProcessor();
            PreAllocationConfigProcessor preAllocationConfigProcessor = new PreAllocationConfigProcessor();

            validateParameters(args);

            String rootPathFiles = args[0];

            validatePath(rootPathFiles);
            createOutputIfNotExists(rootPathFiles);

            ObjectMapper objectMapper = JsonMapper
                    .builder()
                    .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                    .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
                    .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
                    .build();

            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer());
            javaTimeModule.addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());
            objectMapper.registerModule(javaTimeModule);

            FILES_TO_PROCESS
                    .forEach((key, fileSet) -> {
                        try {
                            // Read the JSON file
                            String contentFile = Files.readString(Paths.get("%s/%s".formatted(rootPathFiles, fileSet.getDataFile())));
                            String replaceValuesFile = Files.readString(Paths.get("%s/%s".formatted(rootPathFiles, fileSet.getReplaceFile())));

                            switch (key) {
                                case INVESTMENT_ACCOUNT -> investmentAccountProcessor.process(contentFile, replaceValuesFile, objectMapper, rootPathFiles);
                                case BANK_ACCOUNT -> bankAccountProcessor.process(contentFile, replaceValuesFile, objectMapper, rootPathFiles);
                                case MEMBERIZATION_REQUIREMENTS -> memberizationRequirementsProcessor.process(contentFile, replaceValuesFile, objectMapper, rootPathFiles);
                                case CASH_SWEEP_CONFIG -> cashSweepConfigProcessor.process(contentFile, replaceValuesFile, objectMapper, rootPathFiles);
                                case ALLOCATION_CONFIG -> allocationConfigProcessor.process(contentFile, replaceValuesFile, objectMapper, rootPathFiles);
                                case ACCOUNT_CONFIG -> accountConfigProcessor.process(contentFile, replaceValuesFile, objectMapper, rootPathFiles);
                                case PURCHASE_PRICE_CONFIG -> purchasePriceConfigProcessor.process(contentFile, replaceValuesFile, objectMapper, rootPathFiles);
                                case PRE_ALLOCATION_CONFIG -> preAllocationConfigProcessor.process(contentFile, replaceValuesFile, objectMapper, rootPathFiles);
                            }

                        } catch (IOException e) {
                            log.error("Fail reading file type: %s | file name: %s".formatted(key, fileSet.getDataFile()), e);
                            throw new RuntimeException(e);
                        }
                    });
        log.info("Completed! Check output path: %s/output".formatted(rootPathFiles));
    }

    private static void validatePath(String rootPathFiles) {
        File path = new File(rootPathFiles);

        if (!path.exists()) {
            throw new IllegalArgumentException("The specified path does not exist");
        }

        if (!path.isDirectory()) {
            throw new IllegalArgumentException("The specified path is not a directory");
        }

        List<String> filesError = new ArrayList<>();
        FILES_TO_PROCESS
                .entrySet()
                .stream()
                .flatMap(entry -> Stream.of(entry.getValue().getDataFile(), entry.getValue().getReplaceFile()))
                .forEach(fileNameToProcess -> {
                    if (Arrays.stream(Objects.requireNonNull(path.list())).noneMatch(fileName -> fileName.equals(fileNameToProcess))) {
                        filesError.add(fileNameToProcess);
                    }
                });

        if (!filesError.isEmpty()) {
            filesError.forEach(file -> log.error(String.format("File not found: %s.", file)));
            throw new IllegalArgumentException("Missing files.");
        }
    }

    private static void createOutputIfNotExists(String rootPathFiles) {
        File outputFolder = new File(rootPathFiles + "/output");

        if (!outputFolder.exists()) {
            boolean created = outputFolder.mkdirs();

            if (created) {
                log.info("Output folder created successfully.");
            } else {
                log.error("Failed to create output folder.");
            }
        } else {
            log.info("Output folder already exists.");
            log.info("Deleting existing files.");
            Arrays.
                    stream(Objects.requireNonNull(outputFolder.list())).
                    map(file -> new File(outputFolder, file)).
                    forEach(File::delete);
            log.info("Output folder successfully empty.");
        }
    }

    private static void validateParameters(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Please provide a path to the JSON files");
        }
    }

}