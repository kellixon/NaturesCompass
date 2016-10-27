package com.chaosthedude.naturescompass.config;

import java.io.File;

import com.chaosthedude.naturescompass.NaturesCompass;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigHandler {

	public static Configuration config;

	public static int distanceModifier = 2500;
	public static int sampleSpaceModifier = 16;
	public static boolean fixBiomeNames = true;
	public static int lineOffset = 1;

	public static void loadConfig(File configFile) {
		config = new Configuration(configFile);

		config.load();
		init();

		MinecraftForge.EVENT_BUS.register(new ChangeListener());
	}

	public static void init() {
		String comment;

		comment = "biomeSize * distanceModifier = maxSearchDistance. Raising this value will increase search accuracy but will potentially make the process more resource intensive.";
		distanceModifier = loadInt(Configuration.CATEGORY_GENERAL, "naturescompass.distanceModifier", comment, distanceModifier);

		comment = "biomeSize * sampleSpaceModifier = sampleSpace. Lowering this value will increase search accuracy but will make the process more resource intensive.";
		sampleSpaceModifier = loadInt(Configuration.CATEGORY_GENERAL, "naturescompass.sampleSpaceModifier", comment, sampleSpaceModifier);

		comment = "Fixes biome names by adding missing spaces. Ex: ForestHills becomes Forest Hills";
		fixBiomeNames = loadBool(Configuration.CATEGORY_CLIENT, "naturescompass.fixBiomeNames", comment, fixBiomeNames);

		comment = "The line offset for information rendered on the HUD.";
		lineOffset = loadInt(Configuration.CATEGORY_CLIENT, "naturescompass.lineOffset", comment, lineOffset);

		if (config.hasChanged()) {
			config.save();
		}
	}

	public static int loadInt(String category, String name, String comment, int def) {
		final Property prop = config.get(category, name, def);
		prop.setComment(comment);
		int val = prop.getInt(def);
		if (val < 0) {
			val = def;
			prop.set(def);
		}

		return val;
	}

	public static boolean loadBool(String category, String name, String comment, boolean def) {
		final Property prop = config.get(category, name, def);
		prop.setComment(comment);
		return prop.getBoolean(def);
	}

	public static class ChangeListener {
		@SubscribeEvent
		public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(NaturesCompass.MODID)) {
				init();
			}
		}
	}

}
