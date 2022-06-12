package itemtransformhelper;

import dev.architectury.injectables.annotations.ExpectPlatform;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;


/**
 * The menu used to select and alter the different parts of the ItemCameraTransform for the currently selected item.
 * The menu state is rendered on the screen by HUDTextRenderer.
 * The class registers its components on the Forge and FML event buses.
 * Created by TheGreyGhost on 22/01/15.
 */
public class MenuItemCameraTransforms {

    private static final Logger LOGGER = LogManager.getLogger();

    private final HUDTextRenderer.HUDInfoUpdateLink linkToHudRenderer;

    public MenuItemCameraTransforms() {
        linkToHudRenderer = new HUDTextRenderer.HUDInfoUpdateLink();
        registerListeners(new HUDTextRenderer(linkToHudRenderer),
                new MenuKeyHandler(this.new KeyPressCallback()));
    }

    @ExpectPlatform
    public static void registerListeners(HUDTextRenderer hudTextRenderer, MenuKeyHandler menuKeyHandler) {
        throw new UnsupportedOperationException();
    }

    /**
     * get the current ItemCameraTransforms
     *
     * @return the transform
     */
    public ItemTransforms getItemCameraTransforms() {
        return linkToHudRenderer.itemCameraTransforms;
    }

    /**
     * turn menu on or off
     *
     * @param visible true = make visible
     */
    public void changeMenuVisible(boolean visible) {
        linkToHudRenderer.menuVisible = visible;
    }

    public class KeyPressCallback {

        void keyPressed(MenuKeyHandler.ArrowKeys whichKey) {
            if (!linkToHudRenderer.menuVisible) return;

            switch (whichKey) {
            case DOWN -> linkToHudRenderer.selectedField = linkToHudRenderer.selectedField.getNextField();
            case UP -> linkToHudRenderer.selectedField = linkToHudRenderer.selectedField.getPreviousField();
            case RIGHT, LEFT -> alterField(whichKey == MenuKeyHandler.ArrowKeys.RIGHT);
            default -> {
            }
            }
        }

    }

    private void alterField(boolean increase) {
        ItemTransform transformVec3f = getItemTransformRef(linkToHudRenderer, linkToHudRenderer.selectedTransform);
        if (transformVec3f == null) return; // should never happen

        final float SCALE_INCREMENT = 0.01F;
        final float ROTATION_INCREMENT = 2F;
        final float TRANSLATION_INCREMENT = 0.25F * 0.0625F; // 1/4 of a block, with multiplier from
        // Transformation.Deserializer::deserialize
        switch (linkToHudRenderer.selectedField) {
        case TRANSFORM -> linkToHudRenderer.selectedTransform = increase ? linkToHudRenderer.selectedTransform.getNext()
                : linkToHudRenderer.selectedTransform.getPrevious();
        case SCALE_X -> transformVec3f.scale.add(increase ? SCALE_INCREMENT : -SCALE_INCREMENT, 0, 0);
        case SCALE_Y -> transformVec3f.scale.add(0, increase ? SCALE_INCREMENT : -SCALE_INCREMENT, 0);
        case SCALE_Z -> transformVec3f.scale.add(0, 0, increase ? SCALE_INCREMENT : -SCALE_INCREMENT);
        case ROTATE_X -> {
            float newAngle = transformVec3f.rotation.x() + (increase ? ROTATION_INCREMENT : -ROTATION_INCREMENT);
            newAngle = Mth.wrapDegrees(newAngle - 180) + 180;
            transformVec3f.rotation.set(newAngle, transformVec3f.rotation.y(), transformVec3f.rotation.z());
        }
        case ROTATE_Y -> {
            float newAngle = transformVec3f.rotation.y() + (increase ? ROTATION_INCREMENT : -ROTATION_INCREMENT);
            newAngle = Mth.wrapDegrees(newAngle - 180) + 180;
            transformVec3f.rotation.set(transformVec3f.rotation.x(), newAngle, transformVec3f.rotation.z());
        }
        case ROTATE_Z -> {
            float newAngle = transformVec3f.rotation.z() + (increase ? ROTATION_INCREMENT : -ROTATION_INCREMENT);
            newAngle = Mth.wrapDegrees(newAngle - 180) + 180;
            transformVec3f.rotation.set(transformVec3f.rotation.x(), transformVec3f.rotation.y(), newAngle);
        }
        case TRANSLATE_X -> transformVec3f.translation
                .add(increase ? TRANSLATION_INCREMENT : -TRANSLATION_INCREMENT, 0, 0);
        case TRANSLATE_Y -> transformVec3f.translation
                .add(0, increase ? TRANSLATION_INCREMENT : -TRANSLATION_INCREMENT, 0);
        case TRANSLATE_Z -> transformVec3f.translation
                .add(0, 0, increase ? TRANSLATION_INCREMENT : -TRANSLATION_INCREMENT);
        case RESTORE_DEFAULT_ALL, RESTORE_DEFAULT -> {
            ItemModelFlexibleCamera.UpdateLink link = StartupClientOnly.modelBakeEventHandler.getItemOverrideLink();
            BakedModel savedModel = link.itemModelToOverride;
            if (savedModel != null) {  // not sure why this would ever be null, but it was (in a bug report), so just
                // check to make sure.
                link.itemModelToOverride = null;
                if (linkToHudRenderer.selectedField == HUDTextRenderer.HUDInfoUpdateLink.SelectedField.RESTORE_DEFAULT) {
                    copySingleTransform(linkToHudRenderer, savedModel, linkToHudRenderer.selectedTransform);
                } else {
                    for (HUDTextRenderer.HUDInfoUpdateLink.TransformName transformName
                            : HUDTextRenderer.HUDInfoUpdateLink.TransformName.VALUES) {
                        copySingleTransform(linkToHudRenderer, savedModel, transformName);
                    }
                }
            }
            link.itemModelToOverride = savedModel;
        }
        case PRINT -> {
            StringBuilder output = new StringBuilder();
            output.append("\n\"display\": {\n");
            printTransform(output, "thirdperson_righthand",
                    linkToHudRenderer.itemCameraTransforms.thirdPersonRightHand);
            output.append(",\n");
            printTransform(output, "thirdperson_lefthand", linkToHudRenderer.itemCameraTransforms.thirdPersonLeftHand);
            output.append(",\n");
            printTransform(output, "firstperson_righthand",
                    linkToHudRenderer.itemCameraTransforms.firstPersonRightHand);
            output.append(",\n");
            printTransform(output, "firstperson_lefthand", linkToHudRenderer.itemCameraTransforms.firstPersonLeftHand);
            output.append(",\n");
            printTransform(output, "gui", linkToHudRenderer.itemCameraTransforms.gui);
            output.append(",\n");
            printTransform(output, "head", linkToHudRenderer.itemCameraTransforms.head);
            output.append(",\n");
            printTransform(output, "fixed", linkToHudRenderer.itemCameraTransforms.fixed);
            output.append(",\n");
            printTransform(output, "ground", linkToHudRenderer.itemCameraTransforms.ground);
            output.append("\n}");
            LOGGER.info(output);
            Component text = Component.literal("\"display\" JSON section printed to console (LOGGER.info)...");
            Minecraft.getInstance().gui.getChat().addMessage(text);
        }
        }
    }

    // points to the appropriate transform based on which transform has been selected.
    private static ItemTransform getItemTransformRef(HUDTextRenderer.HUDInfoUpdateLink linkToHUDRenderer,
                                                     HUDTextRenderer.HUDInfoUpdateLink.TransformName transformName) {
        return switch (transformName) {
            case THIRD_LEFT -> linkToHUDRenderer.itemCameraTransforms.thirdPersonLeftHand;
            case THIRD_RIGHT -> linkToHUDRenderer.itemCameraTransforms.thirdPersonRightHand;
            case FIRST_LEFT -> linkToHUDRenderer.itemCameraTransforms.firstPersonLeftHand;
            case FIRST_RIGHT -> linkToHUDRenderer.itemCameraTransforms.firstPersonRightHand;
            case GUI -> linkToHUDRenderer.itemCameraTransforms.gui;
            case HEAD -> linkToHUDRenderer.itemCameraTransforms.head;
            case FIXED -> linkToHUDRenderer.itemCameraTransforms.fixed;
            case GROUND -> linkToHUDRenderer.itemCameraTransforms.ground;
        };
    }

    private void copySingleTransform(HUDTextRenderer.HUDInfoUpdateLink linkToHUDRenderer, BakedModel savedModel,
                                     HUDTextRenderer.HUDInfoUpdateLink.TransformName transformToBeCopied) {
        ItemTransform transformation = getItemTransformRef(linkToHUDRenderer, transformToBeCopied);
        ItemTransforms.TransformType currentType = transformToBeCopied.getVanillaTransformType();
        ItemTransform transform = savedModel.getTransforms().getTransform(currentType);
        copyTransforms(transform, transformation);
    }

    private static void printTransform(StringBuilder output, String transformView, ItemTransform transformation) {
        output.append("    \"").append(transformView).append("\": {\n");
        output.append("        \"rotation\": [ ");
        output.append(String.format(Locale.US, "%.0f, ", transformation.rotation.x()));
        output.append(String.format(Locale.US, "%.0f, ", transformation.rotation.y()));
        output.append(String.format(Locale.US, "%.0f ],", transformation.rotation.z()));
        output.append("\n");

        final double TRANSLATE_MULTIPLIER = 1 / 0.0625;   // see Transformation.Deserializer::deserialize
        output.append("        \"translation\": [ ");
        output.append(String.format(Locale.US, "%.2f, ", transformation.translation.x() * TRANSLATE_MULTIPLIER));
        output.append(String.format(Locale.US, "%.2f, ", transformation.translation.y() * TRANSLATE_MULTIPLIER));
        output.append(String.format(Locale.US, "%.2f ],", transformation.translation.z() * TRANSLATE_MULTIPLIER));
        output.append("\n");
        output.append("        \"scale\": [ ");
        output.append(String.format(Locale.US, "%.2f, ", transformation.scale.x()));
        output.append(String.format(Locale.US, "%.2f, ", transformation.scale.y()));
        output.append(String.format(Locale.US, "%.2f ]", transformation.scale.z()));
        output.append("\n    }");
    }

    private static void copyTransforms(ItemTransform from, ItemTransform to) {
        to.translation.load(from.translation);
        to.scale.load(from.scale);
        to.rotation.load(from.rotation);
    }

    /**
     * Intercept arrow keys and handle repeats
     */
    public static class MenuKeyHandler {

        private final KeyPressCallback keyPressCallback;
        private long keyDownTimeTicks = 0;
        private ArrowKeys lastKey = ArrowKeys.NONE;

        public MenuKeyHandler(KeyPressCallback keyPressCallback) {
            this.keyPressCallback = keyPressCallback;
        }

        public void clientTick() {
            ArrowKeys keyPressed = ArrowKeys.NONE;
            if (isKeyDown(GLFW.GLFW_KEY_LEFT)) keyPressed = ArrowKeys.LEFT;
            if (isKeyDown(GLFW.GLFW_KEY_RIGHT)) keyPressed = ArrowKeys.RIGHT;
            if (isKeyDown(GLFW.GLFW_KEY_DOWN)) keyPressed = ArrowKeys.DOWN;
            if (isKeyDown(GLFW.GLFW_KEY_UP)) keyPressed = ArrowKeys.UP;

            if (keyPressed == ArrowKeys.NONE) {
                lastKey = ArrowKeys.NONE;
                return;
            }
            if (keyPressed != lastKey) {
                lastKey = keyPressed;
                keyDownTimeTicks = 0;
            } else {
                ++keyDownTimeTicks;
                final int INITIAL_PAUSE_TICKS = 10;  // wait 10 ticks before repeating
                if (keyDownTimeTicks < INITIAL_PAUSE_TICKS) return;
            }
            keyPressCallback.keyPressed(keyPressed);
        }

        static boolean isKeyDown(int key) {
            return GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), key) == GLFW.GLFW_PRESS;
        }

        public enum ArrowKeys {NONE, UP, DOWN, LEFT, RIGHT}

    }

}
