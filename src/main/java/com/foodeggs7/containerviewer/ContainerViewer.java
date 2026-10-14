package com.foodeggs7.containerviewer;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class ContainerViewer implements ModInitializer {
	public static final String MOD_ID = "container-viewer";

	@Override
	public void onInitialize() {
		UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
			BlockPos blockPos = hitResult.getBlockPos();

			if (!player.isShiftKeyDown()) {
				return InteractionResult.PASS;
			}

			BlockState blockState = level.getBlockState(blockPos);

			if (!blockState.is(Blocks.CHISELED_BOOKSHELF)) {
				return InteractionResult.PASS;
			}

			if (level.isClientSide()) {
				return InteractionResult.PASS;
			}


			ChiseledBookShelfBlockEntity bookshelfEntity = (ChiseledBookShelfBlockEntity) level.getBlockEntity(blockPos);

			NonNullList<ItemStack> books = bookshelfEntity.getItems();

			if (!books.isEmpty()){
				MenuProvider provider = new SimpleMenuProvider(
						(containerId, playerInventory, menuPlayer) ->
								new ChiseledBookshelfMenu(
										containerId,
										playerInventory,
										bookshelfEntity,
										blockPos
								),
						Component.literal("Chiseled bookshelf")
				);

				player.openMenu(provider);

				return InteractionResult.SUCCESS;
			}


			return InteractionResult.PASS;
		});

		PlayerBlockBreakEvents.AFTER.register((level, player, blockPos, blockState, blockEntity) -> {
			if (!blockState.is(Blocks.CHISELED_BOOKSHELF)) {
				return;
			}

			for (Player serverPlayer : level.players()) {
				if (serverPlayer.containerMenu instanceof ChiseledBookshelfMenu menu && menu.getBookShelfPos().equals(blockPos)) {
					serverPlayer.openMenu(null);
				}
			}
		});
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
