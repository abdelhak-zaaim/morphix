package io.zaaim.morphix.core.morphology;

public interface MorphologyService {
    /**
     * Extract root (جذر) for the given word (may return null if none).
     */
    String extractRoot(String word);

    /**
     * Extract stem for the given word.
     */
    String extractStem(String word);

    /**
     * Whether this service supports diacritics/advanced features
     */
    default boolean supportsDiacritics() {
        return false;
    }
}
