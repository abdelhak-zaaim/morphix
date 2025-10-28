package io.zaaim.morphix.core.morphology;

import safar.basic.morphology.stemmer.impl.ISRIStemmer;

public class SafarMorphologyService implements MorphologyService {
    @Override
    public String extractRoot(String word) {
        return "";
    }

    @Override
    public String extractStem(String word) {
        ISRIStemmer stemmer = new ISRIStemmer();
        String stem = stemmer.stem(word);
        return "";
    }

    @Override
    public boolean supportsDiacritics() {
        return MorphologyService.super.supportsDiacritics();
    }
}
