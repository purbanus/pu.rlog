package pu.rlog.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import pu.rlog.bo.ClientData;
import pu.rlog.bo.ClientNotFoundException;
import pu.rlog.service.RLogService;

import ch.qos.logback.classic.spi.LoggingEventVO;

@Service
public class RLogServiceImpl implements RLogService
{

@Override
public List<ClientData> getActiveClients()
{
	ClientData [] clientData = {
		new ClientData( 1, "192.168.178.50", new Date(), new Date(), 65536, "peter" ),
		new ClientData( 2, "192.168.178.55", new Date(), new Date(), 32767, "purbanus" ),
	};
	return Arrays.asList( clientData );
}

@Override
public ClientData getClient( int aClientId ) throws ClientNotFoundException
{
	// TODO Auto-generated method stub
	return null;
}

@Override
public List<LoggingEventVO> getClientLog( int aClientId ) throws ClientNotFoundException
{
	// TODO Auto-generated method stub
	return null;
}

}
