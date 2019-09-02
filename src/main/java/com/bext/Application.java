package com.bext;

import javax.jms.ConnectionFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;


@SpringBootApplication
@EnableJms
public class Application {

	@Bean
	public JmsListenerContainerFactory<?> miFactory(ConnectionFactory connectionFactory,
													DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		// esto provee todos los default a factory, incluyendo el convertidor de mensajes
		configurer.configure(factory, connectionFactory);
		// se pueden sobreescribir los default de ser necesario.
		return factory;
	}

	@Bean  //Serializa el contenido del mensaje a json usando TextMessage
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}
	
	public static void main(String[] args) {
		// corre la app
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		
		//Envia un mensaje con un POJO
		System.out.println("Enviando un mensaje email");
		jmsTemplate.convertAndSend("mailbox", new Email("correo@servidor.com","Contenido del Mensaje"));
	}

}
