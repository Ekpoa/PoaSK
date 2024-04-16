package poa.poask.util.reflection;

import com.google.common.collect.ForwardingMultimap;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.json.JSONObject;
import poa.poask.PoaSK;
import poa.poask.util.reflection.common.CommonClassMethodFields;
import poa.poask.util.reflection.common.Letters;
import poa.poask.util.reflection.common.Reflection;
import poa.poask.util.reflection.common.SendPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FakePlayer {

    private static final Class<?> minecraftServerClass = CommonClassMethodFields.minecraftServerClass;

    private static final Method getServerMethod;
    private static final Object server;

    private static final Class<?> craftWorldClass = Reflection.getOBCClass("CraftWorld");
    private static final Method craftWorldGetHandle;
    private static Object level;


    private static final Class<?> serverPlayerClass = Reflection.getNMSClass("EntityPlayer", "net.minecraft.server.level");
    private static final Constructor<?> serverPlayerConstructor;


    private static final Class<?> gamePropertiesClass = Reflection.getNMSClass("Property", "com.mojang.authlib.properties");
    private static final Constructor<?> gamePropertiesConstructor;
    private static final Class<?> gameProfileClass = Reflection.getNMSClass("GameProfile", "com.mojang.authlib");
    private static final Method getGameProfileProperties;
    private static final Constructor<?> gameProfileConstructor;

    private static final Class<?> playerClass = Reflection.getNMSClass("EntityHuman", "net.minecraft.world.entity.player");
    private static final Method getGameProfileMethod;
    private static final Class<?> clientInformationClass = Reflection.getNMSClass("ClientInformation", "net.minecraft.server.level");
    private static final Object clientInformationDefault;

    private static final Class<?> entityClass = Reflection.getNMSClass("Entity", "net.minecraft.world.entity");
    private static final Method setPosMethod;


    private static final Class<?> infoUpdatePacketEntryClass = Reflection.getNMSClass("ClientboundPlayerInfoUpdatePacket$b", "net.minecraft.network.protocol.game");
    private static final Constructor<?> infoUpdatePacketEntryConstructor;
    private static final Class<?> gameTypeClass = Reflection.getNMSClass("EnumGamemode", "net.minecraft.world.level");
    private static final Object defaultGameType;
    private static final Object emptyComponent;
    private static final Class<?> remoteChatSessionDataClass = Reflection.getNMSClass("RemoteChatSession$a", "net.minecraft.network.chat");
    private static final Class<?> infoUpdatePacketActionClass = Reflection.getNMSClass("ClientboundPlayerInfoUpdatePacket$a", "net.minecraft.network.protocol.game");
    private static final Method getActionValueOf;
    private static final Object addPlayer;
    private static final Object updateListed;
    private static final Object updateLatency;

    private static final Class<?> infoUpdatePacketClass = Reflection.getNMSClass("ClientboundPlayerInfoUpdatePacket", "net.minecraft.network.protocol.game");
    private static final Constructor<?> infoUpdatePacketConstructor;

    private static final Class<?> addEntityPacketClass = CommonClassMethodFields.spawnPacketClass;
    private static final Constructor<?> addEntityPacketConstructor;

    //private static final Method getBukkitEntity;

    //private static final Class<?> bukkitEntityClass = Reflection.getOBCClass("CraftEntity");
    //private static final Method getEntityId;


    static {
        try {
            getServerMethod = minecraftServerClass.getMethod("getServer");
            server = getServerMethod.invoke(Bukkit.getServer());


            craftWorldGetHandle = craftWorldClass.getDeclaredMethod("getHandle");
            level = craftWorldGetHandle.invoke(Bukkit.getWorlds().get(0));

            serverPlayerConstructor = serverPlayerClass.getConstructor(minecraftServerClass, level.getClass(), gameProfileClass, clientInformationClass);

            gamePropertiesConstructor = gamePropertiesClass.getDeclaredConstructor(String.class, String.class, String.class);

            getGameProfileProperties = gameProfileClass.getDeclaredMethod("getProperties");
            gameProfileConstructor = gameProfileClass.getConstructor(UUID.class, String.class);
            getGameProfileMethod = playerClass.getDeclaredMethod(Letters.getGameProfile); //https://nms.screamingsandals.org/1.20.4/net/minecraft/world/entity/player/Player.html

            clientInformationDefault = clientInformationClass.getDeclaredMethod(Letters.createDefaultGameProfile).invoke(clientInformationClass); //https://nms.screamingsandals.org/1.20.4/net/minecraft/server/level/ClientInformation.html

            setPosMethod = entityClass.getDeclaredMethod(Letters.setPos, double.class, double.class, double.class); //https://nms.screamingsandals.org/1.20.4/net/minecraft/world/entity/Entity.html

            infoUpdatePacketEntryConstructor = infoUpdatePacketEntryClass.getDeclaredConstructor(UUID.class, gameProfileClass, boolean.class, int.class, gameTypeClass, CommonClassMethodFields.chatBaseComponent, remoteChatSessionDataClass);
            getActionValueOf = infoUpdatePacketActionClass.getDeclaredMethod("valueOf", String.class);


            addPlayer = getActionValueOf.invoke(infoUpdatePacketEntryClass, "ADD_PLAYER");
            updateListed = getActionValueOf.invoke(infoUpdatePacketEntryClass, "UPDATE_LISTED");
            updateLatency = getActionValueOf.invoke(infoUpdatePacketEntryClass, "UPDATE_LATENCY");


            defaultGameType = gameTypeClass.getDeclaredField(Letters.defaultGameType).get(null); //https://nms.screamingsandals.org/1.20.4/net/minecraft/world/level/GameType.html
            emptyComponent = CommonClassMethodFields.chatBaseComponent.getDeclaredMethod(Letters.emptyComponent).invoke(CommonClassMethodFields.chatBaseComponent); //https://nms.screamingsandals.org/1.20.4/net/minecraft/network/chat/Component.html

            infoUpdatePacketConstructor = infoUpdatePacketClass.getDeclaredConstructor(EnumSet.class, infoUpdatePacketEntryClass);

            addEntityPacketConstructor = addEntityPacketClass.getDeclaredConstructor(entityClass);

            //getBukkitEntity = serverPlayerClass.getDeclaredMethod("getBukkitEntity");
//            getEntityId = bukkitEntityClass.getDeclaredMethod("getEntityId");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConcurrentMap<Player, Map<UUID, Integer>> playerMapMap = new ConcurrentHashMap<>(); //handled in Login class



    public static void fakePlayer(List<Player> sendTo, String name, String skinName, Location location, boolean listed, int latency) {
        try {
            OfflinePlayer op = Bukkit.getOfflinePlayer(name);


            UUID uuid = UUID.randomUUID();
            Object gameProfile = gameProfileConstructor.newInstance(uuid, name);

            Object fakePlayer = serverPlayerConstructor.newInstance(server, level, gameProfile, clientInformationDefault);

            setPosMethod.invoke(fakePlayer, location.getX(), location.getY(), location.getZ());

            Object fakePlayerGameProfile = getGameProfileMethod.invoke(fakePlayer);


            if (skinName != null && !skinName.isEmpty()) {
                UUID skinUUID = Bukkit.getOfflinePlayer(skinName).getUniqueId();
                String s = fetchSkinURL(skinUUID);
                if (s != null && !s.isEmpty()) {
                    ForwardingMultimap<String, Object> properties = (ForwardingMultimap<String, Object>) getGameProfileProperties.invoke(fakePlayerGameProfile);
                    properties.removeAll("textures");

                    Object textures = gamePropertiesConstructor.newInstance("textures", s, fetchSkinSignature(skinUUID));
                    properties.put("textures", textures);
                }
            }



            Object entry = infoUpdatePacketEntryConstructor.newInstance(uuid, fakePlayerGameProfile, listed, latency, defaultGameType, emptyComponent, null);

            Object addPlayerPacket = infoUpdatePacketConstructor.newInstance(EnumSet.of((Enum) addPlayer), entry);

            Object updateListedPacket = infoUpdatePacketConstructor.newInstance(EnumSet.of((Enum) updateListed), entry);
            Object updateLatencyPacket = infoUpdatePacketConstructor.newInstance(EnumSet.of((Enum) updateLatency), entry);

            Object spawnEntityPacket = addEntityPacketConstructor.newInstance(fakePlayer);

            Method getBukkitEntity = fakePlayer.getClass().getDeclaredMethod("getBukkitEntity");

            Entity bukkitEntity = (Entity) getBukkitEntity.invoke(fakePlayer);
            int id = bukkitEntity.getEntityId();

            for (Player player : sendTo) {
                SendPacket.sendPacket(player, addPlayerPacket);
                Map<UUID, Integer> innerMap = new HashMap<>();
                innerMap.put(uuid, id);

                playerMapMap.put(player, innerMap);

                if (listed)
                    SendPacket.sendPacket(player, updateListedPacket);
                if (latency > 0)
                    SendPacket.sendPacket(player, updateLatencyPacket);

                Bukkit.getScheduler().runTaskLater(PoaSK.getInstance(), () -> SendPacket.sendPacket(player, spawnEntityPacket), 1L);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private static final Class<?> removePlayerPacket = Reflection.getNMSClass("ClientboundPlayerInfoRemovePacket", "net.minecraft.network.protocol.game");
    private static final Constructor<?> removePlayerPacketConstructor;

    static {
        try {
            removePlayerPacketConstructor = removePlayerPacket.getDeclaredConstructor(List.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    @SneakyThrows
    public static void removeFakePlayerPacket(List<Player> sendTo, List<UUID> uuids) {
        Object removeDataPacket = removePlayerPacketConstructor.newInstance(uuids);

        for (Player p : sendTo) {
            List<Integer> ids = new ArrayList<>();
            Map<UUID, Integer> playerMap = playerMapMap.get(p);
            for (UUID uuid : uuids) {
                ids.add(playerMap.get(uuid));
                playerMap.remove(uuid);
            }

            Object removeEntityPacket = RemoveEntityPacket.removeEntityPacket(ids.stream()
                    .mapToInt(Integer::intValue)
                    .toArray());
            SendPacket.sendPacket(p, removeDataPacket);
            SendPacket.sendPacket(p, removeEntityPacket);

            playerMapMap.put(p, playerMap);

        }
    }


    private static Map<UUID, HttpResponse<String>> skinMap = new HashMap<>();

    @SneakyThrows
    public static String fetchSkinURL(UUID uuid) {
        if (!skinMap.containsKey(uuid)) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            skinMap.put(uuid, response);
            Bukkit.getScheduler().runTaskLater(PoaSK.getInstance(), () -> skinMap.remove(uuid), 1200L);
        }
        HttpResponse<String> response = skinMap.get(uuid);
        if (response.statusCode() == 200) {
            JSONObject jsonObject = new JSONObject(response.body());
            String tr = jsonObject.getJSONArray("properties").getJSONObject(0).getString("value");
            return tr;
        }
        return null;
    }

    @SneakyThrows
    public static String fetchSkinSignature(UUID uuid) {
        HttpResponse<String> response = skinMap.get(uuid);
        if (response.statusCode() == 200) {
            JSONObject jsonObject = new JSONObject(response.body());
            String tr = jsonObject.getJSONArray("properties").getJSONObject(0).getString("signature");
            return tr;
        }
        return null;
    }

}
