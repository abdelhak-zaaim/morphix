package io.zaaim.morphix.core.models;

import common.manage_errors.Errors;

import io.zaaim.morphix.core.morphology.stemmer.IStemmer;
import io.zaaim.morphix.core.morphology.stemmer.util.Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Light10Stemmer implements IStemmer {
    public final List<WordStemmerAnalysis> stem(String text) {
        List<WordStemmerAnalysis> result = new ArrayList();
        String text1 = text.replaceAll("([ًٌٍَُِّْـ])", "");
        String[] splits = text1.split("[\\s،\"'.؟*!\\(\\)\\[\\]{},\\/#$^%&-+;=_~|?]+");

        for(String item : splits) {
            String normalized1 = item.replaceAll("[^a-zA-Z0-9ءآأؤإئابةتثجحخدذرزسشصضطظعغفقكلمنهويى]", "");
            String normalized2 = normalized1.replaceAll("ى", "ي");
            String normalized3 = normalized2.replaceAll("[ءآأؤإئ]", "ا");
            Pattern pattern = Pattern.compile("^(وال|فال|بال|بت|يت|لت|مت|تت|وت|ست|نت|بم|لم|وم|كم|فم|ال|لل|وي|لي|سي|في|وا|فا|لا|با|)(.+?)(ات|وا|تا|ون|وه|ان|تي|ته|تم|كم|هن|هم|ها|ية|تك|نا|ين|يه|ة|ه|ي|ا|)$");
            Matcher m = pattern.matcher(normalized3);
            String stem = "";
            String type = "";
            if (m.find()) {
                stem = m.group(2);
                type = "STEM";
            }

            if (!item.trim().equals("")) {
                WordStemmerAnalysis wordStemmerAnalysis = new WordStemmerAnalysis();
                wordStemmerAnalysis.setWord(item);
                List<StemmerAnalysis> listStemmerAnalysis = new ArrayList();
                listStemmerAnalysis.add(new StemmerAnalysis(type, stem));
                wordStemmerAnalysis.setListStemmerAnalysis(listStemmerAnalysis);
                result.add(wordStemmerAnalysis);
            }
        }

        return result;
    }

    public final List<WordStemmerAnalysis> stem(File inputFile) {
        return this.stemFile(inputFile, "UTF-8");
    }

    public final List<WordStemmerAnalysis> stem(File inputFile, String inputEncoding) {
        return this.stemFile(inputFile, inputEncoding);
    }

    public final void stem(String text, File outputFile) {
        List<WordStemmerAnalysis> list = this.stem(text);
        Utilities.saveStemmingResultAsXML(list, outputFile, "en,US");
    }

    public final void stem(String text, File outputFile, String outputLanguage) {
        List<WordStemmerAnalysis> list = this.stem(text);
        Utilities.saveStemmingResultAsXML(list, outputFile, outputLanguage);
    }

    public final void stem(File inputFile, File outputFile) {
        List<WordStemmerAnalysis> list = this.stemFile(inputFile, "UTF-8");
        Utilities.saveStemmingResultAsXML(list, outputFile, "en,US");
    }

    public final void stem(File inputFile, File outputFile, String inputEncoding) {
        List<WordStemmerAnalysis> list = this.stemFile(inputFile, inputEncoding);
        Utilities.saveStemmingResultAsXML(list, outputFile, "en,US");
    }

    public final void stem(File inputFile, File outputFile, String inputEncoding, String outputLanguage) {
        List<WordStemmerAnalysis> list = this.stemFile(inputFile, inputEncoding);
        Utilities.saveStemmingResultAsXML(list, outputFile, outputLanguage);
    }

    private List<WordStemmerAnalysis> stemFile(File inputFile, String inputEncoding) {
        List<WordStemmerAnalysis> list = new ArrayList();
        StringBuilder sb = new StringBuilder();

        try {
            FileInputStream fis = new FileInputStream(inputFile);
            InputStreamReader ipsr = new InputStreamReader(fis, inputEncoding);
            BufferedReader brd = new BufferedReader(ipsr);

            String in;
            while((in = brd.readLine()) != null) {
                sb.append(in);
                sb.append("\n");
            }

            list = this.stem(sb.toString());
            brd.close();
            fis.close();
        } catch (Exception e) {
            Errors.manageError("Exception", e.getMessage());
        }

        return list;
    }
}