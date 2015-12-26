package com.bilalalp.parser.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Setter
@Getter
public class ParserInitialRepository {

    private List<String> stopWordList;
}