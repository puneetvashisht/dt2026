package threads;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Employee {
    private String name;
    private double salary;

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }
}

class SalaryFetcher implements Callable<Double> {
    private Employee employee;

    public SalaryFetcher(Employee employee) {
        this.employee = employee;
    }

    @Override
    public Double call() throws Exception {
        // Simulate delay in fetching salary (e.g., from a database or API)
        Thread.sleep(1000); // 1 second delay
        return employee.getSalary();
    }
}

public class BasicDemo {
    public static void main(String[] args) {
    
        // for(int i = 0; i < 5; i++) {
        //     Thread t = new Thread(() -> {
        //         System.out.println("Hello from a thread!" + Thread.currentThread().getName());
        //     });
        //     t.start();
          
        // }

        // System.out.println("Hello from the main thread!" + Thread.currentThread().getName());

        ExecutorService executor = Executors.newFixedThreadPool(2);


        // for(int i = 0; i < 5; i++) {
        // executor.submit(() -> {
        //     System.out.println("Hello from a thread!" + Thread.currentThread().getName());
        // });
        // executor.shutdown();


        // List of Employees
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", 50000),
            new Employee("Bob", 60000),
            new Employee("Charlie", 70000),
            new Employee("David", 80000),
            new Employee("Eve", 90000)
        );
        List<Future<Double>> futures = new ArrayList<>();

        // fetch salaries parallel  (api ..delay)
        ExecutorService salaryExecutor = Executors.newFixedThreadPool(3);
        for (Employee emp : employees) {
            Future<Double> future = salaryExecutor.submit(new SalaryFetcher(emp));
             futures.add(future);
            // try {
            //     // Double salary = future.get(); // This will block until the salary is fetched
            //     System.out.println("Salary of " + emp.getName() + ": $" + salary);
            // } catch (Exception e) {
            //     e.printStackTrace();
            // }
        }

        double totalSalary = 0.0;

        for(Future<Double> future : futures) {
            try {
                Double salary = future.get(); // This will block until the salary is fetched
                System.out.println("Fetched Salary: $" + salary);
                totalSalary += salary;
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Total Salary of all employees: $" + totalSalary);
        }
        salaryExecutor.shutdown();
        
        


        // wait for all to complete, then add up salaries and print total

        
        
    }
    }

