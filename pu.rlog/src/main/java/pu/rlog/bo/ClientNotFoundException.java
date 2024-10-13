package pu.rlog.bo;

public class ClientNotFoundException extends Exception
{
/**
 * Creates a new ClientNotFoundException with the specified client id.
 */
public ClientNotFoundException( int aClientId )
{
	super( "Client not found: " + aClientId );
}
/**
 * Creates a new ClientNotFoundException with the specified SocketNode.
 */
public ClientNotFoundException( SocketNode aSocketNode )
{
	super( "Client not found: " + aSocketNode );
}
}
