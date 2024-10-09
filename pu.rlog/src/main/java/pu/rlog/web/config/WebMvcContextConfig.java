package pu.rlog.web.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * Spring MVC configuration
 * 
 */
@Configuration
@EnableWebMvc
@ComponentScan( basePackages = { "pu.rlog.web" } )

public class WebMvcContextConfig implements WebMvcConfigurer
{

	@Override
	public void addResourceHandlers( final ResourceHandlerRegistry registry )
	{
		// Dubbele sterretjes mogen niet meer ??!! Zoals /css/**/* Uit de documentatie:
//		Note: In contrast to org.springframework.util.AntPathMatcher, ** is supported only at the end of a pattern. 
//		For example /pages/{**}} is valid but /pages/{**}/details} is not. The same applies also to the capturing 
//		variant {*spring}. The aim is to eliminate ambiguity when comparing patterns for specificity.
//      // registry.addResourceHandler( "/resources/**/*" ).addResourceLocations( "WEB-INF/web-resources/" );
//		registry.addResourceHandler( "/css/**/*"    ).addResourceLocations( "WEB-INF/web-resources/css/" );
//		registry.addResourceHandler( "/js/**/*"     ).addResourceLocations( "WEB-INF/web-resources/js/" );
//		registry.addResourceHandler( "/images/**/*" ).addResourceLocations( "WEB-INF/web-resources/images/" );
		
		// Dus dan maar 1 nivo diep
		registry.addResourceHandler( "/css/*"    ).addResourceLocations( "WEB-INF/web-resources/css/" );
		registry.addResourceHandler( "/js/*"     ).addResourceLocations( "WEB-INF/web-resources/js/" );
		registry.addResourceHandler( "/images/*" ).addResourceLocations( "WEB-INF/web-resources/images/" );
	}

	@Override
	public void addViewControllers( final ViewControllerRegistry registry )
	{
		// Hiermee kun je snel een zwik simpele pagina's toevoegen
		registry.addViewController( "/index.html" ).setViewName( "index" );
	}

	@Override
	public void configureDefaultServletHandling( final DefaultServletHandlerConfigurer configurer )
	{
		configurer.enable();
	}

	@Override
	public void addInterceptors( InterceptorRegistry registry )
	{
		registry.addInterceptor( localeChangeInterceptor() );
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//-- Start Locale Support (I18N)

	/**
	 * The {@link LocaleChangeInterceptor} allows for the locale to be changed. It provides a <code>paramName</code>
	 * property which sets the request parameter to check for changing the language, the default is <code>locale</code>.
	 * 
	 * @return the {@link LocaleChangeInterceptor}
	 */
	@Bean
	public HandlerInterceptor localeChangeInterceptor()
	{
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName( "lang" );
		return localeChangeInterceptor;
	}

	/**
	 * The {@link LocaleResolver} implementation to use. Specifies where to store the current selected locale.
	 * 
	 * @return the {@link LocaleResolver}
	 */
	@Bean
	public LocaleResolver localeResolver()
	{
		return new CookieLocaleResolver();
	}

	/**
	 * To resolve message codes to actual messages we need a {@link MessageSource} implementation. The default
	 * implementations use a {@link java.util.ResourceBundle} to parse the property files with the messages in it.
	 * 
	 * @return the {@link MessageSource}
	 */
	@Bean
	public MessageSource messageSource()
	{
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename( "classpath:/messages" );
		messageSource.setUseCodeAsDefaultMessage( true );
		return messageSource;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Formatters

	@Override
	public void addFormatters( FormatterRegistry registry )
	{
		//registry.addConverter( categoryConverter() );
		registry.addFormatter( new DateFormatter( "dd-MM-yyyy" ) );
	}

//	@Bean
//	public StringToEntityConverter categoryConverter()
//	{
//		return new StringToEntityConverter( Category.class );
//	}

}
