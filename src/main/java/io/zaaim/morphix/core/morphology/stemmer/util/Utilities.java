package io.zaaim.morphix.core.morphology.stemmer.util;
import io.zaaim.morphix.core.models.WordStemmerAnalysis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import io.zaaim.morphix.core.models.StemmerAnalysis;

import java.io.File;
import java.util.List;

public abstract class Utilities {
    public static void saveStemmingResultAsXML(List<WordStemmerAnalysis> stemmingResult, File outputFile, String outputLanguage) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, StandardCharsets.UTF_8))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<stemmingResults language=\"" + escapeXml(outputLanguage) + "\">\n");

            for (WordStemmerAnalysis analysis : stemmingResult) {
                writer.write("  <word>\n");
                writer.write("    <original>" + escapeXml(analysis.getWord()) + "</original>\n");
                writer.write("    <analyses>\n");

                if (analysis.getListStemmerAnalysis() != null) {
                    for (StemmerAnalysis stemmerAnalysis : analysis.getListStemmerAnalysis()) {
                        writer.write("      <analysis>" + escapeXml(stemmerAnalysis.toString()) + "</analysis>\n");
                    }
                }

                writer.write("    </analyses>\n");
                writer.write("  </word>\n");
            }

            writer.write("</stemmingResults>\n");
        } catch (IOException e) {
            throw new RuntimeException("Failed to save stemming results to XML: " + outputFile.getPath(), e);
        }
    }

    private static String escapeXml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }
}
