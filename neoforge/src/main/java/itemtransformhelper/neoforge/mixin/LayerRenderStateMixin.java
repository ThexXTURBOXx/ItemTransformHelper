package itemtransformhelper.neoforge.mixin;

import itemtransformhelper.ItemModelFlexibleCamera;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.BakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static itemtransformhelper.StartupClientOnly.modelBakeEventHandler;

@Mixin(ItemStackRenderState.LayerRenderState.class)
public class LayerRenderStateMixin {

    @Inject(method = "setupBlockModel", at = @At("RETURN"))
    public void setupBlockModel(BakedModel bakedModel, RenderType renderType, CallbackInfo ci) {
        ItemModelFlexibleCamera.UpdateLink link = modelBakeEventHandler.getItemOverrideLink();
        if (link.foundCamera && link.isRenderingHeldItem)
            link.itemModelToOverride = bakedModel;
    }

    @Inject(method = "setupSpecialModel", at = @At("RETURN"))
    public <T> void setupSpecialModel(SpecialModelRenderer<T> specialModelRenderer, T object, BakedModel bakedModel,
                                      CallbackInfo ci) {
        ItemModelFlexibleCamera.UpdateLink link = modelBakeEventHandler.getItemOverrideLink();
        if (link.foundCamera && link.isRenderingHeldItem)
            link.itemModelToOverride = bakedModel;
    }

}
