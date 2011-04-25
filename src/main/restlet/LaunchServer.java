package restlet;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class LaunchServer {

    public static void main(String[] args) {
        try {
            Component component = new Component();
            component.getServers().add(Protocol.HTTP, 8181);
            component.getDefaultHost().attach(new MosesApplication());
            component.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}