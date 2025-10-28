package io.zaaim.morphix.core.morphology.stemmer.impl;

import io.zaaim.morphix.core.morphology.stemmer.ISRIStemmer;
import io.zaaim.morphix.core.morphology.stemmer.IStemmer;
import io.zaaim.morphix.core.models.WordStemmerAnalysis;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ArabicISRIStemmer implements IStemmer {

    private final ISRIStemmer stemmer;

    public ArabicISRIStemmer() {
        this.stemmer = new ISRIStemmer();
    }

    @Override
    public List<WordStemmerAnalysis> stem(String word) {
        List<WordStemmerAnalysis> results = new ArrayList<>();
        String root = stemmer.stem(word);
        WordStemmerAnalysis analysis = new WordStemmerAnalysis();
        analysis.setWord(word);
        analysis.setRoot(root);
        results.add(analysis);
        return results;
    }

    @Override
    public List<WordStemmerAnalysis> stem(File inputFile) {
        return stem(inputFile, StandardCharsets.UTF_8.name());
    }

    @Override
    public List<WordStemmerAnalysis> stem(File inputFile, String encoding) {
        List<WordStemmerAnalysis> results = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(inputFile.toPath(),
                    encoding != null ? java.nio.charset.Charset.forName(encoding) : StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    if (!word.trim().isEmpty()) {
                        results.addAll(stem(word.trim()));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + inputFile.getAbsolutePath(), e);
        }
        return results;
    }

    @Override
    public void stem(String word, File outputFile) {
        stem(word, outputFile, StandardCharsets.UTF_8.name());
    }

    @Override
    public void stem(String word, File outputFile, String encoding) {
        List<WordStemmerAnalysis> results = stem(word);
        writeResults(results, outputFile, encoding);
    }

    @Override
    public void stem(File inputFile, File outputFile) {
        stem(inputFile, outputFile, StandardCharsets.UTF_8.name(), StandardCharsets.UTF_8.name());
    }

    @Override
    public void stem(File inputFile, File outputFile, String outputEncoding) {
        stem(inputFile, outputFile, StandardCharsets.UTF_8.name(), outputEncoding);
    }

    @Override
    public void stem(File inputFile, File outputFile, String inputEncoding, String outputEncoding) {
        List<WordStemmerAnalysis> results = stem(inputFile, inputEncoding);
        writeResults(results, outputFile, outputEncoding);
    }

    private void writeResults(List<WordStemmerAnalysis> results, File outputFile, String encoding) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFile),
                        encoding != null ? encoding : StandardCharsets.UTF_8.name()))) {
            for (WordStemmerAnalysis analysis : results) {
                writer.write(analysis.getWord() + " -> " + analysis.getRoot());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file: " + outputFile.getAbsolutePath(), e);
        }
    }
}
