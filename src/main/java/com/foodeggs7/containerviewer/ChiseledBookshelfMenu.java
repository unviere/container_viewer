package com.foodeggs7.containerviewer;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;

public class ChiseledBookshelfMenu extends AbstractContainerMenu {

    private final ChiseledBookShelfBlockEntity bookshelf;

    private final BlockPos bookshelfPos;

    public ChiseledBookshelfMenu(int containerId, Inventory playerInventory, ChiseledBookShelfBlockEntity bookshelf, BlockPos bookshelfPos) {
        super(MenuType.GENERIC_3x3, containerId);

        this.bookshelf = bookshelf;
        this.bookshelfPos = bookshelfPos;

        for (int i = 0; i < 6; i++) {
            addSlot(new Slot(bookshelf, i, 8 + i * 18, 18) {

                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.is(ItemTags.BOOKSHELF_BOOKS);
                }
            });
        }

        SimpleContainer barrierContainer = new SimpleContainer(3);

        for (int i = 0; i < 3; i++) {
            barrierContainer.setItem(i, new ItemStack(Items.BARRIER));
        }

        for (int i = 0; i < 3; i++) {
            addSlot(new Slot(barrierContainer, i, 8 + (i + 6) * 18, 18) {

                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }

                @Override
                public boolean mayPickup(Player player) {
                    return false;
                }
            });
        }

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 51 + row * 18));
            }
        }

        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, 8 + column * 18, 109));
        }
    }

    public BlockPos getBookShelfPos() {
        return bookshelfPos;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.level().getBlockState(bookshelfPos).is(Blocks.CHISELED_BOOKSHELF);
    }
}