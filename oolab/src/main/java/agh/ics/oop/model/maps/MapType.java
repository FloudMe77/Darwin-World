package agh.ics.oop.model.maps;

import java.util.Optional;
import java.util.function.BiFunction;

public enum MapType {
    EARTH_MAP("Kula Ziemska", EarthMap::new),
    OWLBEAR_MAP("Dziki sowoniedźwiedź", OwlBearMap::new);

    private final String displayName;
    private final BiFunction<Integer, Integer, WorldMap> mapCreator;

    MapType(String displayName, BiFunction<Integer, Integer, WorldMap> mapCreator) {
        this.displayName = displayName;
        this.mapCreator = mapCreator;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Optional<MapType> fromDisplayName(String displayName) {
        return switch (displayName) {
            case "Kula Ziemska" -> Optional.of(MapType.EARTH_MAP);
            case "Dziki sowoniedźwiedź" -> Optional.of(MapType.OWLBEAR_MAP);
            default -> Optional.empty(); // czy wyjątek nie byłby lepszy?
        };
    }

    public WorldMap createMap(int width, int height) {
        return mapCreator.apply(width, height);
    }
}
