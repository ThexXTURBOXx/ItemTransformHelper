package itemtransformhelper.neoforge;

import com.google.common.collect.ImmutableMap;
import itemtransformhelper.HUDTextRenderer;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemDisplayContext;
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

    public static ItemTransforms newItemTransforms(ItemTransform thirdPersonLeftHand,
                                                   ItemTransform thirdPersonRightHand,
                                                   ItemTransform firstPersonLeftHand,
                                                   ItemTransform firstPersonRightHand,
                                                   ItemTransform head,
                                                   ItemTransform gui,
                                                   ItemTransform ground,
                                                   ItemTransform fixed,
                                                   ImmutableMap<ItemDisplayContext, ItemTransform> moddedTransforms) {
        return new ItemTransforms(thirdPersonLeftHand, thirdPersonRightHand,
                firstPersonLeftHand, firstPersonRightHand,
                head, gui, ground, fixed,
                moddedTransforms);
    }

    @SubscribeEvent
    public void displayHUDText(RenderGuiLayerEvent.Pre event) {
        if (!event.getName().equals(VanillaGuiLayers.EFFECTS)) return;
        hudTextRenderer.displayHUDText(event.getGuiGraphics());
    }

}
