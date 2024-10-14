/**
 * 
 */
package pu.rlog.bo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pu.rlog.bo.impl.RLogSocketServer;

import lombok.Data;

@Data
@Service
public class CleanupClientsJob implements Runnable
{
private static final Logger LOG = LoggerFactory.getLogger( CleanupClientsJob.class );

@Autowired
private RLogSocketServer socketServer = null;

//////////////////////////////////////////////////////////////////////////////////////////////////
// Business

@Override
@Scheduled( cron="${cleanupClients.cron}" )
public void run()
{
	LOG.info( "Start cleanup clients" );
	int aantalKilled = 0;
	List<ClientData> clients = getSocketServer().getClientData();
	for ( ClientData clientData : clients )
	{
		SocketNode socketNode = clientData.getSocketNode();
        getSocketServer().socketNodeClosing( socketNode );
        socketNode.close();
		++aantalKilled;
	}
	LOG.info( "Aantal clients killed: " + aantalKilled );
}

}
