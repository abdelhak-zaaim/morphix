package io.zaaim.morphix.api;

import io.zaaim.morphix.core.Vectorizer;
import safar.basic.morphology.stemmer.impl.ISRIStemmer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextVectorizer implements Vectorizer<String> {
    @Override
    public Map<String, Map<String, Float>> vectorize(String str) {
        ISRIStemmer  stemmer = new ISRIStemmer();
        Map<String, Map<String, Float>> map = new HashMap<>();
        stemmer.stem(str);
    }
}
