package itemtransformhelper;

import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;

/**
 * User: The Grey Ghost
 * Date: 20/01/2015
 * This class is a simple wrapper to substitute a new set of camera transforms for an existing item
 * Usage:
 * 1) Construct a new ItemModelFlexibleCamera with the model to wrap and an UpdateLink using getWrappedModel()
 * 2) Replace the ItemModelFlexibleCamera into the modelRegistry in place of the model to wrap
 * 3) Alter the UpdateLink to control the ItemCameraTransform of a given model:
 * a) itemModelToOverride selects the item to be overridden
 * b) forcedTransform is the transform to apply
 * Models which don't match itemModelToOverride will use their original transform
 * <p>
 * NB Starting with Forge 1.8-11.14.4.1563, it appears that all items now implement IPerspectiveAwareModel
 */
public class UpdateLink {

    public static final UpdateLink INSTANCE = new UpdateLink();

    public volatile boolean foundCamera;

    public volatile ItemStack heldItemStack;

    public volatile boolean isRenderingHeldItem;

    public volatile ItemTransforms forcedTransforms = ItemTransforms.NO_TRANSFORMS;

    public volatile ItemTransforms originalTransforms;

}
