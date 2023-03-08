package net.havio.nourishex;

import com.github.clevernucleus.playerex.api.client.PageRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.havio.nourishex.gui.NourishmentPageLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NourishEx implements ModInitializer {
	public static final String MOD_ID = "nourishex";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static boolean isPlayerExLoaded;

	@Override
	public void onInitialize() {
		isPlayerExLoaded = FabricLoader.getInstance().isModLoaded("playerex");
		System.out.println(isPlayerExLoaded + " if true, PlayerEx is loaded, printed by 'NourishEx'");

	}
}
