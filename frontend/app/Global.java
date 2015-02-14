import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.ApplicationContext;
import play.GlobalSettings;
import play.Application;

import config.AppConfig;
import config.DalConfig;

public class Global extends GlobalSettings {

    private ApplicationContext ctx;

    @Override
    public void onStart(Application app) {
        ctx = new AnnotationConfigApplicationContext(AppConfig.class, DalConfig.class);
    }

    @Override
    public <A> A getControllerInstance(Class<A> clazz) {
        return ctx.getBean(clazz);
    }

}