package pu.rlog.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
 * Reden om de properties in een ouderwetse XML laden:
 * - Er zit een bug in Spring waardoor je minstens ��n propertyfile in XML moet laden, anders laadt hij niets
 */

@Configuration
@ComponentScan(basePackages = { "pu.rlog.bo", "pu.rlog.config", "pu.rlog.service" } )
@Import( MailConfig.class ) // @@NOG Is dit nu nodig of vindt die component-scan hem vanzelf ook wel?
@EnableScheduling
public class Config
{
    @Configuration
    @Profile(value = "local")
    @ImportResource( "classpath:/local/IntegrationTestContext.xml" )
    //@PropertySource( "classpath:/local/scheduling.properties" )
    public static class LocalConfiguration
    {
    	// Dit is een heuse @Configuration class dus je kunt er beans inzetten etc
    	// Bijvoorbeeld een simpele DataSource
    }
    @Configuration
    @Profile(value = "!local")
    @ImportResource( "WEB-INF/ApplicationContext.xml" )
    //@PropertySource( "WEB-INF/scheduling.properties" )
    public static class NonLocalConfiguration
    {
    	// Dit is een heuse @Configuration class dus je kunt er beans inzetten etc
    	// Bijvoorbeeld een complexe DataSource
    }

//	@Value( "${rlog.logdir}" )  private String logDir;
//	@Value( "${rlog.logport}" )	private int logPort;
//	@Bean
//	public RLogServer rlogServer()
//	{
//		RLogServer server = new RLogServer(  );
//		server.setLogDir( logDir );
//		server.setPort( logPort );
//		return server;
//	}
	
}
