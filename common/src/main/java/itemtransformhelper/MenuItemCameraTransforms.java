package itemtransformhelper;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import itemtransformhelper.HUDTextRenderer.HUDInfoUpdateLink;
import itemtransformhelper.HUDTextRenderer.HUDInfoUpdateLink.SelectedField;
import itemtransformhelper.HUDTextRenderer.HUDInfoUpdateLink.TransformName;
import java.util.Locale;
import java.util.function.UnaryOperator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.glfw.GLFW;


/**
 * The menu used to select and alter the different parts of the ItemCameraTransform for the currently selected item.
 * The menu state is rendered on the screen by HUDTextRenderer.
 * The class registers its components on the Forge and FML event buses.
 * Created by TheGreyGhost on 22/01/15.
 */
public class MenuItemCameraTransforms {

    private static final Logger LOGGER = LogManager.getLogger();

    private final HUDInfoUpdateLink linkToHudRenderer;

    public MenuItemCameraTransforms() {
        linkToHudRenderer = new HUDInfoUpdateLink();

        MenuKeyHandler menuKeyHandler = new MenuKeyHandler(this.new KeyPressCallback());
        ClientTickEvent.CLIENT_PRE.register(mc -> menuKeyHandler.clientTick());

        registerListeners(new HUDTextRenderer(linkToHudRenderer));
    }

    @ExpectPlatform
    public static void registerListeners(HUDTextRenderer hudTextRenderer) {
        throw new AssertionError();
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
        ItemTransform transform = linkToHudRenderer.getItemTransform();
        if (transform == null) return; // should never happen

        final float SCALE_INCREMENT = 0.01F;
        final float ROTATION_INCREMENT = 2F;
        // 1/4 of a block, with multiplier from ItemTransform.Deserializer::deserialize
        final float TRANSLATION_INCREMENT = 0.25F * 0.0625F;
        switch (linkToHudRenderer.selectedField) {
        case TRANSFORM -> linkToHudRenderer.selectedTransform = increase
                ? linkToHudRenderer.selectedTransform.getNext()
                : linkToHudRenderer.selectedTransform.getPrevious();
        case SCALE_X, SCALE_Y, SCALE_Z -> changeSelectedField(
                f -> f + (increase ? SCALE_INCREMENT : -SCALE_INCREMENT));
        case ROTATE_X, ROTATE_Y, ROTATE_Z -> changeSelectedField(
                f -> Mth.wrapDegrees((f + (increase ? ROTATION_INCREMENT : -ROTATION_INCREMENT)) - 180) + 180);
        case TRANSLATE_X, TRANSLATE_Y, TRANSLATE_Z -> changeSelectedField(
                f -> f + (increase ? TRANSLATION_INCREMENT : -TRANSLATION_INCREMENT));
        case RESTORE_DEFAULT_ALL, RESTORE_DEFAULT -> {
            ItemTransforms originalTransforms = UpdateLink.INSTANCE.originalTransforms;
            if (originalTransforms != null) {
                UpdateLink.INSTANCE.originalTransforms = null;
                if (linkToHudRenderer.selectedField == SelectedField.RESTORE_DEFAULT) {
                    linkToHudRenderer.itemCameraTransforms = changeTransforms(
                            linkToHudRenderer.itemCameraTransforms,
                            linkToHudRenderer.selectedTransform,
                            linkToHudRenderer.selectedTransform.getItemTransform(originalTransforms)
                    );
                } else {
                    linkToHudRenderer.itemCameraTransforms = originalTransforms;
                }
            }
        }
        case PRINT -> {
            StringBuilder output = new StringBuilder();
            output.append("\n\"display\": {\n");
            printTransform(output, "thirdperson_righthand",
                    linkToHudRenderer.itemCameraTransforms.thirdPersonRightHand());
            output.append(",\n");
            printTransform(output, "thirdperson_lefthand",
                    linkToHudRenderer.itemCameraTransforms.thirdPersonLeftHand());
            output.append(",\n");
            printTransform(output, "firstperson_righthand",
                    linkToHudRenderer.itemCameraTransforms.firstPersonRightHand());
            output.append(",\n");
            printTransform(output, "firstperson_lefthand",
                    linkToHudRenderer.itemCameraTransforms.firstPersonLeftHand());
            output.append(",\n");
            printTransform(output, "gui", linkToHudRenderer.itemCameraTransforms.gui());
            output.append(",\n");
            printTransform(output, "head", linkToHudRenderer.itemCameraTransforms.head());
            output.append(",\n");
            printTransform(output, "fixed", linkToHudRenderer.itemCameraTransforms.fixed());
            output.append(",\n");
            printTransform(output, "ground", linkToHudRenderer.itemCameraTransforms.ground());
            output.append("\n}");
            LOGGER.info(output);
            Component text = Component.literal("\"display\" JSON section printed to console (LOGGER.info)...");
            Minecraft.getInstance().gui.getChat().addMessage(text);
        }
        }
    }

    private void changeSelectedField(UnaryOperator<Float> modifier) {
        linkToHudRenderer.itemCameraTransforms = changeTransforms(
                linkToHudRenderer.itemCameraTransforms,
                linkToHudRenderer.selectedTransform,
                changeTransform(
                        linkToHudRenderer.getItemTransform(),
                        linkToHudRenderer.selectedField,
                        changeVec(
                                linkToHudRenderer.getItemTransformVec(),
                                linkToHudRenderer.selectedField,
                                modifier.apply(linkToHudRenderer.getItemTransformVecValue())
                        )
                )
        );
    }

    public static ItemTransforms changeTransforms(ItemTransforms old, TransformName selectedTransform,
                                                  ItemTransform newTransform) {
        return new ItemTransforms(
                selectedTransform == TransformName.THIRD_LEFT ? newTransform : old.thirdPersonLeftHand(),
                selectedTransform == TransformName.THIRD_RIGHT ? newTransform : old.thirdPersonRightHand(),
                selectedTransform == TransformName.FIRST_LEFT ? newTransform : old.firstPersonLeftHand(),
                selectedTransform == TransformName.FIRST_RIGHT ? newTransform : old.firstPersonRightHand(),
                selectedTransform == TransformName.HEAD ? newTransform : old.head(),
                selectedTransform == TransformName.GUI ? newTransform : old.gui(),
                selectedTransform == TransformName.GROUND ? newTransform : old.ground(),
                selectedTransform == TransformName.FIXED ? newTransform : old.fixed()
        );
    }

    public static ItemTransform changeTransform(ItemTransform old, SelectedField selectedField,
                                                Vector3fc newTransform) {
        return new ItemTransform(
                selectedField.isRotation() ? newTransform : old.rotation(),
                selectedField.isTranslation() ? newTransform : old.translation(),
                selectedField.isScale() ? newTransform : old.scale()
        );
    }

    public static Vector3fc changeVec(Vector3fc old, SelectedField selectedField, float newValue) {
        return new Vector3f(
                selectedField.isX() ? newValue : old.x(),
                selectedField.isY() ? newValue : old.y(),
                selectedField.isZ() ? newValue : old.z()
        );
    }

    private static void printTransform(StringBuilder output, String transformView, ItemTransform transformation) {
        final Locale LOCALE = Locale.US;

        output.append("    \"").append(transformView).append("\": {\n");

        output.append(String.format(LOCALE,
                "        \"rotation\": [ %.0f, %.0f, %.0f ],\n",
                transformation.rotation().x(),
                transformation.rotation().y(),
                transformation.rotation().z()));

        final double TRANSLATE_MULTIPLIER = 1 / 0.0625;   // see ItemTransform.Deserializer::deserialize
        output.append(String.format(LOCALE,
                "        \"translation\": [ %.2f, %.2f, %.2f ],\n",
                transformation.translation().x() * TRANSLATE_MULTIPLIER,
                transformation.translation().y() * TRANSLATE_MULTIPLIER,
                transformation.translation().z() * TRANSLATE_MULTIPLIER));

        output.append(String.format(LOCALE,
                "        \"scale\": [ %.2f, %.2f, %.2f ]\n",
                transformation.scale().x(),
                transformation.scale().y(),
                transformation.scale().z()));

        output.append("    }");
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
            final int INITIAL_PAUSE_TICKS = 10;  // wait 10 ticks before repeating

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
            } else if (++keyDownTimeTicks < INITIAL_PAUSE_TICKS) return;

            keyPressCallback.keyPressed(keyPressed);
        }

        private static boolean isKeyDown(int key) {
            return GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), key) == GLFW.GLFW_PRESS;
        }

        public enum ArrowKeys {NONE, UP, DOWN, LEFT, RIGHT}

    }

}
