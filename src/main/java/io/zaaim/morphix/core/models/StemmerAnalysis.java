package io.zaaim.morphix.core.models;

public class StemmerAnalysis {
    private String type;
    private String morpheme;
    private String additionalInfo;

    public StemmerAnalysis() {
    }

    public StemmerAnalysis(String t, String m) {
        this.type = t;
        this.morpheme = m;
    }

    public final String getType() {
        return this.type;
    }

    public final void setType(String t) {
        this.type = t;
    }

    public final String getMorpheme() {
        return this.morpheme;
    }

    public final void setMorpheme(String m) {
        this.morpheme = m;
    }

    public final String getAdditionalInfo() {
        return this.additionalInfo;
    }

    public final void setAdditionalInfo(String info) {
        this.additionalInfo = info;
    }

    public final String toString() {
        return "{type = " + this.type + ", morpheme = " + this.morpheme + "}";
    }
}