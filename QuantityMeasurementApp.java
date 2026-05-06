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

        // =========================
        // UC6: Addition (default → first unit)
        // =========================
        public Quantity add(Quantity other) {

            if (other == null) {
                throw new IllegalArgumentException("Other quantity cannot be null");
            }

            return add(other, this.unit);
        }

        // =========================
        // UC7: Addition with Target Unit
        // =========================
        public Quantity add(Quantity other, LengthUnit targetUnit) {

            if (other == null) {
                throw new IllegalArgumentException("Other quantity cannot be null");
            }

            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }

            // Convert both → base unit (feet)
            double thisFeet = this.toFeet();
            double otherFeet = other.toFeet();

            // Add
            double sumFeet = thisFeet + otherFeet;

            // Convert to target unit
            double resultValue = targetUnit.fromFeet(sumFeet);

            return new Quantity(resultValue, targetUnit);
        }

        // Optional static method
        public static Quantity add(Quantity q1, Quantity q2, LengthUnit targetUnit) {

            if (q1 == null || q2 == null) {
                throw new IllegalArgumentException("Quantities cannot be null");
            }

            return q1.add(q2, targetUnit);
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
    // MAIN METHOD (UC7 Demo)
    // =========================
    public static void main(String[] args) {

        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity q2 = new Quantity(12.0, LengthUnit.INCH);

        System.out.println(q1.add(q2, LengthUnit.FEET));   // 2.0 FEET
        System.out.println(q1.add(q2, LengthUnit.INCH));   // 24.0 INCH
        System.out.println(q1.add(q2, LengthUnit.YARD));   // ~0.667 YARD

        Quantity q3 = new Quantity(1.0, LengthUnit.YARD);
        Quantity q4 = new Quantity(3.0, LengthUnit.FEET);

        System.out.println(q3.add(q4, LengthUnit.YARD));   // 2.0 YARD

        Quantity q5 = new Quantity(36.0, LengthUnit.INCH);
        Quantity q6 = new Quantity(1.0, LengthUnit.YARD);

        System.out.println(q5.add(q6, LengthUnit.FEET));   // 6.0 FEET

        Quantity q7 = new Quantity(2.54, LengthUnit.CENTIMETER);
        Quantity q8 = new Quantity(1.0, LengthUnit.INCH);

        System.out.println(q7.add(q8, LengthUnit.CENTIMETER)); // ~5.08 CM
    }
}