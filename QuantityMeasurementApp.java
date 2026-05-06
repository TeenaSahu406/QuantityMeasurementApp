public class QuantityMeasurementApp {

    public static void main(String[] args) {

        // ===== LENGTH =====
        QuantityLength l1 = new QuantityLength(1, LengthUnit.FEET);
        QuantityLength l2 = new QuantityLength(12, LengthUnit.INCHES);

        System.out.println("Length Add: " + l1.add(l2));
        System.out.println("Length Convert: " + l1.convertTo(LengthUnit.INCHES));
        System.out.println("Length Equals: " + l1.equals(l2));

        // ===== WEIGHT =====
        QuantityWeight w1 = new QuantityWeight(1, WeightUnit.KILOGRAM);
        QuantityWeight w2 = new QuantityWeight(1000, WeightUnit.GRAM);

        System.out.println("Weight Add: " + w1.add(w2));
        System.out.println("Weight Convert: " + w1.convertTo(WeightUnit.POUND));
        System.out.println("Weight Equals: " + w1.equals(w2));

        // Explicit target unit
        System.out.println("Weight Add (grams): " +
                w1.add(w2, WeightUnit.GRAM));
    }
}


// ================= LENGTH UNIT =================
enum LengthUnit {
    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double factor;

    LengthUnit(double factor) {
        this.factor = factor;
    }

    public double toBase(double value) {
        return value * factor;
    }

    public double fromBase(double baseValue) {
        return baseValue / factor;
    }
}


// ================= QUANTITY LENGTH =================
final class QuantityLength {

    private final double value;
    private final LengthUnit unit;
    private static final double EPSILON = 1e-6;

    public QuantityLength(double value, LengthUnit unit) {
        if (unit == null || !Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid length input");
        }
        this.value = value;
        this.unit = unit;
    }

    public QuantityLength convertTo(LengthUnit target) {
        double base = unit.toBase(value);
        double converted = target.fromBase(base);
        return new QuantityLength(converted, target);
    }

    public QuantityLength add(QuantityLength other) {
        return add(other, this.unit);
    }

    public QuantityLength add(QuantityLength other, LengthUnit target) {
        if (other == null || target == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        double sumBase =
                this.unit.toBase(this.value) +
                other.unit.toBase(other.value);

        return new QuantityLength(target.fromBase(sumBase), target);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuantityLength)) return false;

        QuantityLength other = (QuantityLength) obj;

        double base1 = this.unit.toBase(this.value);
        double base2 = other.unit.toBase(other.value);

        return Math.abs(base1 - base2) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(unit.toBase(value));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}


// ================= WEIGHT UNIT =================
enum WeightUnit {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double factor;

    WeightUnit(double factor) {
        this.factor = factor;
    }

    public double toBase(double value) {
        return value * factor;
    }

    public double fromBase(double baseValue) {
        return baseValue / factor;
    }
}


// ================= QUANTITY WEIGHT =================
final class QuantityWeight {

    private final double value;
    private final WeightUnit unit;
    private static final double EPSILON = 1e-6;

    public QuantityWeight(double value, WeightUnit unit) {
        if (unit == null || !Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid weight input");
        }
        this.value = value;
        this.unit = unit;
    }

    public QuantityWeight convertTo(WeightUnit target) {
        if (target == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double base = unit.toBase(value);
        double converted = target.fromBase(base);

        return new QuantityWeight(converted, target);
    }

    public QuantityWeight add(QuantityWeight other) {
        return add(other, this.unit);
    }

    public QuantityWeight add(QuantityWeight other, WeightUnit target) {
        if (other == null || target == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        double sumBase =
                this.unit.toBase(this.value) +
                other.unit.toBase(other.value);

        double result = target.fromBase(sumBase);

        return new QuantityWeight(result, target);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuantityWeight)) return false;

        QuantityWeight other = (QuantityWeight) obj;

        double base1 = this.unit.toBase(this.value);
        double base2 = other.unit.toBase(other.value);

        return Math.abs(base1 - base2) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(unit.toBase(value));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}