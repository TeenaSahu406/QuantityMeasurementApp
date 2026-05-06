public class QuantityMeasurementApp {

    enum LengthUnit {

        FEET(1.0),

        INCH(1.0 / 12.0),

        YARD(3.0),

        CENTIMETER(0.393701 / 12.0); // convert cm → inch → feet

        private final double toFeetFactor;

        LengthUnit(double factor) {
            this.toFeetFactor = factor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }
    }

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

        private double toFeet() {
            return unit.toFeet(value);
        }

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
    }

    public static void main(String[] args) {

        Quantity q1 = new Quantity(1.0, LengthUnit.YARD);
        Quantity q2 = new Quantity(3.0, LengthUnit.FEET);

        System.out.println(q1.equals(q2)); // true

        Quantity q3 = new Quantity(1.0, LengthUnit.YARD);
        Quantity q4 = new Quantity(36.0, LengthUnit.INCH);

        System.out.println(q3.equals(q4)); // true

        Quantity q5 = new Quantity(1.0, LengthUnit.CENTIMETER);
        Quantity q6 = new Quantity(0.393701, LengthUnit.INCH);

        System.out.println(q5.equals(q6)); // true
    }
}