package vaadin.commons.ui.components.container;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContainerDelegateAutoConfiguration {

    @Bean
    public ContainerDelegate containerDelegate(){
        return new SpringDataContainerDelegate();
    }
}
