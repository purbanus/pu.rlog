package pu.rlog.bo.impl;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pu.rlog.bo.ClientData;
import pu.rlog.bo.SimpleSocketServer;
import pu.rlog.bo.SocketNode;
import pu.services.buffers.TailBuffer;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode( callSuper = false )
public class RLogSocketNode extends SocketNode
{
public static final String USER_NAME = "user.name";
public static final int HISTORY_BUFFER_INITIAL_SIZE = 100;
public static final int HISTORY_BUFFER_MAXIMUM_SIZE = 10000;

@ToString.Exclude
private final ClientData clientData;
@ToString.Exclude
private final TailBuffer<ILoggingEvent> historyBuffer = new TailBuffer<>( ILoggingEvent.class,HISTORY_BUFFER_MAXIMUM_SIZE, HISTORY_BUFFER_INITIAL_SIZE );

	public RLogSocketNode( SimpleSocketServer aSocketServer, Socket aSocket, LoggerContext aContext )
	{
		super( aSocketServer, aSocket, aContext );
		clientData = ClientData.builder()
			.clientId( ( (RLogSocketServer) aSocketServer ).getNextClientId() )
			.SocketNode( this )
			.timeStarted( new Date() )
			.timeLastActivity( new Date() )
			.user( "Unknown" )
			.build();
	}
	public int getClientId()
	{
		return clientData.getClientId();
	}
	InetAddress getInetAddress()
	{
		return getSocket().getInetAddress();
	}
	String getIpAddress()
	{
		return getInetAddress().getHostAddress();
	}
	String getHostName()
	{
		return getInetAddress().getHostName();
	}
	synchronized ClientData getClientData()
	{
		if ( clientData.getIpAddress() == null )
		{
			clientData.setIpAddress( getIpAddress() );
		}
		if ( clientData.getHostName() == null )
		{
			clientData.setHostName( getHostName() );
		}
		clientData.setHistorySize( getHistory().size() );
		return clientData;
	}
	synchronized List<ILoggingEvent> getHistory()
	{
		ILoggingEvent [] loggingEvents = getHistoryBuffer().get();
		return Arrays.asList( loggingEvents );
	}
	@Override
	protected synchronized void preAppend( ILoggingEvent aEvent )
	{
		String message = aEvent.getMessage();
		if ( message.startsWith( USER_NAME ) )
		{
			clientData.setUser( message.substring( USER_NAME.length() ).trim() );
		}
	}
	@Override
	protected void doAppend( ILoggingEvent aEvent )
	{
		getHistoryBuffer().put( aEvent );
	}

}
