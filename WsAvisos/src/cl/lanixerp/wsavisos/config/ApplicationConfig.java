package cl.lanixerp.wsavisos.config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import cl.lanixerp.wsavisos.rest.WsAvisos;

@ApplicationPath("/")
public class ApplicationConfig extends Application {
	
	@Override
    public Set<Class<?>> getClasses() {
		Set<Class<?>> clases = new HashSet<>();
            clases.add(WsAvisos.class);
        return clases;
    }
	
	@Override
	public Set<Object> getSingletons() {
		return new HashSet<Object>();
	}
	
}
