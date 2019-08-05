package TucLoA;

import java.io.IOException;

/**
 * Created by costi_000 on 5/4/2016.
 */
public class Main {


    public static void main(String... args) throws IOException {
        System.out.println("takis");
        //Communication comm = new Communication("192.168.43.113",6001);
        Client cl = new Client();

        cl.run();

    }
}
