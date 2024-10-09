package pu.rlog.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * Spring MVC configuration for the View Technologies.
 * 
 * @author Marten Deinum
 * @author Koen Serneels
 * 
 */
@Configuration
public class ViewConfiguration
{
	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer()
	{
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		freeMarkerConfigurer.setTemplateLoaderPath( "/WEB-INF/views/" );
		return freeMarkerConfigurer;
	}

	@Bean
	public ViewResolver velocityViewResolver()
	{
		FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
	    viewResolver.setCache(true); 
	    viewResolver.setPrefix(""); 
		viewResolver.setSuffix( ".ftlh" );
//		viewResolver.setDateToolAttribute( "dateTool" );
//		viewResolver.setNumberToolAttribute( "numberTool" );
		viewResolver.setExposeSpringMacroHelpers( true );
		
		return viewResolver;
	}

}
