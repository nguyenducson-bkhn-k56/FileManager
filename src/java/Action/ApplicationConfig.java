/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author nguyen
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(Action.FavouriteAction.class);
//        resources.add(org.glassfish.jersey.media.multipart.MultiPartProperties.Feature.MultiPartContextResolver.class);
//        resources.add(org.glassfish.jersey.media.multipart.MultiPartProperties.Feature.MultiPartContextResolver.class);
        resources.add(Action.FileAction.class);
        resources.add(Action.FileActionB.class);
        resources.add(Action.FolderAction.class);
        resources.add(Action.UserAction.class);
        resources.add(test.Test.class);
    }
    
}
