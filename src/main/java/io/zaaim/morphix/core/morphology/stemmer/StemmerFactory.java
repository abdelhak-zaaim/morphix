package io.zaaim.morphix.core.morphology.stemmer;

import common.manage_errors.Errors;
import safar.basic.morphology.stemmer.impl.ISRIStemmer;
import safar.basic.morphology.stemmer.impl.KhojaStemmer;
import safar.basic.morphology.stemmer.impl.Light10Stemmer;
import safar.basic.morphology.stemmer.impl.MotazStemmer;
import safar.basic.morphology.stemmer.impl.TashaphyneStemmer;
import safar.basic.morphology.stemmer.interfaces.IStemmer;

public abstract class StemmerFactory {
    private static IStemmer khojaImpl;
    private static IStemmer light10Impl;
    private static IStemmer motazImpl;
    private static IStemmer tashaphyneImpl;
    private static IStemmer isriImpl;

    public static IStemmer getKhojaImplementation() {
        if (khojaImpl == null) {
            khojaImpl = new KhojaStemmer();
        }

        return khojaImpl;
    }

    public static IStemmer getLight10Implementation() {
        if (light10Impl == null) {
            light10Impl = new Light10Stemmer();
        }

        return light10Impl;
    }

    public static IStemmer getMotazImplementation() {
        if (motazImpl == null) {
            motazImpl = new MotazStemmer();
        }

        return motazImpl;
    }

    public static IStemmer getTashaphyneImplementation() {
        if (tashaphyneImpl == null) {
            tashaphyneImpl = new TashaphyneStemmer();
        }

        return tashaphyneImpl;
    }

    public static IStemmer getISRIImplementation() {
        if (isriImpl == null) {
            isriImpl = new ISRIStemmer();
        }

        return isriImpl;
    }

    public static IStemmer getImplementation(String stemmer) {
        if (stemmer.equals("KHOJA_STEMMER")) {
            return getKhojaImplementation();
        } else if (stemmer.equals("LIGHT10_STEMMER")) {
            return getLight10Implementation();
        } else if (stemmer.equals("MOTAZ_STEMMER")) {
            return getMotazImplementation();
        } else if (stemmer.equals("TASHAPHYNE_STEMMER")) {
            return getTashaphyneImplementation();
        } else if (stemmer.equals("ISRI_STEMMER")) {
            return getISRIImplementation();
        } else {
            Errors.manageError("Error", "Can not found \"" + stemmer + "\" implementation");
            return null;
        }
    }
}