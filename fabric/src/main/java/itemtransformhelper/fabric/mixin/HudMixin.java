package itemtransformhelper.fabric.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static itemtransformhelper.fabric.MenuItemCameraTransformsImpl.RENDERERS;

@Mixin(Hud.class)
public class HudMixin {

    @Inject(method = "extractEffects", at = @At(value = "HEAD"))
    public void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        RENDERERS.forEach(r -> r.displayHUDText(graphics));
    }

}
