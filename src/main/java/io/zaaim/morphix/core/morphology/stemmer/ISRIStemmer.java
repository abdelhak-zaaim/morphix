package io.zaaim.morphix.core.morphology.stemmer;

import java.util.*;
import java.util.regex.Pattern;

public class ISRIStemmer {

    private String stem;

    // Length three prefixes
    private final List<String> p3 = Arrays.asList("كال", "بال", "ولل", "وال");

    // Length two prefixes
    private final List<String> p2 = Arrays.asList("ال", "لل");

    // Length one prefixes
    private final List<String> p1 = Arrays.asList("ل", "ب", "ف", "س", "و", "ي", "ت", "ن", "ا");

    // Length three suffixes
    private final List<String> s3 = Arrays.asList("تمل", "همل", "تان", "تين", "كمل");

    // Length two suffixes
    private final List<String> s2 = Arrays.asList("ون", "ات", "ان", "ين", "تن", "كم", "هن", "نا", "يا", "ها", "تم", "كن", "ني", "وا", "ما", "هم");

    // Length one suffixes
    private final List<String> s1 = Arrays.asList("ة", "ه", "ي", "ك", "ت", "ا", "ن");

    // Pattern groups for length 4
    private final Map<Integer, List<String>> pr4 = new HashMap<>();

    // Pattern groups for length 5 and 3 roots
    private final Map<Integer, List<String>> pr53 = new HashMap<>();

    // Regex patterns
    private final Pattern reShortVowels = Pattern.compile("[\\u064B-\\u0652]");
    private final Pattern reHamza = Pattern.compile("[\\u0621\\u0624\\u0626]");
    private final Pattern reInitialHamza = Pattern.compile("^[\\u0622\\u0623\\u0625]");

    // Stop words
    private final Set<String> stopWords = new HashSet<>(Arrays.asList(
            "يكون", "وليس", "وكان", "كذلك", "التي", "وبين", "عليها", "مساء", "الذي", "وكانت",
            "ولكن", "والتي", "تكون", "اليوم", "اللذين", "عليه", "كانت", "لذلك", "أمام", "هناك",
            "منها", "مازال", "لازال", "لايزال", "مايزال", "اصبح", "أصبح", "أمسى", "امسى", "أضحى",
            "اضحى", "مابرح", "مافتئ", "ماانفك", "لاسيما", "ولايزال", "الحالي", "اليها", "الذين",
            "فانه", "والذي", "وهذا", "لهذا", "فكان", "ستكون", "اليه", "يمكن", "بهذا", "الذى"
    ));

    public ISRIStemmer() {
        initializePatterns();
    }

    private void initializePatterns() {
        pr4.put(0, Collections.singletonList("م"));
        pr4.put(1, Collections.singletonList("ا"));
        pr4.put(2, Arrays.asList("ا", "و", "ي"));
        pr4.put(3, Collections.singletonList("ة"));

        pr53.put(0, Arrays.asList("ا", "ت"));
        pr53.put(1, Arrays.asList("ا", "ي", "و"));
        pr53.put(2, Arrays.asList("ا", "ت", "م"));
        pr53.put(3, Arrays.asList("م", "ي", "ت"));
        pr53.put(4, Arrays.asList("م", "ت"));
        pr53.put(5, Arrays.asList("ا", "و"));
        pr53.put(6, Arrays.asList("ا", "م"));
    }

    public String stem(String token) {
        this.stem = token;
        norm(1);

        if (stopWords.contains(this.stem)) {
            return this.stem;
        }

        pre32();
        suf32();
        waw();
        norm(2);

        if (this.stem.length() <= 3) {
            return this.stem;
        }

        switch (this.stem.length()) {
            case 4:
                proW4();
                break;
            case 5:
                proW53();
                endW5();
                break;
            case 6:
                proW6();
                endW6();
                break;
            case 7:
                suf1();
                if (this.stem.length() == 7) {
                    pre1();
                }
                if (this.stem.length() == 6) {
                    proW6();
                    endW6();
                }
                break;
        }

        return this.stem;
    }

    private void norm(int num) {
        if (num == 1) {
            this.stem = reShortVowels.matcher(this.stem).replaceAll("");
        } else if (num == 2) {
            this.stem = reInitialHamza.matcher(this.stem).replaceAll("ا");
        } else if (num == 3) {
            this.stem = reShortVowels.matcher(this.stem).replaceAll("");
            this.stem = reInitialHamza.matcher(this.stem).replaceAll("ا");
        }
    }

    private void pre32() {
        if (this.stem.length() >= 6) {
            for (String pre3 : p3) {
                if (this.stem.startsWith(pre3)) {
                    this.stem = this.stem.substring(3);
                    return;
                }
            }
        }
        if (this.stem.length() >= 5) {
            for (String pre2 : p2) {
                if (this.stem.startsWith(pre2)) {
                    this.stem = this.stem.substring(2);
                    return;
                }
            }
        }
    }

    private void suf32() {
        if (this.stem.length() >= 6) {
            for (String suf3 : s3) {
                if (this.stem.endsWith(suf3)) {
                    this.stem = this.stem.substring(0, this.stem.length() - 3);
                    return;
                }
            }
        }
        if (this.stem.length() >= 5) {
            for (String suf2 : s2) {
                if (this.stem.endsWith(suf2)) {
                    this.stem = this.stem.substring(0, this.stem.length() - 2);
                    return;
                }
            }
        }
    }

    private void waw() {
        if (this.stem.length() >= 4 && this.stem.startsWith("وو")) {
            this.stem = this.stem.substring(1);
        }
    }

    private void proW4() {
        if (pr4.get(0).contains(String.valueOf(this.stem.charAt(0)))) {
            this.stem = this.stem.substring(1);
        } else if (pr4.get(1).contains(String.valueOf(this.stem.charAt(1)))) {
            this.stem = String.valueOf(this.stem.charAt(0)) + this.stem.substring(2);
        } else if (pr4.get(2).contains(String.valueOf(this.stem.charAt(2)))) {
            this.stem = this.stem.substring(0, 2) + this.stem.charAt(3);
        } else if (pr4.get(3).contains(String.valueOf(this.stem.charAt(3)))) {
            this.stem = this.stem.substring(0, 3);
        } else {
            suf1();
            if (this.stem.length() == 4) {
                pre1();
            }
        }
    }

    private void proW53() {
        if (this.stem.length() < 5) return;

        char c0 = this.stem.charAt(0);
        char c1 = this.stem.charAt(1);
        char c2 = this.stem.charAt(2);
        char c3 = this.stem.charAt(3);
        char c4 = this.stem.charAt(4);

        if (pr53.get(0).contains(String.valueOf(c2)) && c0 == 'ا') {
            this.stem = String.valueOf(c1) + this.stem.substring(3);
        } else if (pr53.get(1).contains(String.valueOf(c3)) && c0 == 'م') {
            this.stem = this.stem.substring(1, 3) + c4;
        } else if (pr53.get(2).contains(String.valueOf(c0)) && c4 == 'ة') {
            this.stem = this.stem.substring(1, 4);
        } else if (pr53.get(3).contains(String.valueOf(c0)) && c2 == 'ت') {
            this.stem = String.valueOf(c1) + this.stem.substring(3);
        } else if (pr53.get(4).contains(String.valueOf(c0)) && c2 == 'ا') {
            this.stem = String.valueOf(c1) + this.stem.substring(3);
        } else if (pr53.get(5).contains(String.valueOf(c2)) && c4 == 'ة') {
            this.stem = this.stem.substring(0, 2) + c3;
        } else if (pr53.get(6).contains(String.valueOf(c0)) && c1 == 'ن') {
            this.stem = this.stem.substring(2);
        } else if (c3 == 'ا' && c0 == 'ا') {
            this.stem = this.stem.substring(1, 3) + c4;
        } else if (c4 == 'ن' && c3 == 'ا') {
            this.stem = this.stem.substring(0, 3);
        } else if (c3 == 'ي' && c0 == 'ت') {
            this.stem = this.stem.substring(1, 3) + c4;
        } else if (c3 == 'و' && c1 == 'ا') {
            this.stem = String.valueOf(c0) + c2 + c4;
        } else if (c2 == 'ا' && c1 == 'و') {
            this.stem = String.valueOf(c0) + this.stem.substring(3);
        } else if (c3 == 'ئ' && c2 == 'ا') {
            this.stem = this.stem.substring(0, 2) + c4;
        } else if (c4 == 'ة' && c1 == 'ا') {
            this.stem = String.valueOf(c0) + this.stem.substring(2, 4);
        } else if (c4 == 'ي' && c2 == 'ا') {
            this.stem = this.stem.substring(0, 2) + c3;
        } else {
            suf1();
            if (this.stem.length() == 5) {
                pre1();
            }
        }
    }

    private void proW54() {
        if (this.stem.length() < 5) return;

        char c0 = this.stem.charAt(0);
        char c2 = this.stem.charAt(2);
        char c4 = this.stem.charAt(4);

        if (pr53.get(2).contains(String.valueOf(c0))) {
            this.stem = this.stem.substring(1);
        } else if (c4 == 'ة') {
            this.stem = this.stem.substring(0, 4);
        } else if (c2 == 'ا') {
            this.stem = this.stem.substring(0, 2) + this.stem.substring(3);
        }
    }

    private void endW5() {
        int len = this.stem.length();
        if (len == 3) {
            return;
        } else if (len == 4) {
            proW4();
        } else if (len == 5) {
            proW54();
        }
    }

    private void proW6() {
        if (this.stem.length() < 6) return;

        if (this.stem.startsWith("است") || this.stem.startsWith("مست")) {
            this.stem = this.stem.substring(3);
        } else if (this.stem.charAt(0) == 'م' && this.stem.charAt(3) == 'ا' && this.stem.charAt(5) == 'ة') {
            this.stem = this.stem.substring(1, 3) + this.stem.charAt(4);
        } else if (this.stem.charAt(0) == 'ا' && this.stem.charAt(2) == 'ت' && this.stem.charAt(4) == 'ا') {
            this.stem = String.valueOf(this.stem.charAt(1)) + this.stem.charAt(3) + this.stem.charAt(5);
        } else if (this.stem.charAt(0) == 'ا' && this.stem.charAt(3) == 'و' && this.stem.charAt(2) == this.stem.charAt(4)) {
            this.stem = String.valueOf(this.stem.charAt(1)) + this.stem.substring(4);
        } else if (this.stem.charAt(0) == 'ت' && this.stem.charAt(2) == 'ا' && this.stem.charAt(4) == 'ي') {
            this.stem = String.valueOf(this.stem.charAt(1)) + this.stem.charAt(3) + this.stem.charAt(5);
        } else {
            suf1();
            if (this.stem.length() == 6) {
                pre1();
            }
        }
    }

    private void proW64() {
        if (this.stem.length() < 6) return;

        if (this.stem.charAt(0) == 'ا' && this.stem.charAt(4) == 'ا') {
            this.stem = this.stem.substring(1, 4) + this.stem.charAt(5);
        } else if (this.stem.startsWith("مت")) {
            this.stem = this.stem.substring(2);
        }
    }

    private void endW6() {
        int len = this.stem.length();
        if (len == 3) {
            return;
        } else if (len == 5) {
            proW53();
            endW5();
        } else if (len == 6) {
            proW64();
        }
    }

    private void suf1() {
        for (String sf1 : s1) {
            if (this.stem.endsWith(sf1)) {
                this.stem = this.stem.substring(0, this.stem.length() - 1);
                return;
            }
        }
    }

    private void pre1() {
        for (String sp1 : p1) {
            if (this.stem.startsWith(sp1)) {
                this.stem = this.stem.substring(1);
                return;
            }
        }
    }
}
