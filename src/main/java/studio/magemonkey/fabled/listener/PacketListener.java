package studio.magemonkey.fabled.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import studio.magemonkey.fabled.Fabled;
import studio.magemonkey.fabled.api.util.BuffData;
import studio.magemonkey.fabled.api.util.BuffManager;
import studio.magemonkey.fabled.api.util.BuffType;
import studio.magemonkey.fabled.hook.ProtocolLibHook;

import java.util.ArrayList;
import java.util.List;

public class PacketListener extends FabledListener {
    private ProtocolLibHook     protocolLib;
    private List<PacketAdapter> packetListeners = new ArrayList<>();

    @Override
    public void init() {
        protocolLib = new ProtocolLibHook(Fabled.inst());
        addListener(new EntityEquipmentPacketAdapter(ListenerPriority.HIGH, PacketType.Play.Server.ENTITY_EQUIPMENT));
    }

    private void addListener(PacketAdapter listener) {
        packetListeners.add(listener);
        protocolLib.register(listener);
    }

    /**
     * Sends entity equipment packet
     *
     * @param owner
     */
    public static void updateEquipment(Player owner) {
        ProtocolLibHook protocolLib = new ProtocolLibHook(Fabled.inst());
        PacketContainer packet =
                protocolLib.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        PlayerInventory inv = owner.getInventory();
        packet.getIntegers().write(0, owner.getEntityId());
        List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairList = packet.getSlotStackPairLists().read(0);
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, inv.getHelmet()));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, inv.getChestplate()));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, inv.getLeggings()));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.FEET, inv.getBoots()));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, inv.getItemInMainHand()));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, inv.getItemInOffHand()));
        packet.getSlotStackPairLists().write(0, pairList);
        protocolLib.broadcastToNearby(owner, packet);
    }

    @Override
    public void cleanup() {
        protocolLib.unregister(packetListeners);
    }

    /**
     * Used for true invisibility mechanic
     */
    private class EntityEquipmentPacketAdapter extends PacketAdapter {

        public EntityEquipmentPacketAdapter(ListenerPriority listenerPriority, PacketType... types) {
            super(Fabled.inst(), listenerPriority, types);
        }

        @Override
        public void onPacketSending(PacketEvent event) {
            Entity entity = protocolLib.getProtocolManager()
                    .getEntityFromID(event.getPlayer().getWorld(), event.getPacket().getIntegers().read(0));
            if (!(entity instanceof LivingEntity)) return;

            BuffData data = BuffManager.getBuffData((LivingEntity) entity, false);
            if (data == null || !data.isActive(BuffType.INVISIBILITY) || !((LivingEntity) entity).hasPotionEffect(
                    PotionEffectType.INVISIBILITY)) return;

            PacketContainer                              packet   = event.getPacket();
            ItemStack                                    air      = new ItemStack(Material.AIR);
            List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairList = packet.getSlotStackPairLists().read(0);
            pairList.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, air));
            pairList.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, air));
            pairList.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, air));
            pairList.add(new Pair<>(EnumWrappers.ItemSlot.FEET, air));
            pairList.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, air));
            pairList.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, air));
            packet.getSlotStackPairLists().write(0, pairList);
        }
    }
}
