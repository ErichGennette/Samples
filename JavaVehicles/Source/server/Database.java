package server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import common.NetworkVehicle;

/**
 * The Database class manipulates an Object DB
 * @author Erich
 *
 */
public class Database {
    Properties properties;
    PersistenceManagerFactory pmf;
    PersistenceManager pm;

    /**
     * The constructor creates a data file named with the given string. It also sets up the properties of the DB to use the objectdb driver.
     * @param db_name the name of the file that the DB data is saved.
     */
    public Database(String db_name) {
        try {
            properties = new Properties();
            properties.setProperty("javax.jdo.PersistenceManagerFactoryClass", "com.objectdb.jdo.PMF");
            properties.setProperty("javax.jdo.option.ConnectionURL", db_name);
            pmf = JDOHelper.getPersistenceManagerFactory(properties);
            pm = pmf.getPersistenceManager();
        } catch (Exception e) {
            System.err.println("error connecting to DB " + e.toString());
        }
    }

    /**
     * This method returns a List of Objects created by the data in the db file.
     * @param aClass The type of class the db should return
     * @return a list of objects requested from the db.
     */
    List<Object> getObjects(Class aClass) {
        List<Object> results;
        results = new ArrayList<>();
        Extent extent = pm.getExtent(aClass, true);
        Iterator itr = extent.iterator();
        while (itr.hasNext()) {
            Object p = (Object) itr.next();
            results.add(p);
        }
        extent.closeAll();

        return results;
    }

    /**
     * The saveNew method saves the given object to the database
     * @param obj An object to be saved to the database
     */
    public void saveNew(Object obj) {
        try {
            pm.currentTransaction().begin();
            pm.makePersistent(obj);
            pm.currentTransaction().commit();
        } finally {
            if (pm.currentTransaction().isActive())
                pm.currentTransaction().rollback();
        }
    }
    
    /**
     * The clear method completely wipes all data from the database.
     */
    public void clear() {
    	List<Object> currentDB = getObjects(NetworkVehicle.class);
    	for(Object o: currentDB) {
	    	if(o != null) {
	    		try {
		    		pm.currentTransaction().begin();
		    		pm.deletePersistent(o);
		    		pm.currentTransaction().commit();
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	    	}
    	}
    }

    /**
     * The close method closes the persistence manager so that something else can access the db later if needed.
     */
    void close() {
        if (pm.currentTransaction().isActive())
            pm.currentTransaction().rollback();
        if (!pm.isClosed())
            pm.close();
    }
}
