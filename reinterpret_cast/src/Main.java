import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class Main {
    static class A {
        long field = 566;
    }

    static class B {
        long field = 30;
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);

        A a = new A();
        System.out.println(unsafe.getLong(a, 0)); // 1 is mark word
        System.out.println(unsafe.getLong(a, 8)); // 16779768 is klass word
        System.out.println(unsafe.getLong(a, 16)); // 566 is a field value

        synchronized (a) {
            System.out.println(unsafe.getLong(a, 0));
            // 132104706844888 -- lock is acquired by this thread
        }
        System.out.println(unsafe.getLong(a, 0)); // 1 again

        Object aObj = a; // save for later
        Object b = new B();
        System.out.println(a.getClass()); // Main$A
        unsafe.putLong(a, 8, unsafe.getLong(b, 8)); // Copy klass word
        System.out.println(a.getClass()); // Main$B

        B itsSoWrong = (B) aObj; // It's B now!
        System.out.println(itsSoWrong.field); // 566 still...
        System.out.println(a.field); // Works as well...
        // It calls A.field, but it's already linked to just "obtain long by offset 16"

        try {
            A thatWasA = (A) (Object) a;
        } catch (ClassCastException e) {
            e.printStackTrace(System.out);
        }
    }
}
