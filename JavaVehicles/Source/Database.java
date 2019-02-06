package display2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class Database {
	// Manager for a database connection:
    Properties properties;
    PersistenceManagerFactory pmf;
    PersistenceManager pm;

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

    List<Object> getObjects(Class aClass) {

        List<Object> results;
        results = new ArrayList<>();

        // include instances of subclasses ... works using either true OR false?
//        Extent extent = pm.getExtent(tempClass, false);
        Extent extent = pm.getExtent(aClass, true);

        // Iterate, picking up all records and adding to list
        Iterator itr = extent.iterator();
        while (itr.hasNext()) {
            Object p = (Object) itr.next();
            results.add(p);
            //System.out.println(p);  //too many prints with thisa one
        }
        extent.closeAll();

        return results;
    }

    public void dumpObjects(Class aClass) {

        // Get records
        System.out.println("...");
        System.out.println("Dumping data ..." + aClass.getName());

        List<Object> results = this.getObjects(aClass);

        System.out.println("Dump all Records of class => " + aClass.getName());
        for (Object p : results) {
            System.out.println(aClass.getName() + "=> " + p.toString());
        }
    }

    public void saveNew(Object obj) {
    	List<Object> dbObjects = getObjects(DBVehicle.class);
    	Object sameVehicle = vehicleExists(obj, dbObjects);
    	if(sameVehicle != null) {
    		pm.currentTransaction().begin();
    		pm.deletePersistent(sameVehicle);
    		pm.currentTransaction().commit();
    	}
    	
        // Make persistent
        try {
            // transaction:
            pm.currentTransaction().begin();
            pm.makePersistent(obj);
            pm.currentTransaction().commit();
        } finally {
            // Close the active transaction:
            if (pm.currentTransaction().isActive()) {
                pm.currentTransaction().rollback();
            }
        }
    }
    
    private Object vehicleExists(Object in, List<Object> array) {
    	DBVehicle input = (DBVehicle) in;
		for(Object o : array) {
			DBVehicle v = (DBVehicle) o;
			if(input.model.equals(v.model))
				return o;
		}
		return null;
	}

    void close() {
        if (pm.currentTransaction().isActive()) {
            pm.currentTransaction().rollback();
        }
        if (!pm.isClosed()) {
            pm.close();
        }
    }
}
