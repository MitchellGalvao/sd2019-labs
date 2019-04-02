package impl.clt.rest;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

public class DownloadMediaClient {
	private static final int READ_TIMEOUT = 5000;
	private static final int CONNECT_TIMEOUT = 2000;
	final static int RETRY_PERIOD = 1000;

	public static void main(String[] args) throws IOException {
		
		ClientConfig config = new ClientConfig();
		
		config.property(ClientProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
		config.property(ClientProperties.READ_TIMEOUT, READ_TIMEOUT);

		Client client = ClientBuilder.newClient(config);

		URI baseURI = UriBuilder.fromUri("http://localhost:9999/rest/media").build();
		WebTarget target = client.target( baseURI );
		
		for(;;)
		    try {
		    	Response r = target.request()
				.accept(MediaType.APPLICATION_OCTET_STREAM)
				.get();

		    	if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() )
		    		System.out.println("Response: " + (r.readEntity(byte[].class)).length);
		    	else
		    		System.out.println("Status: " + r.getStatus() );

		    	break;
		    		} catch( ProcessingException pe ) {
		    			try {
		    				Thread.sleep( RETRY_PERIOD );
		    			} catch (InterruptedException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		    		}

	    }

}
