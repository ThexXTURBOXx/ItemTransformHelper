package itemtransformhelper.quilt.mixin;

import itemtransformhelper.fabric.MenuItemCameraTransformsImpl;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderEffects"
            + "(Lnet/minecraft/client/gui/GuiGraphics;)V"))
    public void render(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        MenuItemCameraTransformsImpl.RENDERERS.forEach(r -> r.displayHUDText(guiGraphics));
    }

}
