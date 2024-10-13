package pu.rlog.config;

import java.io.File;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import pu.rlog.bo.SimpleSocketServer;
import pu.rlog.bo.impl.RLogSocketServer;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.joran.spi.JoranException;

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

	@Value( "${rlog.logdir}" )  private String logDir;
	@Value( "${rlog.logport}" )	private int logPort;
	@Value( "${rlog.configFile}" ) private String configFile;

	@Bean
	public RLogSocketServer rLogSocketServer() throws JoranException
	{
//		URL url = StartSimpleSocketServer.class.getResource( path );
		File file = new File( configFile );
		if ( ! file.exists() )
		{
			System.err.println( "File bestaat niet!" );
			throw new RuntimeException( "ConfigFile " + configFile + " bestaat niet !!!" );
		}
		String configFileAbsolutePath = file.getAbsolutePath();
		System.out.println( "Absolute path van configFile=" + configFileAbsolutePath );

		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
	    SimpleSocketServer.configureLC( loggerContext, configFileAbsolutePath );

	    RLogSocketServer socketServer = new RLogSocketServer( loggerContext, logPort, logDir );
	    socketServer.start();

		return socketServer;
	}
	
}
