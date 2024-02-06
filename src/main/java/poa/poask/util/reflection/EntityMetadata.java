package poa.poask.util.reflection;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import poa.poask.util.reflection.common.CommonClassMethodFields;
import poa.poask.util.reflection.common.Letters;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(0, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersByte).get(null), (byte) index0(onFire, invisible, glow))); //byte
    }

    @SneakyThrows
    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(0, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersByte).get(null), (byte) index0(onFire, invisible, glow))); //byte
    }

    @SneakyThrows
    public void setGlow(boolean glow) {
        this.glow = glow;
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(0, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersByte).get(null), (byte) index0(onFire, invisible, glow))); //byte
    }

    @SneakyThrows
    public void setName(String name) {
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(2, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersOptionalComponent).get(null), Optional.of(CommonClassMethodFields.writeMethod.invoke(CommonClassMethodFields.chatBaseComponent, name.replace("&", "§"))))); //optional component
        //dataList.add(declaredConstructor.newInstance(2, CMF.dataSerializersClass.getDeclaredField(Letters.dataSerializersOptionalComponent).get(null), Optional.of(writeMethod.invoke(StringConversion.nmsComponent(MiniMessage.miniMessage().deserialize(name)))))); //optional component
    }
    @SneakyThrows
    public void setName(Component componentName) {
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(2, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersOptionalComponent).get(null), Optional.of(ChatComponents.nmsComponent(componentName)))); //optional component
        //dataList.add(declaredConstructor.newInstance(2, CMF.dataSerializersClass.getDeclaredField(Letters.dataSerializersOptionalComponent).get(null), Optional.of(writeMethod.invoke(StringConversion.nmsComponent(MiniMessage.miniMessage().deserialize(name)))))); //optional component
    }

    @SneakyThrows
    public void setNameVisible(boolean nameVisible) {
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(3, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersBoolean).get(null), nameVisible));
    }

    @SneakyThrows
    public void setSilent(boolean silent) {
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(4, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersBoolean).get(null), silent));
    }

    @SneakyThrows
    public void setGravity(boolean hasGravity){
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(5, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersBoolean).get(null), hasGravity));
    }

    @SneakyThrows
    public void setPose(String pose) {
        Object[] enums = CommonClassMethodFields.poseClass.getEnumConstants();

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

        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(6, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersPose).get(null), p)); //pose
    }



    @SneakyThrows
    public void setItem(ItemStack itemStack){
        Object nmsItemStack  = CommonClassMethodFields.fromBukkitCopy.invoke(CommonClassMethodFields.itemStackClass, itemStack);
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(8, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersItemStack).get(null), nmsItemStack));
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
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(15, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersByte).get(null), (byte) index15(isSmall, hasArms, hasNoBase, isMarker))); //byte
    }
    @SneakyThrows
    public void setHasArms(boolean hasArms) {
        this.hasArms = hasArms;
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(15, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersByte).get(null), (byte) index15(isSmall, hasArms, hasNoBase, isMarker))); //byte
    }
    @SneakyThrows
    public void setNoBase(boolean hasNoBase) {
        this.hasNoBase = hasNoBase;
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(15, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersByte).get(null), (byte) index15(isSmall, hasArms, hasNoBase, isMarker))); //byte
    }
    @SneakyThrows
    public void setIsMarker(boolean isMarker) {
        this.isMarker = isMarker;
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(15, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersByte).get(null), (byte) index15(isSmall, hasArms, hasNoBase, isMarker))); //byte
    }


    @SneakyThrows
    public void setHeadRotation(float x, float y, float z){
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(16, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersRotations).get(null), rotation(x, y, z)));
    }
    @SneakyThrows
    public void setBodyRotation(float x, float y, float z){
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(17, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersRotations).get(null), rotation(x, y, z)));
    }

    @SneakyThrows
    public void setLeftArmRotation(float x, float y, float z){
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(18, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersRotations).get(null), rotation(x, y, z)));
    }

    @SneakyThrows
    public void setRightArmRotation(float x, float y, float z){
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(19, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersRotations).get(null), rotation(x, y, z)));
    }

    @SneakyThrows
    public void setLeftLegRotation(float x, float y, float z){
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(20, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersRotations).get(null), rotation(x, y, z)));
    }

    @SneakyThrows
    public void setRightLegRotation(float x, float y, float z){
        dataList.add(CommonClassMethodFields.dataValueConstructor.newInstance(21, CommonClassMethodFields.dataSerializersClass.getDeclaredField(Letters.dataSerializersRotations).get(null), rotation(x, y, z)));
    }



    @SneakyThrows
    private static Object rotation(float x, float y, float z){
        return CommonClassMethodFields.rotationConstructor.newInstance(x, y, z);
    }



    @SneakyThrows
    public Object build(){
        return CommonClassMethodFields.metadataPacketConstructor.newInstance(id, dataList);
    }


    //TextDisplay

    


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
