package org.com.credify.investorMgtSrvc.tool.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.com.credify.investorMgtSrvc.tool.domain.FileType;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class FileProcessor<T, S> {
    protected abstract List<T> replaceValues(String contentFile, String replaceValuesFile, ObjectMapper objectMapper) throws JsonProcessingException;

    public void process(String contentFile, String replaceValuesFile, ObjectMapper objectMapper, String pathToDeliverFiles) {
        try {
            List<T> outputEntities = replaceValues(contentFile, replaceValuesFile, objectMapper);
            generatePages(outputEntities, pathToDeliverFiles);
            generateLiquibaseScripts(outputEntities, pathToDeliverFiles);
        } catch (JsonProcessingException e) {
            log.error("Fail parsing replace values input file type: %s.".formatted(getProcessFileType()), e);
            throw new RuntimeException(e);
        }
    }

    private void generateLiquibaseScripts(List<T> inputValues, String pathToDeliverFiles) {
        String liquibaseContent = parseLiquibaseTemplate(inputValues);

        Path path = Paths.get("%s/output/%s_LiquibaseScript.xml".formatted(pathToDeliverFiles, getProcessFileType()));
        try {
            Files.write(path, liquibaseContent.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generatePages(List<T> inputValues, String pathToDeliverFiles) {
        String htmlContent = parsePageTemplate(inputValues);

        Path path = Paths.get("%s/output/%s_CreateData.html".formatted(pathToDeliverFiles, getProcessFileType()));
        try {
            Files.write(path, htmlContent.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String parsePageTemplate(List<T> inputValues){
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setSuffix(".html");

        Context context = new Context();
        Map<String, Object> templateVariables = Map.of("inputValues", inputValues, "fieldTypes", getFieldTypesMap());

        context.setVariables(templateVariables);

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process(getPageTemplateName(), context);
    }

    private String parseLiquibaseTemplate(List<T> inputValues){
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.XML);
        templateResolver.setSuffix(".xml");

        Context context = new Context();
        Map<String, Object> templateVariables = Map.of("inputValues", inputValues, "fieldTypes", getFieldTypesMap());

        context.setVariables(templateVariables);

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process(getLiquibaseTemplateName(), context);
    }

    private String getPageTemplateName() {
        return "templates/%s_TableTemplate".formatted(getProcessFileType());
    }

    private String getLiquibaseTemplateName() {
        return "templates/%s_LiquibaseTemplate".formatted(getProcessFileType());
    }

    abstract FileType getProcessFileType();

    abstract Map<String, String> getFieldTypesMap();

    abstract T clone(T input, S replaceValues);

    abstract List<T> parseInputData(String contentFile, ObjectMapper objectMapper);

    abstract List<S> parseReplaceValues(String replaceValuesFile, ObjectMapper objectMapper);
}
