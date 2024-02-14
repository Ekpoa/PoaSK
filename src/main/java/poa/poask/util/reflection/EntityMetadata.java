package poa.poask.util.reflection;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import poa.poask.util.reflection.common.CommonClassMethodFields;
import poa.poask.util.reflection.common.Letters;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static poa.poask.util.reflection.common.CommonClassMethodFields.*;

public class EntityMetadata {

    


    public static Object basePacketForEntity(int id, boolean fire, boolean invisible, boolean glow, String name, boolean nameVisible, boolean silent, boolean gravity, String pose) {
        EntityMetadata entityMetadata = new EntityMetadata(id);
        entityMetadata.setOnFire(fire);
        entityMetadata.setInvisible(invisible);
        entityMetadata.setGlow(glow);
        if (name != null)
            entityMetadata.setName(MiniMessage.miniMessage().deserialize(name));
        entityMetadata.setNameVisible(nameVisible);
        entityMetadata.setSilent(silent);
        entityMetadata.setGravity(!gravity); //cos packet is "has no gravity"
        entityMetadata.setPose(pose);
        return entityMetadata.build();
    }

    List<Object> dataList = new ArrayList<>();
    int id;


    boolean onFire = false;
    boolean invisible = false;
    boolean glow = false;


    public EntityMetadata(int id){
        this.id = id;
    }

    @SneakyThrows
    public void setOnFire(boolean onFire) {
        this.onFire = onFire;
        dataList.add(dataValueConstructor.newInstance(0, dataSerializersClass.getDeclaredField(Letters.dataSerializersByte).get(null), (byte) index0(onFire, invisible, glow))); //byte
    }

    @SneakyThrows
    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
        dataList.add(dataValueConstructor.newInstance(0, dataSerializersClass.getDeclaredField(Letters.dataSerializersByte).get(null), (byte) index0(onFire, invisible, glow))); //byte
    }

    @SneakyThrows
    public void setGlow(boolean glow) {
        this.glow = glow;
        dataList.add(dataValueConstructor.newInstance(0, dataSerializersClass.getDeclaredField(Letters.dataSerializersByte).get(null), (byte) index0(onFire, invisible, glow))); //byte
    }

    @SneakyThrows
    public void setName(String name) {
        dataList.add(dataValueConstructor.newInstance(2, dataSerializersClass.getDeclaredField(Letters.dataSerializersOptionalComponent).get(null), Optional.of(writeMethod.invoke(chatBaseComponent, name.replace("&", "§"))))); //optional component
        //dataList.add(declaredConstructor.newInstance(2, CMF.dataSerializersClass.getDeclaredField(Letters.dataSerializersOptionalComponent).get(null), Optional.of(writeMethod.invoke(StringConversion.nmsComponent(MiniMessage.miniMessage().deserialize(name)))))); //optional component
    }
    @SneakyThrows
    public void setName(Component componentName) {
        dataList.add(dataValueConstructor.newInstance(2, dataSerializersClass.getDeclaredField(Letters.dataSerializersOptionalComponent).get(null), Optional.of(ChatComponents.nmsComponent(componentName)))); //optional component
        //dataList.add(declaredConstructor.newInstance(2, CMF.dataSerializersClass.getDeclaredField(Letters.dataSerializersOptionalComponent).get(null), Optional.of(writeMethod.invoke(StringConversion.nmsComponent(MiniMessage.miniMessage().deserialize(name)))))); //optional component
    }

    @SneakyThrows
    public void setNameVisible(boolean nameVisible) {
        dataList.add(dataValueConstructor.newInstance(3, dataSerializersClass.getDeclaredField(Letters.dataSerializersBoolean).get(null), nameVisible));
    }

    @SneakyThrows
    public void setSilent(boolean silent) {
        dataList.add(dataValueConstructor.newInstance(4, dataSerializersClass.getDeclaredField(Letters.dataSerializersBoolean).get(null), silent));
    }

    @SneakyThrows
    public void setGravity(boolean hasGravity){
        dataList.add(dataValueConstructor.newInstance(5, dataSerializersClass.getDeclaredField(Letters.dataSerializersBoolean).get(null), hasGravity));
    }

    @SneakyThrows
    public void setPose(String pose) {
        Object[] enums = poseClass.getEnumConstants();

        Object p = null;

        for(Object o : enums) {
            if (o.toString().equals(Poses.valueOf(pose.toLowerCase()).string)) {
                p = o;
                break;
            }
            if(o.toString().equals(pose.toUpperCase())){
                p = o;
                break;
            }
        }

        dataList.add(dataValueConstructor.newInstance(6, dataSerializersClass.getDeclaredField(Letters.dataSerializersPose).get(null), p)); //pose
    }




    @SneakyThrows
    public void setItem(ItemStack itemStack){
        Object nmsItemStack = itemAsBukkitCopy(itemStack);
        dataList.add(dataValueConstructor.newInstance(8, dataSerializersClass.getDeclaredField(Letters.dataSerializersItemStack).get(null), nmsItemStack));
    }

    private static int index0(boolean isOnFire, boolean invisible, boolean glow) {
        byte b = 0;
        if (isOnFire)
            b = 0x01;
        if (invisible)
            b = (byte) (b + 0x20);
        if (glow)
            b = (byte) (b + 0x40);

        return b;
    }



    //ARMOR STAND STUFF

    boolean isSmall = false;
    boolean hasArms = false;
    boolean hasNoBase = false;
    boolean isMarker = false;

    private static int index15(boolean isSmall, boolean hasArms, boolean hasNoBase, boolean isMarker) {
        byte b = 0;
        if (isSmall)
            b = 0x01;
        if (hasArms)
            b = (byte) (b + 0x04);
        if (hasNoBase)
            b = (byte) (b + 0x08);
        if(isMarker)
            b = (byte) (b + 0x10);

        return b;
    }

    @SneakyThrows
    public void setIsSmall(boolean isSmall) {
        this.isSmall = isSmall;
        dataList.add(dataValueConstructor.newInstance(15, dataSerializersClass.getDeclaredField(Letters.dataSerializersByte).get(null), (byte) index15(isSmall, hasArms, hasNoBase, isMarker))); //byte
    }
    @SneakyThrows
    public void setHasArms(boolean hasArms) {
        this.hasArms = hasArms;
        dataList.add(dataValueConstructor.newInstance(15, dataSerializersClass.getDeclaredField(Letters.dataSerializersByte).get(null), (byte) index15(isSmall, hasArms, hasNoBase, isMarker))); //byte
    }
    @SneakyThrows
    public void setNoBase(boolean hasNoBase) {
        this.hasNoBase = hasNoBase;
        dataList.add(dataValueConstructor.newInstance(15, dataSerializersClass.getDeclaredField(Letters.dataSerializersByte).get(null), (byte) index15(isSmall, hasArms, hasNoBase, isMarker))); //byte
    }
    @SneakyThrows
    public void setIsMarker(boolean isMarker) {
        this.isMarker = isMarker;
        dataList.add(dataValueConstructor.newInstance(15, dataSerializersClass.getDeclaredField(Letters.dataSerializersByte).get(null), (byte) index15(isSmall, hasArms, hasNoBase, isMarker))); //byte
    }


    @SneakyThrows
    public void setHeadRotation(float x, float y, float z){
        dataList.add(dataValueConstructor.newInstance(16, dataSerializersClass.getDeclaredField(Letters.dataSerializersRotations).get(null), rotation(x, y, z)));
    }
    @SneakyThrows
    public void setBodyRotation(float x, float y, float z){
        dataList.add(dataValueConstructor.newInstance(17, dataSerializersClass.getDeclaredField(Letters.dataSerializersRotations).get(null), rotation(x, y, z)));
    }

    @SneakyThrows
    public void setLeftArmRotation(float x, float y, float z){
        dataList.add(dataValueConstructor.newInstance(18, dataSerializersClass.getDeclaredField(Letters.dataSerializersRotations).get(null), rotation(x, y, z)));
    }

    @SneakyThrows
    public void setRightArmRotation(float x, float y, float z){
        dataList.add(dataValueConstructor.newInstance(19, dataSerializersClass.getDeclaredField(Letters.dataSerializersRotations).get(null), rotation(x, y, z)));
    }

    @SneakyThrows
    public void setLeftLegRotation(float x, float y, float z){
        dataList.add(dataValueConstructor.newInstance(20, dataSerializersClass.getDeclaredField(Letters.dataSerializersRotations).get(null), rotation(x, y, z)));
    }

    @SneakyThrows
    public void setRightLegRotation(float x, float y, float z){
        dataList.add(dataValueConstructor.newInstance(21, dataSerializersClass.getDeclaredField(Letters.dataSerializersRotations).get(null), rotation(x, y, z)));
    }



    @SneakyThrows
    private static Object rotation(float x, float y, float z){
        return rotationConstructor.newInstance(x, y, z);
    }

    //TextDisplay

    @SneakyThrows
    public void setDisplayItem(ItemStack item) {
        Object nmsItemStack = itemAsBukkitCopy(item);
        dataList.add(dataValueConstructor.newInstance(23, dataSerializersClass.getDeclaredField(Letters.dataSerializersItemStack).get(null), nmsItemStack));
    }

    @SneakyThrows
    public void setInterpolationDelay(int delay){
        dataList.add(dataValueConstructor.newInstance(8, dataSerializersClass.getDeclaredField(Letters.getDataSerializersInt).get(null), delay));
    }

    @SneakyThrows
    public void setTransformationDuration(int duration){
        dataList.add(dataValueConstructor.newInstance(9, dataSerializersClass.getDeclaredField(Letters.getDataSerializersInt).get(null), duration));
    }

    @SneakyThrows
    public void setPosRotDuration(int duration){
        dataList.add(dataValueConstructor.newInstance(10, dataSerializersClass.getDeclaredField(Letters.getDataSerializersInt).get(null), duration));
    }

    @SneakyThrows
    public void setTranslation(float x, float y, float z){
        dataList.add(dataValueConstructor.newInstance(11, dataSerializersClass.getDeclaredField(Letters.getDataSerializersVector).get(null), new Vector3f(x,y,z)));
    }
    @SneakyThrows
    public void setScale(float x, float y, float z){
        dataList.add(dataValueConstructor.newInstance(12, dataSerializersClass.getDeclaredField(Letters.getDataSerializersVector).get(null), new Vector3f(x,y,z)));
    }
    @SneakyThrows
    public void setRotationLeft(double x, double y, double z, double w){
        dataList.add(dataValueConstructor.newInstance(13, dataSerializerClass.getDeclaredField(Letters.getDataSerializersQuaternion).get(null), new Quaternionf(x,y,z,w)));
    }

    @SneakyThrows
    public void setRotationRight(double x, double y, double z, double w){
        dataList.add(dataValueConstructor.newInstance(14, dataSerializerClass.getDeclaredField(Letters.getDataSerializersQuaternion).get(null), new Quaternionf(x,y,z,w)));
    }

    @SneakyThrows
    public void setBillboard(String string){
        byte b = 0;
        switch (string.toLowerCase()){
            case "fixed" -> b = (byte) 0;
            case "vertical" -> b = (byte) 1;
            case "horizontal" -> b = (byte) 2;
            case "center" -> b = (byte) 3;
        }
        dataList.add(dataValueConstructor.newInstance(15, dataSerializerClass.getDeclaredField(Letters.dataSerializersByte).get(null), b));
    }



    @SneakyThrows
    public void setBrightness(int brightness){
        dataList.add(dataValueConstructor.newInstance(16, dataSerializersClass.getDeclaredField(Letters.getDataSerializersInt).get(null), brightness));
    }


    @SneakyThrows
    public void setViewRange(float viewRange){
        dataList.add(dataValueConstructor.newInstance(17, dataSerializersClass.getDeclaredField(Letters.getDataSerializersFloat).get(null), viewRange));
    }

    @SneakyThrows
    public void setShadowRadius(float shadowRadius){
        dataList.add(dataValueConstructor.newInstance(18, dataSerializersClass.getDeclaredField(Letters.getDataSerializersFloat).get(null), shadowRadius));
    }

    @SneakyThrows
    public void setShadowStrength(float shadowStrength){
        dataList.add(dataValueConstructor.newInstance(19, dataSerializersClass.getDeclaredField(Letters.getDataSerializersFloat).get(null), shadowStrength));
    }

    @SneakyThrows
    public void setWidth(float width){
        dataList.add(dataValueConstructor.newInstance(20, dataSerializersClass.getDeclaredField(Letters.getDataSerializersFloat).get(null), width));
    }

    @SneakyThrows
    public void setHeight(float height){
        dataList.add(dataValueConstructor.newInstance(21, dataSerializersClass.getDeclaredField(Letters.getDataSerializersFloat).get(null), height));
    }

    @SneakyThrows
    public void setGlowOverride(int glowOverride){
        dataList.add(dataValueConstructor.newInstance(22, dataSerializersClass.getDeclaredField(Letters.getDataSerializersInt).get(null), glowOverride));
    }

    @SneakyThrows
    public void setText(Component component){
        dataList.add(dataValueConstructor.newInstance(23, dataSerializersClass.getDeclaredField(Letters.getDataSerializersComponent).get(null), ChatComponents.nmsComponent(component)));
    }

    public void setText(String miniMessageText){
        setText(MiniMessage.miniMessage().deserialize(miniMessageText));
    }



    //Build

    @SneakyThrows
    public Object build(){
        return metadataPacketConstructor.newInstance(id, dataList);
    }










    public enum Poses {
        standing("a"),
        sleeping("c"),
        swimming("d"),
        spin_attack("e"),
        crouching("f"),
        dying("h"),
        croaking("i"),
        using_tongue("j"),
        sitting("k"),
        roaring("l"),
        sniffing("m"),
        emerging("n"),
        digging("o");
        public final String string;

        Poses(String string) {
            this.string = string;
        }
    }


}
