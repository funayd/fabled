package studio.magemonkey.fabled;

import be.seeseemelk.mockbukkit.MockBukkit;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.function.Predicate;

public class DataGenerator {
    public static void main(String[] args) {
        if (args.length != 1 && args.length != 2) {
            throw new IllegalArgumentException();
        }

        String[] splitVersion = args[0].split("\\.");
        String   version      = splitVersion[0] + (splitVersion.length >= 2 ? '_' + splitVersion[1] : "");

        boolean useTypescript = Arrays.stream(args).noneMatch(Predicate.isEqual("js"));
        File    file          = new File("output/" + version.replace("_", ".") + "." + (useTypescript ? "ts" : "js"));
        file.delete();
        file.getParentFile().mkdirs();
        Server server = MockBukkit.mock();

        String dataVersion = "DATA_" + version;

        try (OutputStream out = Files.newOutputStream(file.toPath())) {
            if (useTypescript) {
                // This is just the import for Svelte -- We're just priming things here and this is specific
                // to the new editor
                out.write(("import type { VersionData } from '$api/types';\n\n").getBytes());
            }
            out.write((
                    (useTypescript ? "export const " : "var ") + dataVersion
                            + (useTypescript ? ": VersionData" : "") + " = {"
            ).getBytes());
            out.write(("\n    MATERIALS: [\n").getBytes());
            for (Material material : Material.values()) {
                if (material.isLegacy()) continue;
                if (material.isItem()) {
                    writeEnumConstant(out, material);
                }
            }
            out.write(("    ],\n    BLOCKS: [\n").getBytes());
            for (Material material : Material.values()) {
                if (material.isLegacy()) continue;
                if (material.isBlock()) {
                    writeEnumConstant(out, material);
                }
            }
            out.write(("    ],\n    DAMAGEABLE_MATERIALS: [\n").getBytes());
            for (Material material : Material.values()) {
                if (material.isLegacy()) continue;
                if (material.getMaxDurability() > 0) {
                    writeEnumConstant(out, material);
                }
            }
            out.write(("    ],\n    SOUNDS: [\n").getBytes());
            for (Sound sound : Sound.values()) {
                writeEnumConstant(out, sound);
            }
            out.write(("    ],\n    ENTITIES: [\n").getBytes());
            for (EntityType entityType : EntityType.values()) {
                writeEnumConstant(out, entityType);
            }
            out.write(("    ],\n    BIOMES: [\n").getBytes());
            for (Biome biome : Biome.values()) {
                writeEnumConstant(out, biome);
            }
            out.write(("    ],\n    POTIONS: [\n").getBytes());
            for (Field field : PotionEffectType.class.getDeclaredFields()) {
                if (field.getType().isAssignableFrom(PotionEffectType.class)
                        && !field.isAnnotationPresent(Deprecated.class)) {
                    out.write(("        \"" + sentenceCase(field.getName()) + "\",\n").getBytes());
                }
            }
            out.write(("    ],\n    PARTICLES: [\n").getBytes());
            for (Particle particle : Particle.values()) {
                writeEnumConstant(out, particle);
            }
            out.write(("    ],\n    DAMAGE_TYPES: [\n").getBytes());
            for (EntityDamageEvent.DamageCause damageCause : EntityDamageEvent.DamageCause.values()) {
                writeEnumConstant(out, damageCause);
            }
            out.write(("    ],\n    PROJECTILES: [\n").getBytes());
            for (EntityType entityType : EntityType.values()) {
                Class<? extends Entity> entityClass = entityType.getEntityClass();
                if (entityClass != null && Projectile.class.isAssignableFrom(entityClass)) {
                    writeEnumConstant(out, entityType);
                }
            }
            out.write(("    ],\n    MOB_DISGUISES: [\n").getBytes());
            for (EntityType entityType : EntityType.values()) {
                if (DisguiseType.getType(entityType).isMob()) {
                    writeEnumConstant(out, entityType);
                }
            }
            out.write(("    ],\n    MISC_DISGUISES: [\n").getBytes());
            for (EntityType entityType : EntityType.values()) {
                if (DisguiseType.getType(entityType).isMisc()) {
                    writeEnumConstant(out, entityType);
                }
            }
            out.write(("    ],\n    CONSUMABLE: [\n").getBytes());
            for (Material material : Material.values()) {
                if (material.isLegacy()) continue;
                if (material.isEdible()) {
                    writeEnumConstant(out, material);
                }
            }
            writeEnumConstant(out, Material.MILK_BUCKET);
            writeEnumConstant(out, Material.POTION);

            out.write(("    ],\n    ENCHANTS: [\n").getBytes());
            for (Field field : Enchantment.class.getDeclaredFields()) {
                if (field.getType().isAssignableFrom(Enchantment.class)
                        && !field.isAnnotationPresent(Deprecated.class)) {
                    out.write(("        \"" + sentenceCase(field.getName()) + "\",\n").getBytes());
                }
            }
            out.write(("    ]\n};\n\n").getBytes());
            if (useTypescript) {
                out.write((dataVersion + ".MATERIALS.sort();\n"
                        + dataVersion + ".DAMAGEABLE_MATERIALS.sort();\n"
                        + dataVersion + ".SOUNDS.sort();\n"
                        + dataVersion + ".ENTITIES.sort();\n"
                        + dataVersion + ".BIOMES.sort();\n"
                        + dataVersion + ".POTIONS.sort();\n"
                        + dataVersion + ".PARTICLES.sort();\n"
                        + dataVersion + ".DAMAGE_TYPES.sort();\n"
                        + dataVersion + ".PROJECTILES.sort();\n"
                        + dataVersion + ".MOB_DISGUISES.sort();\n"
                        + dataVersion + ".MISC_DISGUISES.sort();\n"
                        + dataVersion + ".CONSUMABLE.sort();\n"
                        + dataVersion + ".ENCHANTS.sort();\n"
                        + dataVersion + ".ANY_POTION = " + dataVersion
                        + ".POTIONS.slice().splice(0, 0, \"Any\");").getBytes());
            } else {
                out.write(("var keys = Object.keys(DATA_" + version + ");\n" +
                        "for (var i = 0; i < keys.length; i++) {\n" +
                        "    DATA_" + version + "[keys[i]].sort();\n" +
                        "}\n" +
                        "DATA_" + version + ".ANY_POTION = DATA_" + version
                        + ".POTIONS.slice().splice(0, 0, 'Any');").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        MockBukkit.unmock();
    }

    public static void writeEnumConstant(OutputStream out, Enum<?> enumConstant) throws IOException {
        try {
            Field field = enumConstant.getClass().getField(enumConstant.name());
            if (field.isAnnotationPresent(Deprecated.class)) {
                return;
            }
            String name = enumConstant.name();
            if (name.contains("LEGACY") || name.equals("UNKNOWN")) {
                return;
            }
            out.write(("        \"" + sentenceCase(enumConstant.name()) + "\",\n").getBytes());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static String sentenceCase(String value) {
        if (value.isEmpty()) {
            return value;
        }
        return value.substring(0, 1).replace('_', ' ').toUpperCase() + value.substring(1)
                .replace('_', ' ')
                .toLowerCase();
    }
}
