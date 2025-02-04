package org.com.credify.investorMgtSrvc.tool.domain.filereplace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FileReplaceBankAccountValues extends FileReplaceInvestmentAccountBasedValues{
    private String type;
}
