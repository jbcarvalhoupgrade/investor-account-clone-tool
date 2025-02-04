package com.credify.investor.act.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileSet {
    private String dataFile;
    private String replaceFile;
}
