import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App {
    public static void main(String[] args) throws Exception {
        // 1. Create a list of type Employee (Superclass)
        Set<Employee> employees = new HashSet<>();

        Employee emp1 = new RegularEmployee("Alice", 101, 5000.0, 1000.0);
        Employee emp2 = new RegularEmployee("Jerry", 101, 50000.0, 1000.0);
        // emp1.hashCode();

        employees.add(emp1);
        employees.add(emp2);

        System.out.println(employees);


    //     System.out.println(emp1 == emp2);
    //     System.out.println(emp1.equals(emp2));

    //     // 2. Add both Regular and Contract employees to the list
    //     // This is possible because of the "IS-A" relationship established by Inheritance.
    //     employees.add(new RegularEmployee("Alice", 101, 5000.0, 1000.0));
    //     employees.add(new ContractEmployee("Bob", 102, 50.0, 120));


    //     // add 4 more emplyees to the list
    //     employees.add(new RegularEmployee("Charlie", 103, 6000.0, 1500.0));
    //     employees.add(new ContractEmployee("David", 104, 60.0, 100));
    //     employees.add(new RegularEmployee("Eve", 105, 5500.0, 1200.0));
    //     employees.add(new ContractEmployee("Frank", 106, 55.0, 80));

    //     System.out.println("Processing Payroll Polymorphically:\n");

    //     // 3. Process each employee uniformly using Polymorphism
    //     for (Employee emp : employees) {
    //         // At compile-time, Java only knows that 'emp' is an Employee.
    //         // At runtime, dynamic binding determines the actual object type
    //         // and executes the overridden calculateSalary() method.
    //         double salary = emp.calculateSalary();
    //         // System.out.printf("%s | Calculated Pay: $%.2f%n", emp, salary);
    //     }

    //     // calculate holiday bonus for each employee
    //     System.out.println("\nCalculating Holiday Bonus:\n");
    //     for (Employee emp : employees) {
    //         double bonus = calculateHolidayBonus(emp);
    //         // System.out.printf("%s | Holiday Bonus: $%.2f%n", emp, bonus);
    //     }
    // }

    // public static double calculateHolidayBonus(Employee emp) {
    // return switch (emp) {
    //     case RegularEmployee r -> r.getBonus() * 1.5;
    //     case ContractEmployee c -> 200.00;
    //     // No 'default' branch needed! The compiler knows there are no other subclasses.
    // };
    // // return 0.0; // Placeholder implementation
    }
}
