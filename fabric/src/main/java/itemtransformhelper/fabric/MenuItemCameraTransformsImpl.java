package itemtransformhelper.fabric;

import itemtransformhelper.HUDTextRenderer;
import itemtransformhelper.MenuItemCameraTransforms;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MenuItemCameraTransformsImpl {

    public static final Queue<HUDTextRenderer> RENDERERS = new ConcurrentLinkedQueue<>();
    public static final Queue<MenuItemCameraTransforms.MenuKeyHandler> HANDLERS = new ConcurrentLinkedQueue<>();

    public static void registerListeners(HUDTextRenderer hudTextRenderer,
                                         MenuItemCameraTransforms.MenuKeyHandler menuKeyHandler) {
        RENDERERS.offer(hudTextRenderer);
        HANDLERS.offer(menuKeyHandler);
    }

}
