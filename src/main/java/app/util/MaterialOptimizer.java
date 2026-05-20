package app.util;

import app.entities.Component;
import app.entities.MaterialSelection;

import java.util.List;

public class MaterialOptimizer {

    private MaterialOptimizer() {
    }

    public static MaterialSelection findBestCombination(List<Component> stock, double requiredLength) {

        MaterialSelection best = null;

        for (Component first : stock) {

            double firstLength =
                    first.getLength();

            if (firstLength >= requiredLength) {

                double waste = firstLength - requiredLength;

                MaterialSelection candidate =
                        new MaterialSelection(
                                List.of(first),
                                firstLength,
                                waste
                        );

                best = chooseBetter(best, candidate);
            }

            for (Component second : stock) {

                double total = first.getLength() + second.getLength();

                if (total >= requiredLength) {

                    double waste = total - requiredLength;

                    MaterialSelection candidate =
                            new MaterialSelection(
                                    List.of(first, second),
                                    total,
                                    waste
                            );

                    best = chooseBetter(best, candidate);
                }
            }
        }

        return best;
    }

    private static MaterialSelection chooseBetter(
            MaterialSelection current,
            MaterialSelection candidate) {

        if (current == null) {
            return candidate;
        }

        if (candidate.getWaste() < current.getWaste()) {
            return candidate;
        }

        return current;
    }
}