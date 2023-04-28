package earth.terrarium.argonauts.client.screens.chat.messages;

import com.google.common.primitives.UnsignedInteger;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import earth.terrarium.argonauts.client.screens.chat.CustomChatScreen;
import earth.terrarium.argonauts.client.utils.ClientUtils;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessage;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class ChatMessageListEntry extends ListEntry {

    private final UnsignedInteger id;
    private final ChatMessage message;
    private final FormattedCharSequence component;

    public ChatMessageListEntry(UnsignedInteger id, ChatMessage message, FormattedCharSequence component) {
        this.id = id;
        this.message = message;
        this.component = component;
    }

    @Override
    protected void render(@NotNull ScissorBoxStack scissorStack, @NotNull PoseStack stack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
        if (this.id.mod(ModUtils.UNSIGNED_TWO).equals(UnsignedInteger.ZERO)) {
            Gui.fill(stack, left, top, left + width, top + height, 0x80000000);
        }
        Minecraft.getInstance().font.draw(stack, this.component, left + 8, top + 1, 0xFFFFFF);

        var style = Minecraft.getInstance().font.getSplitter().componentStyleAtWidth(this.component, Mth.floor(mouseX - left));
        if (style != null && hovered) {
            if (style.getHoverEvent() != null) {
                Component component = style.getHoverEvent().getValue(HoverEvent.Action.SHOW_TEXT);
                if (component != null) {
                    ClientUtils.setTooltip(component);
                }
            } else if (style.getClickEvent() != null) {
                ClickEvent.Action clickAction = style.getClickEvent().getAction();
                if (clickAction == ClickEvent.Action.OPEN_URL && Minecraft.getInstance().screen instanceof CustomChatScreen screen) {
                    screen.setEmbedUrl(style.getClickEvent().getValue());
                }
            }
        }
    }

    public UnsignedInteger id() {
        return this.id;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (button == InputConstants.MOUSE_BUTTON_LEFT) {
            var style = Minecraft.getInstance().font.getSplitter().componentStyleAtWidth(this.component, Mth.floor(x));
            if (style != null && style.getClickEvent() != null) {
                switch (style.getClickEvent().getAction()) {
                    case OPEN_URL -> Util.getPlatform().openUri(style.getClickEvent().getValue());
                    default -> {
                        System.out.println("Unhandled click event: " + style.getClickEvent().getAction());
                    }
                }
            }
        }
        return super.mouseClicked(x, y, button);
    }

    @Override
    public void setFocused(boolean bl) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
