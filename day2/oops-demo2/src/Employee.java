public abstract sealed class Employee extends Object permits RegularEmployee, ContractEmployee {

    // public abstract class Employee{
    private String name;
    private int id;

    Address address;
    public Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }


    // Abstract method to be implemented by subclasses
    public abstract double calculateSalary();

    @Override
    public String toString() {
        return String.format("Employee[ID=%d, Name=%s]", id, name);
    }
    
    // @Override
    // public int hashCode() {
    //     final int prime = 31;
    //     int result = 1;
    //     result = prime * result + id;
    //     return result;
    // }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Employee other = (Employee) obj;
        if (id != other.id)
            return false;
        return true;
    }   


    
    //
}
