package io.github.toberocat.pyfabric.command;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import dev.xpple.clientarguments.arguments.*;
import io.github.toberocat.pyfabric.server.BiReturnConsumer;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.argument.*;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;

public enum Type {
    SUB(null, null),
    BOOL(BoolArgumentType.bool(), BoolArgumentType::getBool),
    FLOAT(FloatArgumentType.floatArg(), FloatArgumentType::getFloat),
    DOUBLE(DoubleArgumentType.doubleArg(), DoubleArgumentType::getDouble),
    INTEGER(IntegerArgumentType.integer(), IntegerArgumentType::getInteger),
    LONG(LongArgumentType.longArg(), LongArgumentType::getLong),
    STRING(StringArgumentType.string(), StringArgumentType::getString),
    GREEDY_STRING(StringArgumentType.greedyString(), StringArgumentType::getString),
    BLOCK_POS(CBlockPosArgumentType.blockPos(), CBlockPosArgumentType::getCBlockPos),
    VEC3(CVec3ArgumentType.vec3(), CVec3ArgumentType::getCPosArgument),
    VEC2(CVec2ArgumentType.vec2(), CVec2ArgumentType::getCVec2),
    COLOR(CColorArgumentType.color(), CColorArgumentType::getCColor),
    COMPONENT(CTextArgumentType.text(), CTextArgumentType::getCTextArgument),
    MESSAGE(CMessageArgumentType.message(), CMessageArgumentType::getCMessage),
    NBT_COMPOUND_TAG(CNbtCompoundArgumentType.nbtCompound(), CMessageArgumentType::getCMessage),
    NBT_TAG(CNbtElementArgumentType.nbtElement(), CNbtElementArgumentType::getCNbtElement),
    OPERATION(COperationArgumentType.operation(), COperationArgumentType::getCOperation),
    PARTICLE(CParticleEffectArgumentType.particleEffect(), CParticleEffectArgumentType::getCParticle),
    ANGLE(CAngleArgumentType.angle(), CAngleArgumentType::getCAngle),
    ROTATION(CRotationArgumentType.rotation(), CRotationArgumentType::getCRotation),
    ITEM_SLOT(CItemSlotArgumentType.itemSlot(), CItemSlotArgumentType::getCItemSlot),
    DIMENSION(CDimensionArgumentType.dimension(), CDimensionArgumentType::getCDimensionArgument),
    TIME(CTimeArgumentType.time(), CTimeArgumentType::getCTime),
    UUID(CUuidArgumentType.uuid(),CUuidArgumentType::getCUuid),
    INT_RANGE(CNumberRangeArgumentType.intRange(), CNumberRangeArgumentType.IntRangeArgumentType::getCRangeArgument),
    FLOAT_RANGE(CNumberRangeArgumentType.floatRange(), CNumberRangeArgumentType.FloatRangeArgumentType::getCRangeArgument);

    ArgumentType<?> arg;
    BiReturnConsumer<CommandContext<FabricClientCommandSource>, String, ?> consumer;

    Type(ArgumentType<?> arg, BiReturnConsumer<CommandContext<FabricClientCommandSource>, String, ?> consumer) {
        this.arg = arg;
        this.consumer = consumer;
    }

    public BiReturnConsumer<CommandContext<FabricClientCommandSource>, String, ?> getConsumer() {
        return consumer;
    }

    public ArgumentType<?> argument() {
        return arg;
    }
}
