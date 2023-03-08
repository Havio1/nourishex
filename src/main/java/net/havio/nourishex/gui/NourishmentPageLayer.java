package net.havio.nourishex.gui;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.PageLayer;
import com.github.clevernucleus.playerex.api.client.RenderComponent;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.nourish.NourishComponent;
import dev.emi.nourish.NourishHolder;
import dev.emi.nourish.groups.NourishGroup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.havio.nourishex.NourishExClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class NourishmentPageLayer extends PageLayer {
    private static final Identifier GUI_TEX = new Identifier("nourish", "textures/gui/gui.png");
    private int maxNameLength = 0;
    private int w;
    private int h;

    private static Supplier<Float> scaleX = () -> {
        return ExAPI.getConfig().textScaleX();
    };
    private static Supplier<Float> scaleY = () -> {
        return ExAPI.getConfig().textScaleY();
    };

    private static float scaleZ = 0.75F;
    private static final List<RenderComponent> COMPONENTS = new ArrayList();

    public NourishmentPageLayer(HandledScreen<?> parent, ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(parent, handler, inventory, title);
    }

    @Override
    public void init() {
        List<NourishGroup> groups = NourishHolder.NOURISH.get(client.player).getProfile().groups;
        for (NourishGroup group: groups) {
            int l = this.textRenderer.getWidth(Text.translatable("nourish.group." + group.identifier.getPath()).getString());
            if (l > maxNameLength) {
                maxNameLength = l;
            }
        }
        w = maxNameLength + 120;
        h = 34 + groups.size() * 20;
        if (groups.size() > 0 && groups.get(groups.size() - 1).secondary) {
            h += 24;
        }
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

        RenderSystem.setShaderTexture(0, GUI_TEX);
        matrices.push();
        matrices.scale((Float)scaleX.get(), (Float)scaleY.get(), scaleZ);
        COMPONENTS.forEach((component) -> {
            component.renderText(this.client.player, matrices, this.textRenderer, this.x, this.y, (Float)scaleX.get(), (Float)scaleY.get());
        });
        int yo = 90;
        boolean secondary = false;
        for (NourishGroup group: NourishHolder.NOURISH.get(client.player).getProfile().groups) {
            if (group.secondary && !secondary) {
                secondary = true;
                Text t = Text.translatable("nourish.gui.secondary");
                int sw = this.textRenderer.getWidth(t.getString());
                this.textRenderer.draw(matrices, t.getString(), x + w / 2 - sw / 2 + 120, y + yo + 4, 4210752);
                yo += 20;
            }
            int color = group.getColor() | 0xFF000000;

            //changed from "x + 10" to "x+ 20"
            this.textRenderer.draw(matrices, Text.translatable("nourish.group." + group.identifier.getPath()).getString(), x + 180, y + yo + 4, 4210752);

            NourishComponent comp = NourishHolder.NOURISH.get(client.player);
            RenderSystem.setShaderTexture(0, GUI_TEX);

            //draw empty progress bar texture
            this.drawTexture(matrices, x + maxNameLength + 15 + 180, y + yo + 2, 0, 8, 90, 12);
            //draw fill progress bar
            DrawableHelper.fill(matrices,  x + maxNameLength + 196, y + yo + 3, x + maxNameLength + 196 + Math.round(88 * comp.getValue(group)), y + yo + 13, color);

            if (mouseX > x + maxNameLength + 15 && mouseY > y + yo + 2 && mouseX < x + maxNameLength + 108 && mouseY < y + yo + 13) {
                if (group.description) {
                    List<Text> lines = Lists.newArrayList();
                    lines.add(Text.translatable("nourish.group.description." + group.identifier.getPath()));
                    this.renderTooltip(matrices, lines, mouseX, mouseY);
                }
            }
            yo += 20;
        }
        matrices.pop();
        COMPONENTS.forEach((component) -> {
            component.renderTooltip(this.client.player, this::renderTooltip, matrices, this.textRenderer, this.x, this.y, mouseX, mouseY, (Float)scaleX.get(), (Float)scaleY.get());
        });
        super.render(matrices, mouseX, mouseY, delta);
    }

    public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, NourishExClient.GUI);
        this.drawTexture(matrices, this.x + 9, this.y + 24, 244, 9, 9, 9);
        this.drawTexture(matrices, this.x + 9, this.y + 90, 226, 18, 9, 9);
        this.drawTexture(matrices, this.x + 93, this.y + 24, 235, 18, 9, 9);
    }
}
