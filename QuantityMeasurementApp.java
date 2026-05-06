import java.util.Scanner;

public class QuantityMeasurementApp {

    static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            Feet other = (Feet) obj;
            return Double.compare(this.value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first value in feet: ");
        double input1 = scanner.nextDouble();

        System.out.print("Enter second value in feet: ");
        double input2 = scanner.nextDouble();

        Feet value1 = new Feet(input1);
        Feet value2 = new Feet(input2);

        boolean result = value1.equals(value2);

        System.out.println("Are values equal? " + result);

        scanner.close();
    }
}