package lambdasdemo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
// import java.util.concurrent.locks.Condition;
import java.util.function.Predicate;

// @FunctionalInterface
// interface Condition<T> {
//     boolean test(T t);
// }

public class LamdasVsImperative {
    public static void main(String[] args) {
         List<Employee> employees = new ArrayList<>();

    //     // 2. Add both Regular and Contract employees to the list
    //     // This is possible because of the "IS-A" relationship established by Inheritance.
        employees.add(new RegularEmployee("Alice", 101, 5000.0, 1000.0));
        employees.add(new ContractEmployee("Bob", 102, 50.0, 120));


    //     // add 4 more emplyees to the list
        employees.add(new RegularEmployee("Charlie", 103, 6000.0, 1500.0));
        employees.add(new ContractEmployee("David", 104, 60.0, 100));
        employees.add(new RegularEmployee("Eve", 105, 5500.0, 1200.0));
        employees.add(new ContractEmployee("Frank", 106, 55.0, 80));


        // if employee name starts with 'B', print employee
        // printEmployees(employees, new Condition() {
        //     @Override
        //     public boolean test(Object t) {
        //         Employee emp = (Employee) t;
        //         return emp.getName().startsWith("B");
        //     }
        // });

        printEmployees(employees, (e)->e.getName().startsWith("B"));


        // if employee salary is greater than 6000, print employee
        printEmployees(employees, (e)->e.calculateSalary() > 6000);


        // print all employees
        printEmployees(employees, (e)->true);
    }


    public static void printEmployees(List<Employee> employees, Predicate<Employee> condition) {
        for (Employee emp : employees) {
            if (condition.test(emp)) {
                System.out.println(emp);
            }
        }
    }


}
