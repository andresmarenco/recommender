package recommender.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ConnectionManager {
	
	public static final String URL_KEY = "recommender.url";
	public static final String DRIVERNAME_KEY = "recommender.driverName";
	public static final String USER_KEY = "recommender.user";
	public static final String PASSWORD_KEY = "recommender.password";
	public static final String CONNECTION_SOURCE_KEY = "recommender.connectionSource";
	public static final String DEFAULT_DATASOURCE_KEY = "jdbc/Recommender";
	

	/** Current instance of the class for the Singleton Pattern */
	private static ConnectionManager _ClassInstance;
	
	private ConnectionManagerSource _Connection;
	
	/**
	 * Default Constructor
	 */
	private ConnectionManager() {
		String sourceValue = System.getProperty(CONNECTION_SOURCE_KEY);
		ConnectionSource source = (sourceValue != null) ? ConnectionSource.valueOf(sourceValue) : ConnectionSource.SERVLET_CONTAINER;
		
		
		switch(source) {
		case DIRECT: {
			_Connection = new DirectConnectionManager();
			break;
		}
		
		case SERVLET_CONTAINER: {
			_Connection = new ServletConnectionManager();
			break;
		}
		}
	}
	
	
	
	/**
	 * Gets a connection to the Database.
	 * All the connections should be closed after use.
	 * @return Connection
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		return _Connection.getConnection();
	}
	
	
	
	
	/**
     * Returns an instance of the class
     * @return Singleton instance of the class
     */
    public synchronized static ConnectionManager getInstance()
    {
        try
		{
		    if(_ClassInstance == null)
		    {
			    _ClassInstance = new ConnectionManager();    
		    }
		    return _ClassInstance;
		}
		catch(Exception ex)
		{
            ex.printStackTrace();
		    return null;
		}
    }
    
    
    
    
    /**
     * The Clone method is not supported due the Singleton Pattern
     * @return Always throws an exception
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
         throw new CloneNotSupportedException();
    }
    
    
    
    /**
     * Interface for Connections Abstract Factory
     * @author andres
     *
     */
    private static interface ConnectionManagerSource {
    	public Connection getConnection() throws SQLException;
    }
    
    
    
    
    /**
     * Pooled Connection to the Database
     * @author andres
     *
     */
    private static class ServletConnectionManager implements ConnectionManagerSource {
    	private DataSource _DataSource;
    	
    	public ServletConnectionManager() {
    		try
    		{
    			InitialContext context = new InitialContext();
    			_DataSource = (DataSource) context.lookup("java:/comp/env/" + DEFAULT_DATASOURCE_KEY);
    		}
    		catch(Exception ex) {
    			_DataSource = null;
    			ex.printStackTrace();
    		}
    	}
    	
    	public Connection getConnection() throws SQLException {
    		return _DataSource.getConnection();
    	}
    }
    
    
    
    
    /**
     * Direct Connection to the Database
     * @author andres
     *
     */
    private static class DirectConnectionManager implements ConnectionManagerSource {
    	private final String url;
    	private final String driverName;
    	private final String user;
    	private final String password;
    	
    	public DirectConnectionManager() {
    		this.url = System.getProperty(ConnectionManager.URL_KEY);
    		this.driverName = System.getProperty(ConnectionManager.DRIVERNAME_KEY);
    		this.user = System.getProperty(ConnectionManager.USER_KEY);
    		this.password = System.getProperty(ConnectionManager.PASSWORD_KEY);
    		
    		try {
				Class.forName(this.driverName);
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
    	}
    	
    	public Connection getConnection() throws SQLException {
    		return DriverManager.getConnection(url, user, password);
    	}
    }
	
    
    
    
    /**
     * Source of the Connection Information
     * @author andres
     *
     */
	public static enum ConnectionSource {
		SERVLET_CONTAINER,
		DIRECT
	}
}
