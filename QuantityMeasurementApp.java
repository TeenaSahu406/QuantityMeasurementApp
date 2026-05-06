public class QuantityMeasurementApp {

    // ✅ Standalone Enum (UC8)
    enum LengthUnit {
        FEET(1.0),
        INCHES(1.0 / 12.0),
        YARDS(3.0),
        CENTIMETERS(1.0 / 30.48);

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double convertToBaseUnit(double value) {
            return value * toFeetFactor;
        }

        public double convertFromBaseUnit(double baseValue) {
            return baseValue / toFeetFactor;
        }
    }

    // ✅ Quantity Class
    static class QuantityLength {

        private final double value;
        private final LengthUnit unit;
        private static final double EPSILON = 1e-6;

        public QuantityLength(double value, LengthUnit unit) {
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Invalid value");
            }
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        // ✅ Convert (UC5)
        public QuantityLength convertTo(LengthUnit targetUnit) {
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }

            double base = unit.convertToBaseUnit(value);
            double result = targetUnit.convertFromBaseUnit(base);

            return new QuantityLength(result, targetUnit);
        }

        // ✅ Static Convert
        public static double convert(double value, LengthUnit source, LengthUnit target) {
            if (!Double.isFinite(value) || source == null || target == null) {
                throw new IllegalArgumentException("Invalid input");
            }

            double base = source.convertToBaseUnit(value);
            return target.convertFromBaseUnit(base);
        }

        // ✅ Addition (UC6)
        public QuantityLength add(QuantityLength other) {
            return add(other, this.unit);
        }

        // ✅ Addition with target unit (UC7)
        public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
            if (other == null || targetUnit == null) {
                throw new IllegalArgumentException("Invalid input");
            }

            double base1 = this.unit.convertToBaseUnit(this.value);
            double base2 = other.unit.convertToBaseUnit(other.value);

            double sumBase = base1 + base2;
            double result = targetUnit.convertFromBaseUnit(sumBase);

            return new QuantityLength(result, targetUnit);
        }

        // ✅ Equals (UC1–UC4)
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof QuantityLength)) return false;

            QuantityLength other = (QuantityLength) obj;

            double base1 = this.unit.convertToBaseUnit(this.value);
            double base2 = other.unit.convertToBaseUnit(other.value);

            return Math.abs(base1 - base2) < EPSILON;
        }

        @Override
        public String toString() {
            return "Quantity(" + value + ", " + unit + ")";
        }
    }

    // ✅ Main Method
    public static void main(String[] args) {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCHES);

        // Equality
        System.out.println("Equality: " + q1.equals(q2));

        // Conversion
        System.out.println("Convert to inches: " + q1.convertTo(LengthUnit.INCHES));

        // Static convert
        System.out.println("Static convert: " +
                QuantityLength.convert(1.0, LengthUnit.FEET, LengthUnit.INCHES));

        // Addition (default)
        System.out.println("Add (default): " + q1.add(q2));

        // Addition (target unit)
        System.out.println("Add (yards): " + q1.add(q2, LengthUnit.YARDS));

        // Extra test
        QuantityLength q3 = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength q4 = new QuantityLength(1.0, LengthUnit.YARDS);

        System.out.println("Yard == Feet: " + q3.equals(q4));
    }
}