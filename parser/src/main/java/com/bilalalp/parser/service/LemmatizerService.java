package com.bilalalp.parser.service;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

@Service
public class LemmatizerService implements Serializable {

    public String lemmatize(final String documentText) {

        final Properties props = getProperties();
        final StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        final Annotation document = new Annotation(documentText);
        pipeline.annotate(document);
        final List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        final StringBuilder stringBuilder = new StringBuilder();
        for (final CoreMap sentence : sentences) {
            for (final CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                stringBuilder.append(token.get(CoreAnnotations.LemmaAnnotation.class).concat(" "));
            }
        }
        return stringBuilder.toString().trim();
    }

    private Properties getProperties() {
        final Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        return props;
    }

}
