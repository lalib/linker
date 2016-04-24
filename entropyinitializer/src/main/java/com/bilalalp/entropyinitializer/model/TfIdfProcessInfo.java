package com.bilalalp.entropyinitializer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "tfIdfProcessInfo")
public class TfIdfProcessInfo {

    @Id
    private String id;

    private Long patentId;

    private Long wordId;

    private Double tfIdfValue;
}