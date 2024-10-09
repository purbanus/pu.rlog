package pu.rlog.web.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.ServletContextRequestLoggingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import pu.rlog.config.Config;

/**
 * {@link WebApplicationConfig} that will be called by Spring's {@code SpringServletContainerInitializer} as part of the
 * JEE {@code ServletContainerInitializer} pattern. This class will be called on application startup and will configure
 * our JEE and Spring configuration.
 * <p/>
 * 
 * It will first initializes our {@code AnnotationConfigWebApplicationContext} with the common {@link Configuration}
 * classes: {@code InfrastructureContextConfiguration} and {@code TestDataContextConfiguration} using a typical JEE
 * {@code ContextLoaderListener}.
 * <p/>
 * 
 * Next it creates a {@link DispatcherServlet}, being a normal JEE Servlet which will create on its turn a child
 * {@code AnnotationConfigWebApplicationContext} configured with the Spring MVC {@code Configuration} classes
 * {@code WebMvcContextConfiguration} and {@code WebflowContextConfiguration}. This Servlet will be registered using
 * JEE's programmatical API support.
 * <p/>
 * 
 * Note: the {@code OpenEntityManagerInViewFilter} is only enabled for pages served soley via Spring MVC. For pages
 * being served via WebFlow we configured WebFlow to use the JpaFlowExecutionListener.
 * 
 * @author Marten Deinum
 * @author Koen Serneels
 * 
 */
public class WebApplicationConfig implements WebApplicationInitializer
{

	private static final String DISPATCHER_SERVLET_NAME = "rlog";

	@Override
	public void onStartup( ServletContext servletContext ) throws ServletException
	{
//		doLog4j( servletContext );
		registerListener( servletContext );
		registerDispatcherServlet( servletContext );
		registerRequestLoggingFilter( servletContext );
	}

	/*
	<context-param>
		<param-name>log4jConfigLocation</param-name>
		<param-value>/WEB-INF/log4j.xml</param-value>
	</context-param>
	<context-param>
		<param-name>log4jRefreshInterval</param-name>
		<param-value>10000</param-value>
	</context-param>
	<context-param>
		<param-name>webAppRootKey</param-name>
		<param-value>rlog.root</param-value>
	</context-param>
	<listener>
		<listener-class>org.springframework.web.util.Log4jConfigListener</listener-class>
	</listener>

	 */
//	private static void doLog4j( ServletContext aServletContext )
//    {
//		aServletContext.setInitParameter( "log4jConfigLocation", "/WEB-INF/log4j.xml" );
//		aServletContext.setInitParameter( "log4jRefreshInterval", "10000" );
//		aServletContext.setInitParameter( "webAppRootKey", DISPATCHER_SERVLET_NAME + ".root" );
//		// @@NOG Misschien is LogbackConfigListener een idee
//		//aServletContext.addListener( new Log4jConfigListener() );
//    }

	/*
	<context-param>
        <param-name>contextClass</param-name>
        <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
    </context-param>
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>nl.mediacenter.rlog.config.Config</param-value>
    </context-param>
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>
	 */
	private static void registerListener( ServletContext servletContext )
	{
		// Dit is voor hun database, datasources, transacties etc.
		// rootContext = createContext( InfrastructureContextConfiguration.class, TestDataContextConfiguration.class );
		@SuppressWarnings( "resource" )
		AnnotationConfigWebApplicationContext rootContext = createContext( Config.class );
		servletContext.addListener( new ContextLoaderListener( rootContext ) );

	}
	/*
 	<!-- Servlet voor interactieve tests -->
	<servlet>
		<servlet-name>rlog</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
            <param-name>contextClass</param-name>
            <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
        </init-param>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>nl.mediacenter.rlog.config.MvcConfig</param-value>
        </init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>rlog</servlet-name>
		<url-pattern>/*</url-pattern>
	</servlet-mapping>
	 */
	private static void registerDispatcherServlet( ServletContext servletContext )
	{
		@SuppressWarnings( "resource" )
		AnnotationConfigWebApplicationContext dispatcherContext = createContext( WebMvcContextConfig.class );
		ServletRegistration.Dynamic dispatcher;
		dispatcher = servletContext.addServlet( DISPATCHER_SERVLET_NAME, new DispatcherServlet( dispatcherContext ) );
		dispatcher.setLoadOnStartup( 1 );
		dispatcher.addMapping( "/" );
	}
	/**
	 * Factory method to create {@link AnnotationConfigWebApplicationContext} instances.
	 * 
	 * @param annotatedClasses
	 * @return
	 */
	private static AnnotationConfigWebApplicationContext createContext( final Class<?>... annotatedClasses )
	{
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register( annotatedClasses );
		return context;
	}

	/*
 	<filter>
		<filter-name>Request Logging Filter</filter-name>
		<filter-class>org.springframework.web.filter.ServletContextRequestLoggingFilter</filter-class>
	</filter>
	<filter-mapping>
		<filter-name>Request Logging Filter</filter-name>
		<servlet-name>rlog</servlet-name>
	</filter-mapping>

	 */
	private static void registerRequestLoggingFilter( ServletContext aServletContext )
    {
		ServletContextRequestLoggingFilter filter = new ServletContextRequestLoggingFilter();
		filter.setBeforeMessagePrefix( "Pipo before request[" );
		aServletContext.addFilter( "Request Logging Filter", filter );
    }


}
