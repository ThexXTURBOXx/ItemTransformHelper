package itemtransformhelper.fabric;

import itemtransformhelper.HUDTextRenderer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MenuItemCameraTransformsImpl {

    public static final Queue<HUDTextRenderer> RENDERERS = new ConcurrentLinkedQueue<>();

    public static void registerListeners(HUDTextRenderer hudTextRenderer) {
        RENDERERS.offer(hudTextRenderer);
    }

}
