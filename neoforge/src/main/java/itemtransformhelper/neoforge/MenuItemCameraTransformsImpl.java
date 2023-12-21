package itemtransformhelper.neoforge;

import itemtransformhelper.HUDTextRenderer;
import itemtransformhelper.MenuItemCameraTransforms;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;

public class MenuItemCameraTransformsImpl {

    private final HUDTextRenderer hudTextRenderer;
    private final MenuItemCameraTransforms.MenuKeyHandler menuKeyHandler;

    public MenuItemCameraTransformsImpl(HUDTextRenderer hudTextRenderer,
                                        MenuItemCameraTransforms.MenuKeyHandler menuKeyHandler) {
        this.hudTextRenderer = hudTextRenderer;
        this.menuKeyHandler = menuKeyHandler;
    }

    public static void registerListeners(HUDTextRenderer hudTextRenderer,
                                         MenuItemCameraTransforms.MenuKeyHandler menuKeyHandler) {
        NeoForge.EVENT_BUS.register(new MenuItemCameraTransformsImpl(hudTextRenderer, menuKeyHandler));
    }

    @SubscribeEvent
    public void displayHUDText(CustomizeGuiOverlayEvent.Chat event) {
        hudTextRenderer.displayHUDText(event.getGuiGraphics());
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            menuKeyHandler.clientTick();
        }
    }

}
