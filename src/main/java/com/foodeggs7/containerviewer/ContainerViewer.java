package com.foodeggs7.containerviewer;

import net.fabricmc.api.ModInitializer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class ContainerViewer implements ModInitializer {
	public static final String MOD_ID = "container-viewer";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
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

			if(!books.isEmpty()){
				System.out.println(books);

//				SimpleContainer container = new SimpleContainer(9) {
//					@Override
//					public void setItem(int slot, ItemStack stack) {
//						ItemStack oldStack = getItem(slot);
//
//						super.setItem(slot, stack);
//
//						if (slot < 6 && oldStack != stack) {
//							bookshelfEntity.setItem(slot, stack.copy());
//						}
//					}
//				};

//				for(int i = 0; i < books.size(); i++){
//					container.setItem(i, books.get(i).copy());
//				}
//
//				for (int i = 6; i < 9; i++) {
//					container.setItem(i, new ItemStack(Items.BARRIER));
//				}
//
//				MenuProvider provider = new SimpleMenuProvider((containerId, playerInventory, menuPlayer) ->
//						new ChiseledBookshelfMenu(containerId, playerInventory, container),
//
//						Component.literal("Chiseled bookshelf")
//				);
//
//				player.openMenu(provider);
//
//				return InteractionResult.SUCCESS;

				MenuProvider provider = new SimpleMenuProvider(
						(containerId, playerInventory, menuPlayer) ->
								new ChiseledBookshelfMenu(
										containerId,
										playerInventory,
										bookshelfEntity
								),
						Component.literal("Chiseled bookshelf")
				);

				player.openMenu(provider);

				return InteractionResult.SUCCESS;
			}


			return InteractionResult.PASS;
		});
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
