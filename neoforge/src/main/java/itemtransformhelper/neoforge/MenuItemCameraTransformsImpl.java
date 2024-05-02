package itemtransformhelper.neoforge;

import itemtransformhelper.HUDTextRenderer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;

public class MenuItemCameraTransformsImpl {

    private final HUDTextRenderer hudTextRenderer;

    public MenuItemCameraTransformsImpl(HUDTextRenderer hudTextRenderer) {
        this.hudTextRenderer = hudTextRenderer;
    }

    public static void registerListeners(HUDTextRenderer hudTextRenderer) {
        NeoForge.EVENT_BUS.register(new MenuItemCameraTransformsImpl(hudTextRenderer));
    }

    @SubscribeEvent
    public void displayHUDText(RenderGuiLayerEvent.Pre event) {
        if (!event.getName().equals(VanillaGuiLayers.EFFECTS)) return;
        hudTextRenderer.displayHUDText(event.getGuiGraphics());
    }

}
