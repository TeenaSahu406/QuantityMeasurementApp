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

        // Convert → feet (base unit)
        public double toFeet(double value) {
            return value * factorToFeet;
        }

        // Convert ← from feet
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

        // Convert to base unit
        private double toFeet() {
            return unit.toFeet(value);
        }

        // =========================
        // UC5: Conversion
        // =========================
        public Quantity convertTo(LengthUnit targetUnit) {

            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }

            double feetValue = this.toFeet();
            double convertedValue = targetUnit.fromFeet(feetValue);

            return new Quantity(convertedValue, targetUnit);
        }

        public static double convert(double value,
                                     LengthUnit source,
                                     LengthUnit target) {

            if (source == null || target == null) {
                throw new IllegalArgumentException("Units cannot be null");
            }

            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Invalid value");
            }

            double feetValue = source.toFeet(value);
            return target.fromFeet(feetValue);
        }

        // =========================
        // UC6: ADDITION
        // =========================
        public Quantity add(Quantity other) {

            if (other == null) {
                throw new IllegalArgumentException("Other quantity cannot be null");
            }

            // Convert both → feet
            double thisFeet = this.toFeet();
            double otherFeet = other.toFeet();

            // Add
            double sumFeet = thisFeet + otherFeet;

            // Convert back to THIS unit
            double resultValue = this.unit.fromFeet(sumFeet);

            return new Quantity(resultValue, this.unit);
        }

        // Optional static method
        public static Quantity add(Quantity q1, Quantity q2) {
            if (q1 == null || q2 == null) {
                throw new IllegalArgumentException("Quantities cannot be null");
            }
            return q1.add(q2);
        }

        // =========================
        // Equality (UC1–UC4)
        // =========================
        @Override
        public boolean equals(Object obj) {

            if (this == obj) return true;

            if (obj == null || getClass() != obj.getClass()) return false;

            Quantity other = (Quantity) obj;

            return Double.compare(this.toFeet(), other.toFeet()) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(toFeet());
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    // =========================
    // MAIN METHOD (UC6 Demo)
    // =========================
    public static void main(String[] args) {

        // Same unit
        System.out.println(new Quantity(1.0, LengthUnit.FEET)
                .add(new Quantity(2.0, LengthUnit.FEET)));
        // 3.0 FEET

        // Feet + Inches
        System.out.println(new Quantity(1.0, LengthUnit.FEET)
                .add(new Quantity(12.0, LengthUnit.INCH)));
        // 2.0 FEET

        // Inches + Feet
        System.out.println(new Quantity(12.0, LengthUnit.INCH)
                .add(new Quantity(1.0, LengthUnit.FEET)));
        // 24.0 INCH

        // Yard + Feet
        System.out.println(new Quantity(1.0, LengthUnit.YARD)
                .add(new Quantity(3.0, LengthUnit.FEET)));
        // 2.0 YARD

        // CM + Inch
        System.out.println(new Quantity(2.54, LengthUnit.CENTIMETER)
                .add(new Quantity(1.0, LengthUnit.INCH)));
        // ~5.08 CENTIMETER

        // With zero
        System.out.println(new Quantity(5.0, LengthUnit.FEET)
                .add(new Quantity(0.0, LengthUnit.INCH)));
        // 5.0 FEET

        // Negative values
        System.out.println(new Quantity(5.0, LengthUnit.FEET)
                .add(new Quantity(-2.0, LengthUnit.FEET)));
        // 3.0 FEET
    }
}