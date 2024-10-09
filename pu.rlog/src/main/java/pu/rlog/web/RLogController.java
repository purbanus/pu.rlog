package pu.rlog.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import pu.rlog.bo.ClientData;
import pu.rlog.bo.ClientNotFoundException;
import pu.rlog.service.RLogService;

import ch.qos.logback.classic.spi.LoggingEventVO;
import lombok.Data;

/**
 * Handles requests for the application home page.
 */
@Controller
@Data
public class RLogController
{
	
	private static final Logger logger = LoggerFactory.getLogger(RLogController.class);
	
	@Autowired
	private RLogService rLogService;
	
	@RequestMapping(value = { "/active.html", "/active", "/" }, method = RequestMethod.GET)
	public ModelAndView active()
	{
		logger.info( "Active logs" );

		List<ClientData> clients = getRLogService().getActiveClients();
		Map<String, Object> model = new HashMap<>();
		model.put( "clients", clients );
		return new ModelAndView( "active", model );
	}
	@RequestMapping(value = "/client.html", method = RequestMethod.GET)
	public ModelAndView client( @RequestParam( "id" ) int aClientId ) throws ClientNotFoundException
	{
		logger.info( "Log van client " + aClientId );

		try
		{
			List<LoggingEventVO> log = getRLogService().getClientLog( aClientId );
			//String log = HtmlUtils.htmlEscape( rawLog );
			
			Map<String, Object> model = new HashMap<>();
			model.put( "client", getRLogService().getClient( aClientId ) );
			model.put( "log", log );
			model.put( "isError", false );
			return new ModelAndView( "client", model );
		}
		catch ( ClientNotFoundException e )
		{
			Map<String, Object> model = new HashMap<>();
			model.put( "clientId", aClientId );
			model.put( "isError", true );
			model.put( "error", "Client " + aClientId + " not found" );
			return new ModelAndView( "client", model );
		}
	}
	
}
