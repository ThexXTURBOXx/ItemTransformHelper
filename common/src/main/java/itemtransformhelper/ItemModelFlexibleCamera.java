package itemtransformhelper;


import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
public class ItemModelFlexibleCamera implements BakedModel {

    protected final BakedModel originalModel;
    protected final UpdateLink updateLink;

    public ItemModelFlexibleCamera(BakedModel originalModel, UpdateLink updateLink) {
        this.originalModel = originalModel;
        this.updateLink = updateLink;
    }

    @NotNull
    @Override
    public ItemTransforms getTransforms() {
        return (updateLink.itemModelToOverride == this)
                ? updateLink.forcedTransform
                : originalModel.getTransforms();
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        return originalModel.getQuads(state, side, rand);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return originalModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return originalModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return originalModel.usesBlockLight();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return originalModel.getParticleIcon();
    }

    public static class UpdateLink {

        public volatile boolean foundCamera;

        public volatile ItemStack heldItemStack;

        public volatile boolean isRenderingHeldItem;

        public volatile BakedModel itemModelToOverride;

        public volatile ItemTransforms forcedTransform;

    }

}
