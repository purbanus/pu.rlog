package pu.rlog.bo;

public class ClientNotFoundException extends Exception
{
/**
 * Creates a new ClientNotFoundException with the specified client id.
 */
public ClientNotFoundException( Integer aClientID )
{
	super( "Client not found: " + aClientID );
}
}
