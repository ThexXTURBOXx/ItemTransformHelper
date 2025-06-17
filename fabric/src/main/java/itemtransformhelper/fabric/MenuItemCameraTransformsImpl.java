package itemtransformhelper.fabric;

import com.google.common.collect.ImmutableMap;
import itemtransformhelper.HUDTextRenderer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemDisplayContext;

public class MenuItemCameraTransformsImpl {

    public static final Queue<HUDTextRenderer> RENDERERS = new ConcurrentLinkedQueue<>();

    public static void registerListeners(HUDTextRenderer hudTextRenderer) {
        RENDERERS.offer(hudTextRenderer);
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
                head, gui, ground, fixed); // No moddedTransforms on Fabric
    }

}
