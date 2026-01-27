package dz.fst.bank.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * JAX-RS Application Configuration pour FSTBANK
 */
@ApplicationPath("/api")
public class FSTBankApplication extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(DashboardResource.class);
        return classes;
    }
}
