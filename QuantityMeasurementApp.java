public class QuantityMeasurementApp {

    // Step 1: Enum for units
    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0); // 1 inch = 1/12 feet

        private final double conversionFactorToFeet;

        LengthUnit(double factor) {
            this.conversionFactorToFeet = factor;
        }

        public double toFeet(double value) {
            return value * conversionFactorToFeet;
        }
    }

    // Step 2: Generic Quantity class
    static class Quantity {
        private final double value;
        private final LengthUnit unit;

        public Quantity(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        // Convert to base unit (feet)
        private double toFeet() {
            return unit.toFeet(value);
        }

        // Step 3: equals method with conversion
        @Override
        public boolean equals(Object obj) {

            if (this == obj) return true;

            if (obj == null || getClass() != obj.getClass()) return false;

            Quantity other = (Quantity) obj;

            // Compare after converting both to feet
            return Double.compare(this.toFeet(), other.toFeet()) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(toFeet());
        }
    }

    public static void main(String[] args) {

        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity q2 = new Quantity(12.0, LengthUnit.INCH);

        System.out.println("Are equal? " + q1.equals(q2)); // true

        Quantity q3 = new Quantity(1.0, LengthUnit.INCH);
        Quantity q4 = new Quantity(1.0, LengthUnit.INCH);

        System.out.println("Are equal? " + q3.equals(q4)); // true
    }
}