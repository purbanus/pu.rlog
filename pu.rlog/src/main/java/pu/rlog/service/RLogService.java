package pu.rlog.service;

import java.util.List;

import pu.rlog.bo.ClientData;
import pu.rlog.bo.ClientNotFoundException;

import ch.qos.logback.classic.spi.LoggingEventVO;

public interface RLogService
{
public abstract List<ClientData> getActiveClients();
public abstract ClientData getClient( int aClientId ) throws ClientNotFoundException;
public abstract List<LoggingEventVO> getClientLog( int aClientId ) throws ClientNotFoundException;
}
