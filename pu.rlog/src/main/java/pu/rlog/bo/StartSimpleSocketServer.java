package pu.rlog.bo;

import java.io.File;

import ch.qos.logback.classic.net.SimpleSocketServer;

public class StartSimpleSocketServer
{
/**
 * java ch.qos.logback.classic.net.SimpleSocketServer 6000 \
  src/main/java/chapters/appenders/socket/server1.xml
 * @throws Exception 
 */

public static void main( String [] args ) throws Exception
{
	String path = "src/main/resources/logback-server.xml";
//	URL url = StartSimpleSocketServer.class.getResource( path );
	File file = new File( path);
	if ( ! file.exists() )
	{
		System.err.println( "File bestaat niet!" );
	}
	else
	{
		String absolutePath = file.getAbsolutePath();
		System.out.println( "Absolute path=" + absolutePath );
		args = new String []{ "40000", absolutePath } ;
		SimpleSocketServer.main( args );
	}

}

}
