package itemtransformhelper.forge;

import itemtransformhelper.HUDTextRenderer;
import itemtransformhelper.MenuItemCameraTransforms;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
        MinecraftForge.EVENT_BUS.register(new MenuItemCameraTransformsImpl(hudTextRenderer, menuKeyHandler));
    }

    @SubscribeEvent
    public void displayHUDText(CustomizeGuiOverlayEvent.Chat event) {
        hudTextRenderer.displayHUDText(event.getPoseStack());
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            menuKeyHandler.clientTick();
        }
    }

}
