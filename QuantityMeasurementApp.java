public class QuantityMeasurementApp {

    // =========================
    // ENUM: Length Units
    // =========================
    enum LengthUnit {

        FEET(1.0),
        INCH(1.0 / 12.0),
        YARD(3.0),
        CENTIMETER(0.393701 / 12.0);

        private final double factorToFeet;

        LengthUnit(double factor) {
            this.factorToFeet = factor;
        }

        public double toFeet(double value) {
            return value * factorToFeet;
        }

        public double fromFeet(double feetValue) {
            return feetValue / factorToFeet;
        }
    }

    // =========================
    // CLASS: Quantity
    // =========================
    static class Quantity {

        private final double value;
        private final LengthUnit unit;

        public Quantity(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Invalid value");
            }

            this.value = value;
            this.unit = unit;
        }

        // Convert to base unit (feet)
        private double toFeet() {
            return unit.toFeet(value);
        }

        // =========================
        // Instance Method (UC5)
        // =========================
        public Quantity convertTo(LengthUnit targetUnit) {

            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }

            double feetValue = this.toFeet();
            double convertedValue = targetUnit.fromFeet(feetValue);

            return new Quantity(convertedValue, targetUnit);
        }

        // =========================
        // Static Method (UC5 API)
        // =========================
        public static double convert(double value,
                                     LengthUnit source,
                                     LengthUnit target) {

            if (source == null || target == null) {
                throw new IllegalArgumentException("Units cannot be null");
            }

            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Invalid value");
            }

            double valueInFeet = source.toFeet(value);
            return target.fromFeet(valueInFeet);
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    // =========================
    // MAIN METHOD (UC5 Demo)
    // =========================
    public static void main(String[] args) {

        // Static conversion
        System.out.println(Quantity.convert(1.0, LengthUnit.FEET, LengthUnit.INCH));
        // 12.0

        System.out.println(Quantity.convert(3.0, LengthUnit.YARD, LengthUnit.FEET));
        // 9.0

        System.out.println(Quantity.convert(36.0, LengthUnit.INCH, LengthUnit.YARD));
        // 1.0

        // Instance conversion
        Quantity q = new Quantity(1.0, LengthUnit.FEET);
        System.out.println(q.convertTo(LengthUnit.INCH));
        // 12.0 INCH
    }
}