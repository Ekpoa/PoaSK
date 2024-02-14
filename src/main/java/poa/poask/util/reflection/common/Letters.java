package poa.poask.util.reflection.common;

import org.bukkit.Bukkit;

public class Letters {

    public static String getEntityPlayerConnectionField = letter("c", "c", "c", "b", "b", "b", "b", "b"); //https://nms.screamingsandals.org/1.19.4/net/minecraft/server/level/ServerPlayer.html
    public static String connectionSendPacket = letter("b","b", "a", "a", "a", "a", "a", "a"); //https://nms.screamingsandals.org/1.20.1/net/minecraft/server/network/ServerGamePacketListenerImpl.html

    public static String getEntityData = letter("an", "al", "aj", "aj", "al", "ai", "ai", "ai"); //https://nms.screamingsandals.org/1.19.4/net/minecraft/world/entity/Entity.html (get entity data)
    public static String dataWatcherInt2ObjectMap = letter("e","e", "e", "e", "e", "f", "f", "f"); //https://nms.screamingsandals.org/1.20/net/minecraft/network/syncher/SynchedEntityData.html
    public static String dataWatcher$ItemAccessor = letter("a","a", "a", "a", "a", "a", "a", "a"); //https://nms.screamingsandals.org/1.19.4/net/minecraft/network/syncher/SynchedEntityData$DataItem.html
    public static String dataWatcher$ItemValue = letter("b","b", "b", "b", "b", "b", "b", "b"); //see above


    public static String chatComponentLiteral = letter("b","b", "b", "b", "b", "b", "b", "b"); //https://nms.screamingsandals.org/1.19.4/net/minecraft/network/chat/Component.html
    public static String friendlyByteBufferWriteComponent = letter("a","a", "a", "a", "a", "a", "a", "a"); //https://nms.screamingsandals.org/1.19.4/net/minecraft/network/FriendlyByteBuf.html
    public static String friendlyByteBufferWriteUtf = letter("a", "a", "a", "a", "a", "a", "a", "a"); //see above
    public static String friendlyByteBufferWriteEnum = letter("a","a", "a", "a", "a", "a", "a", "a"); //see above
    public static String getFriendlyByteBufferWriteVarInt = letter("c","c", "d", "d", "d", "d", "d", "d"); //https://nms.screamingsandals.org/1.19.4/net/minecraft/network/FriendlyByteBuf.html
    public static String friendlyByteBuffWriteVarIntArray = letter("a","a", "a", "a", "a", "a", "a", "a");
    public static String chatFormattingGetByName = letter("b", "b", "b", "b", "b", "b", "b", "b"); //https://nms.screamingsandals.org/1.19.4/net/minecraft/ChatFormatting.html

    public static String connectionField = letter("c","c", "c", "b", "b", "b", "b", "b"); //https://nms.screamingsandals.org/1.19.4/net/minecraft/server/level/ServerPlayer.html
    public static String connectionField2 = letter("c","c", "h", "h", "b", "b", "b", "b"); //https://nms.screamingsandals.org/1.19.4/net/minecraft/server/network/ServerGamePacketListenerImpl.html
    public static String addressField = letter("o","o", "n", "n", "n", "n", "n", "n"); //https://nms.screamingsandals.org/1.19.4/net/minecraft/network/Connection.html


    public static String dataSerializersByte = letter("a","a", "a", "a", "a", "a", "a", "a"); //https://nms.screamingsandals.org/1.19.4/net/minecraft/network/syncher/EntityDataSerializers.html
    public static String dataSerializersOptionalComponent = letter("g","g", "g", "g", "g", "f", "f", "f"); //see above
    public static String dataSerializersBoolean = letter("k","k", "k", "k", "j", "i", "i", "i"); //see above
    public static String dataSerializersPose = letter("v", "v", "v", "v", "u", "t", "t", "t"); //see above
    public static String dataSerializersItemStack = letter("h","h", "h", "h", "h", "g", "g", "g"); //see above
    public static String dataSerializersRotations = letter("m","m", "m", "m", "l", "k", "k", "k"); //see above
    public static String getDataSerializersInt = letter("b", "b", "b", "b", "b", "b", "b", "b");
    public static String getDataSerializersVector = letter("A", "A", "A", "A", "A", "A", "A", "A");
    public static String getDataSerializersQuaternion = letter("B", "B", "B", "B", "B", "B", "B", "B");
    public static String getDataSerializersFloat = letter("d", "d", "d", "d", "B", "B", "B", "B");
    public static String getDataSerializersComponent = letter("f","f", "f", "f", "f", "e", "e", "e");


    public static String getMinecraftServerConnection = letter("ad","ad", "ad", "ad", "ac", "ad", "ad", "ad");  //https://nms.screamingsandals.org/1.20.2/net/minecraft/server/MinecraftServer.html
    public static String getRemoteAddress = letter("f", "f", "c", "c", "c", "c", "c", "c"); //https://nms.screamingsandals.org/1.20.2/net/minecraft/network/Connection.html
    public static String getConnections = letter("e","e", "e", "e", "e", "e", "e", "e"); //https://nms.screamingsandals.org/1.20.2/net/minecraft/server/network/ServerConnectionListener.html
    public static String minecraftServerConnectionAddress = letter("n","n", "n", "n", "n", "n", "n", "n"); //https://nms.screamingsandals.org/1.19.3/net/minecraft/network/Connection.html

    
    private static String letter(String v7, String v6, String v5, String v4, String v3, String v2, String v1, String v0) {
        return switch (getBukkitVersion()) {
            case "1203", "1204" -> v7;
            case "1202" -> v6;
            case "1201", "1200" -> v5;
            case "1194" -> v4;
            case "1193" -> v3;
            case "1192" -> v2;
            case "1191" -> v1;
            case "119" -> v0;
            default -> null;
        };
    }

    public static String getBukkitVersion() {
        return Bukkit.getMinecraftVersion().replaceAll("[.]", "");
    }

    private static String entityTypeLetter(String v5, String v4, String v3, String v210) {
        return switch (Bukkit.getMinecraftVersion().replaceAll("[.]", "")) {
            case "1193" -> v3;
            case "1194", "1200", "1201", "1202" -> v4;
            case "1203", "1204" -> v5;
            default -> v210;
        };
    }


    public static String Allay = entityTypeLetter("b" , "b", "b", "b");
    public static String AreaEffectCloud = entityTypeLetter("c","c", "c", "c");
    public static String ArmorStand = entityTypeLetter("d", "d", "d", "d");
    public static String TippedArrow = entityTypeLetter("e", "e", "e", "e");
    public static String Axolotl = entityTypeLetter("f", "f", "f", "f");
    public static String Bat = entityTypeLetter("g","g", "g", "g");
    public static String Bee = entityTypeLetter("h","h", "h", "h");
    public static String Blaze = entityTypeLetter("i", "i", "i", "i");
    public static String BlockDisplay = entityTypeLetter("j","j", "j", "null");
    public static String Boat = entityTypeLetter("k","k", "j", "j");
    public static String Breeze = entityTypeLetter("l","null", "null", "null");
    public static String ChestBoat = entityTypeLetter("p","o", "k", "k");
    public static String Cat = entityTypeLetter("n","m", "l", "l");
    public static String Camel = entityTypeLetter("m", "l", "l", "null");
    public static String CaveSpider = entityTypeLetter("o","n", "n", "m");
    public static String Chicken = entityTypeLetter("r","q", "o", "n");
    public static String Cod = entityTypeLetter("s","r", "p", "o");
    public static String Cow = entityTypeLetter("u", "t", "q", "p");
    public static String Creeper = entityTypeLetter("v", "u", "r", "q");
    public static String Dolphin = entityTypeLetter("w","v", "s", "r");
    public static String Donkey = entityTypeLetter("x","w", "t", "s");
    public static String DragonFireball = entityTypeLetter("y","x", "u", "t");
    public static String Drowned = entityTypeLetter("z", "y", "v", "u");
    public static String ElderGuardian = entityTypeLetter("B","A", "w", "v");
    public static String EndCrystal = entityTypeLetter("C", "B", "x", "w");
    public static String EnderDragon = entityTypeLetter("D", "C", "y", "x");
    public static String Enderman = entityTypeLetter("F", "E", "z", "y");
    public static String Endermite = entityTypeLetter("G", "F", "A", "z");
    public static String Evoker = entityTypeLetter("H", "G", "B", "A");
    public static String EvokerFangs = entityTypeLetter("I","H", "C", "B");
    public static String ExperienceOrb = entityTypeLetter("K","I", "D", "C");
    public static String FallingBlock = entityTypeLetter("M","L", "F", "E");
    public static String FireworkRocket = entityTypeLetter("N","M", "G", "F");
    public static String Fox = entityTypeLetter("O","N", "H", "G");
    public static String Frog = entityTypeLetter("P","O", "I", "H");
    public static String Ghast = entityTypeLetter("R", "Q", "J", "I");
    public static String Giant = entityTypeLetter("S", "R", "K", "J");
    public static String GlowItemFrame = entityTypeLetter("T","S", "L", "K");
    public static String GlowSquid = entityTypeLetter("U","T", "M", "L");
    public static String Goat = entityTypeLetter("V","U", "N", "M");
    public static String Guardian = entityTypeLetter("W","V", "O", "M");
    public static String Hoglin = entityTypeLetter("X","W", "P", "N");
    public static String Horse = entityTypeLetter("Z","Y", "Q", "O");
    public static String Husk = entityTypeLetter("aa","Z", "R", "P");
    public static String Illusioner = entityTypeLetter("ab","aa", "S", "Q");
    public static String IronGolem = entityTypeLetter("ad","ac", "T", "S");
    public static String DroppedItem = entityTypeLetter("ae","ad", "U", "T");
    public static String ItemDisplay = entityTypeLetter("af", "ae", "ae", "null");
    public static String ItemFrame = entityTypeLetter("ag","af", "V", "U");
    public static String Fireball = entityTypeLetter("ah","ag", "W", "V");
    public static String LeashKnot = entityTypeLetter("ai","ah", "X", "W");
    public static String LightningBolt = entityTypeLetter("aj","ai", "Y", "X");
    public static String Llama = entityTypeLetter("ak","aj", "Z", "Y");
    public static String LlamaSpit = entityTypeLetter("al","ak", "aa", "Z");
    public static String MagmaCube = entityTypeLetter("am","al", "ab", "aa");
    public static String Marker = entityTypeLetter("an","am", "ac", "ab");
    public static String Minecart = entityTypeLetter("ao","an", "ad", "ac");
    public static String ChestMinecart = entityTypeLetter("q","p", "ae", "ad");
    public static String CommandBlockMinecart = entityTypeLetter("t","s", "af", "ae");
    public static String FurnaceMinecart = entityTypeLetter("Q","P", "ag", "af");
    public static String HopperMinecart = entityTypeLetter("Y","X", "ah", "ag");
    public static String SpawnerMinecart = entityTypeLetter("aR","aQ", "ai", "ah");
    public static String TntMinecart = entityTypeLetter("ba","aZ", "aj", "ai");
    public static String TextDisplay = entityTypeLetter("aY", "aX", "ax", "null");
    public static String Mule = entityTypeLetter("aq","ap", "ak", "aj");
    public static String Mooshroom = entityTypeLetter("ap","ao", "al", "");
    public static String Ocelot = entityTypeLetter("ar","aq", "am", "ak");
    public static String Painting = entityTypeLetter("as","ar", "an", "am");
    public static String Panda = entityTypeLetter("at","as", "ao", "an");
    public static String Parrot = entityTypeLetter("au","at", "ap", "ao");
    public static String Phantom = entityTypeLetter("av","au", "aq", "ap");
    public static String Pig = entityTypeLetter("aw","av", "ar", "aq");
    public static String Piglin = entityTypeLetter("ax","aw", "as", "ar");
    public static String PiglinBrute = entityTypeLetter("ay","ax", "at", "as");
    public static String Pillager = entityTypeLetter("az","ay", "au", "at");
    public static String PolarBear = entityTypeLetter("aA","az", "av", "au");
    public static String Tnt = entityTypeLetter("aZ","aY", "aw", "av");
    public static String PufferFish = entityTypeLetter("aC","aB", "ax", "aw");
    public static String Rabbit = entityTypeLetter("aD","aC", "ay", "ax");
    public static String Ravager = entityTypeLetter("aE","aD", "az", "ay");
    public static String Salmon = entityTypeLetter("aF","eE", "aA", "az");
    public static String Sheep = entityTypeLetter("aG","aF", "aB", "aA");
    public static String Shulker = entityTypeLetter("aH","aG", "aC", "aB");
    public static String ShulkerBullet = entityTypeLetter("aI","aH", "aD", "aC");
    public static String Silverfish = entityTypeLetter("aJ","aI", "aE", "aD");
    public static String Skeleton = entityTypeLetter("aK","aJ", "aF", "aE");
    public static String SkeletonHorse = entityTypeLetter("aL","aK", "aG", "aF");
    public static String Slime = entityTypeLetter("aM","aL", "aH", "aG");
    public static String SmallFireball = entityTypeLetter("aN","aM", "aI", "aH");
    public static String Sniffer = entityTypeLetter("aO","aN", "0", "0");
    public static String SnowGolem = entityTypeLetter("aP","aO", "aJ", "aI");
    public static String Snowball = entityTypeLetter("aQ","aP", "aK", "aj");
    public static String SpectralArrow = entityTypeLetter("aS","aR", "aL", "aK");
    public static String Spider = entityTypeLetter("aT","aS", "aM", "aL");
    public static String Squid = entityTypeLetter("aU","aT", "aN", "aM");
    public static String Stray = entityTypeLetter("aV","aU", "aO", "aN");
    public static String Strider = entityTypeLetter("aW","aV", "aP", "aO");
    public static String Tadpole = entityTypeLetter("aX","aW", "aQ", "aP");
    public static String Egg = entityTypeLetter("A","z", "aR", "aQ");
    public static String EnderPearl = entityTypeLetter("E","D", "aS", "aR");
    public static String ExperienceBottle = entityTypeLetter("J","I", "aT", "aS");
    public static String Potion = entityTypeLetter("aB","aA", "aU", "aT");
    public static String Trident = entityTypeLetter("bc","bb", "aV", "aU");
    public static String TraderLlama = entityTypeLetter("bb","ba", "aW", "aV");
    public static String TropicalFish = entityTypeLetter("bd","bc", "aX", "aW");
    public static String Turtle = entityTypeLetter("be","bd", "aY", "aX");
    public static String Vex = entityTypeLetter("bf","be", "aZ", "aY");
    public static String Villager = entityTypeLetter("bg","bf", "ba", "aZ");
    public static String Vindicator = entityTypeLetter("bh","bg", "bb", "ba");
    public static String Warden = entityTypeLetter("bj","bi", "bd", "bc");
    public static String Witch = entityTypeLetter("bl","bj", "be", "bd");
    public static String Wither = entityTypeLetter("bm","bk", "bf", "be");
    public static String WitherSkeleton = entityTypeLetter("bn","bl", "bg", "bf");
    public static String WitherSkull = entityTypeLetter("bo","bm", "bh", "bg");
    public static String Wolf = entityTypeLetter("bp","bn", "bi", "bh");
    public static String Zoglin = entityTypeLetter("bq","bo", "bj", "bi");
    public static String Zombie = entityTypeLetter("br","bp", "bk", "bj");
    public static String ZombieHorse = entityTypeLetter("bs","bq", "bl", "bk");
    public static String ZombieVillager = entityTypeLetter("bt","br", "bm", "bl");
    public static String ZombifiedPiglin = entityTypeLetter("bu","bs", "bn", "bm");
    public static String Human = entityTypeLetter("bv","bt", "bo", "bn");
    public static String FishingBobber = entityTypeLetter("bw","bu", "bp", "bo");


//    enum entityTypes{
//        pig("a", "b");
//
//        final String string;
//        entityTypes(String string, String two){
//            if(Bukkit.getMinecraftVersion().replaceAll("[.]", "").equalsIgnoreCase("1193"))
//                this.string = two;
//            else
//                this.string = string;
//        }
//    }


}
