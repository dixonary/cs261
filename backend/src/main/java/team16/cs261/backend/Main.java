package team16.cs261.backend;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by martin on 13/02/15.
 */
public class Main {

    public static void main(String... args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/backend.xml");
        context.getBean("backend");
    }

}
