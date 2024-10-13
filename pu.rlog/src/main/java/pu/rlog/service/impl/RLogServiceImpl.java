package pu.rlog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pu.rlog.bo.ClientData;
import pu.rlog.bo.ClientNotFoundException;
import pu.rlog.bo.impl.RLogSocketServer;
import pu.rlog.service.RLogService;

import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.Data;

@Service
@Data
public class RLogServiceImpl implements RLogService
{
@Autowired
private RLogSocketServer rLogSocketServer;

@Override
public List<ClientData> getActiveClients()
{
	return getRLogSocketServer().getClientData();
}

@Override
public ClientData getClient( int aClientId ) throws ClientNotFoundException
{
	return getRLogSocketServer().getClient( aClientId );
}

@Override
public List<ILoggingEvent> getClientLog( int aClientId ) throws ClientNotFoundException
{
	return getRLogSocketServer().getClientLog( aClientId );
}

}
