package com.hxrdsxk.item.payload;

import com.hxrdsxk.AllINeedMod;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record ActivateTotemPayload() implements CustomPayload {
	public static final Id<ActivateTotemPayload> ID = new Id<>(Identifier.of(AllINeedMod.MOD_ID, "activate_totem"));

	public static final PacketCodec<PacketByteBuf, ActivateTotemPayload> CODEC = PacketCodec.unit(new ActivateTotemPayload());

	public static final Type<PacketByteBuf, ActivateTotemPayload> TYPE = new Type<>(ID, CODEC);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
