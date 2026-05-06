// =======================
// IMeasurable Interface
// =======================
interface IMeasurable {
    double getConversionFactor();
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    String getUnitName();
}

// =======================
// LengthUnit Enum
// Base Unit: FEET
// =======================
enum LengthUnit implements IMeasurable {

    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * conversionFactor; // convert to feet
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }

    @Override
    public String getUnitName() {
        return this.name();
    }
}

// =======================
// WeightUnit Enum
// Base Unit: KILOGRAM
// =======================
enum WeightUnit implements IMeasurable {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * conversionFactor; // convert to kg
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }

    @Override
    public String getUnitName() {
        return this.name();
    }
}

// =======================
// Generic Quantity Class
// =======================
class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;
    private static final double EPSILON = 1e-6;

    public Quantity(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    // ===================
    // EQUALS
    // ===================
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Quantity<?>)) return false;

        Quantity<?> other = (Quantity<?>) obj;

        // Prevent cross-category comparison
        if (!this.unit.getClass().equals(other.unit.getClass())) {
            return false;
        }

        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = ((IMeasurable) other.unit).convertToBaseUnit(other.value);

        return Math.abs(base1 - base2) < EPSILON;
    }

    @Override
    public int hashCode() {
        double base = unit.convertToBaseUnit(value);
        return Double.hashCode(base);
    }

    // ===================
    // CONVERSION
    // ===================
    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double base = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(base);

        return new Quantity<>(round(converted), targetUnit);
    }

    // ===================
    // ADDITION (DEFAULT)
    // ===================
    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    // ===================
    // ADDITION (TARGET UNIT)
    // ===================
    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        if (other == null || targetUnit == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        double sum = base1 + base2;

        double result = targetUnit.convertFromBaseUnit(sum);

        return new Quantity<>(round(result), targetUnit);
    }

    // ===================
    // ROUNDING
    // ===================
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
    }
}

// =======================
// QuantityMeasurementApp
// =======================
public class QuantityMeasurementApp {

    public static void main(String[] args) {

        // ========= LENGTH =========
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

        System.out.println("Length Equality: " + l1.equals(l2));
        System.out.println("Length Conversion: " + l1.convertTo(LengthUnit.INCHES));
        System.out.println("Length Addition: " + l1.add(l2, LengthUnit.FEET));

        // ========= WEIGHT =========
        Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

        System.out.println("Weight Equality: " + w1.equals(w2));
        System.out.println("Weight Conversion: " + w1.convertTo(WeightUnit.GRAM));
        System.out.println("Weight Addition: " + w1.add(w2, WeightUnit.KILOGRAM));

        // ========= CROSS CATEGORY =========
        System.out.println("Cross Category Comparison: " + l1.equals(w1)); // false
    }
}