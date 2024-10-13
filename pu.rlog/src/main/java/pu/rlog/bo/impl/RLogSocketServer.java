package pu.rlog.bo.impl;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import pu.rlog.bo.ClientData;
import pu.rlog.bo.ClientNotFoundException;
import pu.rlog.bo.SimpleSocketServer;
import pu.rlog.bo.SocketNode;
import pu.services.Util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode( callSuper = false )
public class RLogSocketServer extends SimpleSocketServer implements InitializingBean, DisposableBean
{
public static int nextClientId = 1;

private final String logDir;
public RLogSocketServer( LoggerContext aLoggerContext, int aPort, String aLogDir )
{
	super( aLoggerContext, aPort );
	logDir = aLogDir;
}
@Override
public void afterPropertiesSet() throws Exception
{
	// Wacht tot de connectionListener gestart is
//	while ( ! isConnectionListenerStarted() )
	{
		Thread.sleep( 2000 );
	}
	// @@NOG Je moet hier de system properties loggen naar remote, zodat ze in het client log terechtkomen.
	// Waarschijnlijk is het het simpelst om hier programmatisch eeen remote logger te maken.
//	SimpleRemoteLogStarter.start();
	Util.printClassPath();
}
@Override
public void destroy() throws Exception
{
	close();
}
public int getNextClientId()
{
	return nextClientId++;
}
private synchronized List<SocketNode> getSocketNodesCopy()
{
	return new ArrayList<>( getSocketNodeList() );
}
protected Map<Integer, RLogSocketNode> getSocketNodeMap()
{
	Map<Integer, RLogSocketNode> socketNodeMap = new HashMap<>();
	for ( SocketNode client : getSocketNodeList() )
	{
		RLogSocketNode socketNode = (RLogSocketNode) client;
		socketNodeMap.put( socketNode.getClientId(), socketNode );
	}
	return socketNodeMap;
}
/**
 * Returns a list of the current client connections, each list entry containing
 * the data of one client in a <code>ClientData</code> object. Be aware that
 * between retrieving this list and using the information, clients may have disconnected.
 * when clients disconnect, RLogServer immediately deletes them from its internal tables.
 * Subsequent queries using id's of disconnected clients will fail with a
 * <code>ClientNotFoundException</code>.
 */
public List<ClientData> getClientData()
{
	// You could make the synchronization difficult, and I don't want that.
	// So we synchronize just long enough to create a copy of the client map.
	// After that, we iterate over the clients, and it's acceptable that the
	// information is just a little bit stale, in the sense that between querying
	// client 0 and client n, clients < n might have changed a little.
	// The effect is that the list we produce is not an atomic snapshot, but that is
	// not required.
	List<SocketNode> tempCopy = getSocketNodesCopy(); // Synchronized
	
	// Create a list with more verbose info
	List<ClientData> clientData = new ArrayList<>( tempCopy.size() );
	for ( SocketNode client : tempCopy )
	{
		clientData.add( ( (RLogSocketNode)client ).getClientData() );
	}
	
	clientData.sort( Comparator.comparing( ClientData::getIpAddress ) );
	return clientData;
}
/**
 * Returns a client given its id.
 * 
 * @param aClientID The ID if the client.
 * @exception ClientNotFoundException If the ID was not found. This probably means that the client
 *            has disconnected.
 */
public ClientData getClient( int aClientId ) throws ClientNotFoundException
{
	RLogSocketNode client = null;
	synchronized( this )
	{
		client = getSocketNodeMap().get( aClientId );
	}
	if ( client == null )
	{
		throw new ClientNotFoundException( aClientId );
	}
	return client.getClientData();
}

/**
 * Returns a snapshot of a client's log.
 * 
 * @param aSocketNode The ID if the client to obtain the log from. This is probably obtained from a
 *                  <code>getClientData</code> call.
 * @exception ClientNotFoundException If the ID was not found. This probably means that the client
 *            has disconnected.
 */
public List<ILoggingEvent> getClientLog( int aClientID ) throws ClientNotFoundException
{
	RLogSocketNode client = null;
	synchronized( this )
	{
		client = getSocketNodeMap().get( aClientID );
	}
	if ( client == null )
	{
		throw new ClientNotFoundException( aClientID );
	}
	return client.getHistory();
}

@Override
protected RLogSocketNode createSocketNode( SimpleSocketServer aSimpleSocketServer, Socket aSocket, LoggerContext aLc )
{
	return new RLogSocketNode( aSimpleSocketServer, aSocket, aLc );
}

}
