package team16.cs261.backend;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
//import team16.cs261.backend.config.SpringExtension;

/**
 * Created by martin on 13/02/15.
 */
public class Main {

    public static String[] args;

    public static void main(String... args) {
        Main.args=args;

        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/backend.xml");
        ctx.getBean("backend");

        // get hold of the actor system
        //ActorSystem system = ctx.getBean(ActorSystem.class);
        // use the Spring Extension to create props for a named actor bean
        //ActorRef counter = system.actorOf(
                //SpringExtension.SpringExtProvider.get(system).props("parser"), "parser");


        //counter.tell("message", null);
    }



}
