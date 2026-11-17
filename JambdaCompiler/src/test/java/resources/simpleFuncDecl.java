class SimpleFuncDecl {

    SimpleFuncDecl(){}

    public static int myFunction1(int a, int b) {
        return a;
    }

    public static int myFunction2(int a, int b, int c) {
        return a;
    }

    public static int myFunction3(int a, int b, int c) {
        return a;
    }

    public static int main() {
        myFunction3(10,11,12);
        return 0;
    }

    public static void main (String [] args) {
        main();
    }

}