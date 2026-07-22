package lambdasdemo;

@FunctionalInterface
interface Greeting {
    void sayHello(String name);
}

class JapaneseGreeting implements Greeting {
    @Override
    public void sayHello(String name) {
        System.out.println("Konichiwa! " + name + "!");
    }
}

class EnglishGreeting implements Greeting {
    @Override
    public void sayHello(String name) {
        System.out.println("Hello! " + name + "!");
    }
}

public class LamdasDemo {
    
    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("     EVOLUTION OF CODE CONCISENESS (SUBCLASS -> LAMBDA)   ");
        System.out.println("==========================================================\n");

        // ------------------------------------------------------------
        // APPROACH 1: Concrete Subclass (Requires declaring a separate class)
        // ------------------------------------------------------------
        Greeting japaneseGreeting = new JapaneseGreeting();
        japaneseGreeting.sayHello("Taro");

        // ------------------------------------------------------------
        // APPROACH 2: Anonymous Inner Class (No separate class declaration, but high boilerplate)
        // ------------------------------------------------------------
        Greeting englishGreeting = new Greeting() {
            @Override
            public void sayHello(String name) {
                System.out.println("Hello! " + name + "!");
            }
        };
        englishGreeting.sayHello("John");

        // ------------------------------------------------------------
        // APPROACH 3: Lambda Expression (Concise, focus purely on behavior)
        // ------------------------------------------------------------
        
         
        Greeting spanishGreeting = name -> System.out.println("Hola! " + name + "!");
        spanishGreeting.sayHello("Maria");
    }
}
