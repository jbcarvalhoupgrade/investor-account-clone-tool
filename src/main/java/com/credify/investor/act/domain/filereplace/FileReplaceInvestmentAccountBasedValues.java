package com.credify.investor.act.domain.filereplace;

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
public class FileReplaceInvestmentAccountBasedValues extends FileReplace {
    private Long investmentAccountId;
}
