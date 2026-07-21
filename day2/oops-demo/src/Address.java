public class Address {
    
    //fields

    private String street;
    private String city;
    private String state;
    private String zipCode;

    //constructor
    public Address(String street, String city, String state) {
        this.street = street;
        this.city = city;
        this.state = state;
        // this.zipCode = zipCode;
    }

    //getters and setters
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    //methods
    public void addHouseNumber(String houseNumber) {
        this.street = houseNumber + " " + this.street;
    }
    
    
    @Override
    public String toString() {
        return street + ", " + city + ", " + state + " " + zipCode;
    }


    // main method for testing
    public static void main(String[] args) {
        Address address = new Address("123 Main St", "Springfield", "IL");
        address.setZipCode("62701");
        address.addHouseNumber("456");
        System.out.println(address);    
    }


}
