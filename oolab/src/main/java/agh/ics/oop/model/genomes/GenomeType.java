package agh.ics.oop.model.genomes;

import java.util.Optional;

public enum GenomeType {
    FULL_RANDOM_GENOME_CHANGE("Pełna losowość", new FullRandomGenomeChange()),
    REPLACEMENT_GENOME_CHANGE("Podmianka", new ReplacmentGenomeChange());

    private final String displayName;
    private final GenomeChange genomeChange;

    GenomeType(String displayName, GenomeChange genomeChange) {
        this.displayName = displayName;
        this.genomeChange = genomeChange;
    }

    public String getDisplayName() {
        return displayName;
    }

    public GenomeChange getGenomeChange() {
        return genomeChange;
    }

    public static Optional<GenomeType> fromDisplayName(String displayName) {
        return switch (displayName) {
            case "Pełna losowość" -> Optional.of(GenomeType.FULL_RANDOM_GENOME_CHANGE);
            case "Podmianka" -> Optional.of(GenomeType.REPLACEMENT_GENOME_CHANGE);
            default -> Optional.empty();
        };
    }
}
