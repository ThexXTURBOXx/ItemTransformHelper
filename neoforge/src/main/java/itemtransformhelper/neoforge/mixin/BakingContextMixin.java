package itemtransformhelper.neoforge.mixin;

import itemtransformhelper.ItemModelFlexibleCamera;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static itemtransformhelper.StartupClientOnly.modelBakeEventHandler;

@Mixin(ItemModel.BakingContext.class)
public class BakingContextMixin {

    @Inject(method = "bake", at = @At(value = "RETURN"), cancellable = true)
    public void bake(ResourceLocation resourceLocation, CallbackInfoReturnable<BakedModel> cir) {
        modelBakeEventHandler.modelBakeEvent();
        cir.setReturnValue(new ItemModelFlexibleCamera(cir.getReturnValue(),
                modelBakeEventHandler.getItemOverrideLink()));
    }

}
