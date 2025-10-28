package io.zaaim.morphix.core.models;

import java.util.List;

public class WordStemmerAnalysis {
    private String word;
    private List<StemmerAnalysis> listStemmerAnalysis;

    public final String getWord() {
        return this.word;
    }

    public final void setWord(String w) {
        this.word = w;
    }

    public final List<StemmerAnalysis> getListStemmerAnalysis() {
        return this.listStemmerAnalysis;
    }

    public final void setListStemmerAnalysis(List<StemmerAnalysis> stemmerAnalysis) {
        this.listStemmerAnalysis = stemmerAnalysis;
    }

    public final String toString() {
        return "Word = " + this.word + ", Stemming_results = " + this.listStemmerAnalysis;
    }
}
