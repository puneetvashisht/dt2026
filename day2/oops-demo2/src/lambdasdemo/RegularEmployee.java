package lambdasdemo;
public final class RegularEmployee extends Employee {
    private double monthlySalary;
    private double bonus;

    public double getBonus() {
        return bonus;
    }

    public RegularEmployee(String name, int id, double monthlySalary, double bonus) {
        super(name, id);
        this.monthlySalary = monthlySalary;
        this.bonus = bonus;
    }

    @Override
    public double calculateSalary() {
        return monthlySalary + bonus;
    }
}
