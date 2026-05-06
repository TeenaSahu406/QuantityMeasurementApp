import java.util.Objects;

// -------------------- INTERFACE --------------------
interface IMeasurable {
    double getConversionFactor();

    default double convertToBaseUnit(double value) {
        return value * getConversionFactor();
    }

    default double convertFromBaseUnit(double baseValue) {
        return baseValue / getConversionFactor();
    }

    String getUnitName();
}

// -------------------- LENGTH --------------------
enum LengthUnit implements IMeasurable {
    FEET(1.0),
    INCH(1.0 / 12.0);

    private final double factor;

    LengthUnit(double factor) {
        this.factor = factor;
    }

    public double getConversionFactor() {
        return factor;
    }

    public String getUnitName() {
        return name();
    }
}

// -------------------- WEIGHT --------------------
enum WeightUnit implements IMeasurable {
    KILOGRAM(1.0),
    GRAM(0.001);

    private final double factor;

    WeightUnit(double factor) {
        this.factor = factor;
    }

    public double getConversionFactor() {
        return factor;
    }

    public String getUnitName() {
        return name();
    }
}

// -------------------- VOLUME --------------------
enum VolumeUnit implements IMeasurable {
    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double factor;

    VolumeUnit(double factor) {
        this.factor = factor;
    }

    public double getConversionFactor() {
        return factor;
    }

    public String getUnitName() {
        return name();
    }
}

// -------------------- QUANTITY --------------------
class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        if (Double.isNaN(value) || Double.isInfinite(value))
            throw new IllegalArgumentException("Invalid numeric value");

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    // -------------------- VALIDATION --------------------
    private void validateOperand(Quantity<U> other) {
        if (other == null)
            throw new IllegalArgumentException("Quantity cannot be null");

        if (!this.unit.getClass().equals(other.unit.getClass()))
            throw new IllegalArgumentException("Different measurement categories");

        if (Double.isNaN(other.value) || Double.isInfinite(other.value))
            throw new IllegalArgumentException("Invalid numeric value");
    }

    private double toBase() {
        return unit.convertToBaseUnit(value);
    }

    // -------------------- EQUALITY --------------------
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quantity<?> other)) return false;

        if (!this.unit.getClass().equals(other.unit.getClass()))
            return false;

        double base1 = this.toBase();
        double base2 = ((Quantity<U>) other).toBase();

        return Math.abs(base1 - base2) < 1e-6;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit.getClass(), toBase());
    }

    // -------------------- CONVERSION --------------------
    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double base = toBase();
        double converted = targetUnit.convertFromBaseUnit(base);

        return new Quantity<>(converted, targetUnit);
    }

    // -------------------- ADDITION --------------------
    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateOperand(other);

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double resultBase = this.toBase() + other.toBase();
        double result = targetUnit.convertFromBaseUnit(resultBase);

        return new Quantity<>(result, targetUnit);
    }

    // ==================== UC12 START ====================

    // -------------------- SUBTRACTION --------------------
    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateOperand(other);

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double resultBase = this.toBase() - other.toBase();
        double result = targetUnit.convertFromBaseUnit(resultBase);

        return new Quantity<>(result, targetUnit);
    }

    // -------------------- DIVISION --------------------
    public double divide(Quantity<U> other) {
        validateOperand(other);

        double divisor = other.toBase();
        if (divisor == 0)
            throw new ArithmeticException("Division by zero");

        return this.toBase() / divisor;
    }

    // ==================== UC12 END ====================

    @Override
    public String toString() {
        return String.format("%.4f %s", value, unit.getUnitName());
    }
}

// -------------------- APPLICATION --------------------
public class QuantityMeasurementApp {

    public static void main(String[] args) {

        // -------- LENGTH --------
        Quantity<LengthUnit> len1 = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> len2 = new Quantity<>(6, LengthUnit.INCH);

        System.out.println("Length Subtraction: " + len1.subtract(len2));
        System.out.println("Length Division: " + len1.divide(len2));

        // -------- WEIGHT --------
        Quantity<WeightUnit> w1 = new Quantity<>(10, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(5000, WeightUnit.GRAM);

        System.out.println("Weight Subtraction: " + w1.subtract(w2));
        System.out.println("Weight Division: " + w1.divide(w2));

        // -------- VOLUME --------
        Quantity<VolumeUnit> v1 = new Quantity<>(5, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(500, VolumeUnit.MILLILITRE);

        System.out.println("Volume Subtraction: " + v1.subtract(v2));
        System.out.println("Volume Division: " + v1.divide(v2));

        // -------- ADDITION --------
        System.out.println("Addition: " + v1.add(v2));

        // -------- CONVERSION --------
        System.out.println("Conversion: " + v1.convertTo(VolumeUnit.GALLON));

        // -------- EQUALITY --------
        Quantity<VolumeUnit> v3 = new Quantity<>(1000, VolumeUnit.MILLILITRE);
        System.out.println("Equality: " + v1.equals(v3));
    }
}