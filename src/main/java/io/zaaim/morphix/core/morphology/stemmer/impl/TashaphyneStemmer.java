package io.zaaim.morphix.core.morphology.stemmer.impl;

import common.manage_errors.Errors;
import safar.basic.morphology.stemmer.interfaces.IStemmer;
import safar.basic.morphology.stemmer.model.StemmerAnalysis;
import safar.basic.morphology.stemmer.model.WordStemmerAnalysis;
import safar.basic.morphology.stemmer.python_src.FileLoader;
import safar.basic.morphology.stemmer.util.Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TashaphyneStemmer implements IStemmer {
    public final List<WordStemmerAnalysis> stem(String text) {
        List<WordStemmerAnalysis> list = new ArrayList();
        String[] tokens = text.split("\\s+");
        PythonInterpreter.initialize(System.getProperties(), System.getProperties(), new String[0]);
        PythonInterpreter interp = new PythonInterpreter();
        interp.execfile(FileLoader.class.getResourceAsStream("tashaphyne/stemming.py"));
        PyInstance pyInstance = (PyInstance)interp.eval("ArabicLightStemmer()");

        for(int k = 0; k < tokens.length; ++k) {
            StringBuilder unicodeWord = new StringBuilder();

            for(int i = 0; i < tokens[k].length(); ++i) {
                if (tokens[k].charAt(i) < 16) {
                    unicodeWord.append("\\u000").append(Integer.toHexString(tokens[k].charAt(i)));
                } else if (tokens[k].charAt(i) < 256) {
                    unicodeWord.append("\\u00").append(Integer.toHexString(tokens[k].charAt(i)));
                } else if (tokens[k].charAt(i) < 4096) {
                    unicodeWord.append("\\u0").append(Integer.toHexString(tokens[k].charAt(i)));
                } else {
                    unicodeWord.append("\\u").append(Integer.toHexString(tokens[k].charAt(i)));
                }
            }

            interp.exec("word = u'" + unicodeWord + "'");
            pyInstance.invoke("lightStem", interp.get("word"));
            String stem = pyInstance.invoke("get_root").toString();
            WordStemmerAnalysis wordStemmerAnalysis = new WordStemmerAnalysis();
            List<StemmerAnalysis> listStemmerAnalysis = new ArrayList();
            StemmerAnalysis stemmerAnalysis = new StemmerAnalysis();
            wordStemmerAnalysis.setWord(tokens[k]);
            stemmerAnalysis.setMorpheme(stem);
            stemmerAnalysis.setType("STEM");
            listStemmerAnalysis.add(stemmerAnalysis);
            wordStemmerAnalysis.setListStemmerAnalysis(listStemmerAnalysis);
            list.add(wordStemmerAnalysis);
        }

        return list;
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
                sb.append(" ");
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
