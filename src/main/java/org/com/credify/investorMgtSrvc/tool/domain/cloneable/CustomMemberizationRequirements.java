package org.com.credify.investorMgtSrvc.tool.domain.cloneable;

import com.credify.dgs.enums.TemplateType;
import com.credify.investorMgtSrvc.data.domain.InvestmentAccount;
import com.credify.investorMgtSrvc.data.domain.MemberizationRequirements;
import com.credify.investorMgtSrvc.data.domain.MembershipFormField;
import com.credify.investorMgtSrvc.enums.MembershipFormFieldName;
import com.credify.investorMgtSrvc.model.MembershipFormType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.text.CaseUtils;
import org.com.credify.investorMgtSrvc.tool.domain.filereplace.FileReplaceInvestmentAccountBasedValues;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomMemberizationRequirements extends MemberizationRequirements implements CloneableInvestmentAccountBased {
    private Long investmentAccountId;
    private String membershipFormFieldsAsString;

    public static CustomMemberizationRequirements clone(CustomMemberizationRequirements memberizationRequirements, FileReplaceInvestmentAccountBasedValues replaceValues) {
        CustomMemberizationRequirements customMemberizationRequirements = new CustomMemberizationRequirements();

        customMemberizationRequirements.setInvestmentAccount(memberizationRequirements.getInvestmentAccount());
        customMemberizationRequirements.setMembershipRequired(memberizationRequirements.getMembershipRequired());
        customMemberizationRequirements.setMembershipTemplateType(memberizationRequirements.getMembershipTemplateType());
        customMemberizationRequirements.setFormType(memberizationRequirements.getFormType());
        customMemberizationRequirements.setMembershipFormFields(memberizationRequirements.getMembershipFormFields());
        customMemberizationRequirements.setActorId("liquibase");

        Map<String, Function<Object, Object>> parsers = Map.ofEntries(
                Map.entry("id", input -> Integer.toUnsignedLong((Integer) input)),
                Map.entry("membershipRequired", input -> Boolean.valueOf((String) input)),
                Map.entry("membershipTemplateType", input -> TemplateType.valueOf((String) input)),
                Map.entry("formType", input -> MembershipFormType.valueOf((String) input)),
                Map.entry("membershipFormFields", input ->((List)input)
                                .stream()
                                .flatMap(map -> ((LinkedHashMap) map).values().stream())
                                .map(in -> MembershipFormFieldName.valueOf((String)in))
                                .map(in -> MembershipFormField.builder().name((MembershipFormFieldName)in).build())
                                .toList()),
                Map.entry("investmentAccount",
                        input -> InvestmentAccount
                                .builder()
                                .id(Integer.toUnsignedLong((Integer) input))
                                .build())
        );

        replaceValues
                .getNewValues()
                .forEach((key, value) -> {

                    String fieldName = CaseUtils.toCamelCase(key, false, '_');

                    try {
                        Field field;

                        if (!fieldName.equals("investmentAccountId")) {
                            field = customMemberizationRequirements
                                    .getClass()
                                    .getSuperclass()
                                    .getDeclaredField(fieldName);
                        } else {
                            field = CustomMemberizationRequirements.class.getDeclaredField(fieldName);
                        }

                        field.setAccessible(true);

                        field.set(customMemberizationRequirements, Optional
                                .ofNullable(parsers.get(fieldName))
                                .map(mapperFunction -> mapperFunction.apply(value))
                                .orElse(value));

                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });

        return customMemberizationRequirements;

    }

    public String getMembershipFormFieldsAsString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(getMembershipFormFields());
    }
}
