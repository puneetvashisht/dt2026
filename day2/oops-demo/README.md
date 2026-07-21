# OOP Concepts Demo: Polymorphism & Sealed Classes

This project demonstrates core Object-Oriented Programming (OOP) concepts in modern Java (Java 17+), using an Employee Payroll system.

---

## Codebase Structure

- **Base Class**: [Employee.java](file:///Users/puneet/work/deutsche-2026/TrainersGuide/day2/oops-demo/src/Employee.java) (Abstract Sealed class)
- **Subclass 1**: [RegularEmployee.java](file:///Users/puneet/work/deutsche-2026/TrainersGuide/day2/oops-demo/src/RegularEmployee.java) (Final class representing salaried staff)
- **Subclass 2**: [ContractEmployee.java](file:///Users/puneet/work/deutsche-2026/TrainersGuide/day2/oops-demo/src/ContractEmployee.java) (Final class representing hourly staff)
- **Runner**: [App.java](file:///Users/puneet/work/deutsche-2026/TrainersGuide/day2/oops-demo/src/App.java) (Demonstrates polymorphic execution)

---

## 1. Polymorphism (via Inheritance)

### The Core Concept
Inheritance establishes an **"IS-A"** relationship (e.g., `RegularEmployee` **is a** `Employee`). This relationship enables **Subtype Polymorphism** (runtime polymorphism).

### The "Side Benefit"
While inheritance is often used for code reuse (sharing the common `id` and `name` fields), its ultimate benefit is **polymorphic behavior**. 

- **Without Polymorphism**: You would need to check the specific type of each object manually (e.g., using `instanceof`) and cast it to calculate salaries differently. This violates the **Open-Closed Principle (OCP)** because introducing a new employee type forces you to update those conditionals everywhere.
- **With Polymorphism**: You can treat all subclasses uniformly via their superclass reference (`Employee`). At runtime, Java's dynamic binding executes the overridden version of `calculateSalary()` defined by the actual object:

```java
for (Employee emp : employees) {
    // Dynamic dispatch automatically calls the correct subclass method
    double salary = emp.calculateSalary(); 
}
```

---

## 2. Sealed Types in Java

In Java 17+, **Sealed Classes** allow you to restrict which classes are permitted to extend them. 

### Why Sealed Classes?
Before sealed classes, a class could either be `final` (cannot be extended at all) or `public` (open to extension by anyone). Sealed classes offer a middle ground by allowing a class to declare a restricted set of permitted subclasses.

In this project, we explicitly permit only `RegularEmployee` and `ContractEmployee`:
```java
public abstract sealed class Employee permits RegularEmployee, ContractEmployee { ... }
```

### Constraints on Subclasses
Any subclass extending a sealed class must use one of the following modifiers:
- `final`: No further subclassing is allowed (e.g., `public final class RegularEmployee extends Employee`).
- `sealed`: Subclassing is allowed but restricted to permitted types.
- `non-sealed`: The class is fully opened for extension by any other class.

### Benefit: Compile-Time Exhaustiveness
Sealed classes allow Java's pattern matching switch statements to be exhaustive, eliminating the need for a catch-all `default` branch:

```java
double bonus = switch (emp) {
    case RegularEmployee r -> r.getBonus();
    case ContractEmployee c -> 0.0;
};
```
If you add a new subclass to `permits`, the compiler will immediately alert you to any `switch` statements that need updating.

---

## Running the Project

To compile and run the application from the command line:

```bash
javac -d bin src/*.java
java -cp bin App
```
