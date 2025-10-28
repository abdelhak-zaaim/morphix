package io.zaaim.morphix.core.morphology.stemmer;

import io.zaaim.morphix.core.models.WordStemmerAnalysis;

import java.io.File;
import java.util.List;

public interface IStemmer {
    List<WordStemmerAnalysis> stem(String var1);

    List<WordStemmerAnalysis> stem(File var1);

    List<WordStemmerAnalysis> stem(File var1, String var2);

    void stem(String var1, File var2);

    void stem(String var1, File var2, String var3);

    void stem(File var1, File var2);

    void stem(File var1, File var2, String var3);

    void stem(File var1, File var2, String var3, String var4);
}

