package itemtransformhelper;

import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;

/**
 * User: The Grey Ghost
 * Date: 20/01/2015
 * We use the ModelBakeEvent to iterate through all the registered models, wrap each one in an
 * ItemModelFlexibleCamera, and write it
 * back into the registry.
 * Each wrapped model gets a reference to ModelBakeEventHandler::itemOverrideLink.
 * Later, we can alter the members of itemOverrideLink to change the ItemCameraTransforms for a desired Item
 */
public class ModelBakeEventHandler {

    private final ItemModelFlexibleCamera.UpdateLink itemOverrideLink = new ItemModelFlexibleCamera.UpdateLink();

    private boolean warned = false;

    public ModelBakeEventHandler() {
        itemOverrideLink.forcedTransform = new ItemTransforms(
                ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM,
                ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM,
                ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM,
                ItemTransform.NO_TRANSFORM, ItemTransform.NO_TRANSFORM);
    }

    public void modelBakeEvent() {
        if (!warned) {
            warned = true;
            ItemTransformHelper.LOGGER.warn("Warning - The Item Transform Helper replaces your BakedModels with a "
                                            + "wrapped version, this");
            ItemTransformHelper.LOGGER.warn("  is done even when the helper is not in your hotbar, and might cause "
                                            + "problems if your");
            ItemTransformHelper.LOGGER.warn("  BakedModel implements an interface ItemTransformHelper doesn't know "
                                            + "about.");
            ItemTransformHelper.LOGGER.warn("  I recommend you disable the mod when you're not actively using it to "
                                            + "transform your items.");
        }
    }

    public ItemModelFlexibleCamera.UpdateLink getItemOverrideLink() {
        return itemOverrideLink;
    }

}
