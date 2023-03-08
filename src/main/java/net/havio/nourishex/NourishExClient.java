package net.havio.nourishex;

import com.github.clevernucleus.playerex.api.client.PageRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.havio.nourishex.gui.NourishmentPageLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.havio.nourishex.NourishEx.MOD_ID;

public class NourishExClient implements ClientModInitializer {

    public static final Identifier GUI = new Identifier("nourishex", "textures/gui/gui.png");
    public static final Identifier NOURISHMENT_PAGE = new Identifier(MOD_ID, "nourishment");

    public void onInitializeClient() {
        PageRegistry.registerPage(NOURISHMENT_PAGE, new Identifier(MOD_ID, "textures/gui/nourishment.png"), Text.translatable("nourishex.gui.page.nourishment.title"));
        PageRegistry.registerLayer(NOURISHMENT_PAGE, NourishmentPageLayer::new);
    }
}
