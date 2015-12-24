package work;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import vaadin.commons.ui.components.container.ContainerDelegate;
import vaadin.commons.ui.components.container.SpringDataContainerDelegate;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class VaadinTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaadinTestApplication.class, args);
    }

    @Bean
    public ContainerDelegate springDataAdaptor(){
        return new SpringDataContainerDelegate();
    }
}
