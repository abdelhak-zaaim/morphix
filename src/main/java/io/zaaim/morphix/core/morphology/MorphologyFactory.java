package io.zaaim.morphix.core.morphology;

public class MorphologyFactory {

    public static MorphologyService create(String type) {
        if (type == null) type = "safari";
        return switch (type.toLowerCase()) {
            case "safari" -> new SafarMorphologyService();
            // case "khoja" -> new KhojaMorphologyService();
            default -> throw new IllegalArgumentException("Unknown morphology type: " + type);
        };
    }
}
