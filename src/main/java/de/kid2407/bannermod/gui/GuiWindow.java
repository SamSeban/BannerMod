package de.kid2407.bannermod.gui;

import de.kid2407.bannermod.BannerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

/**
 * User: Tobias Franz
 * Date: 28.04.2020
 * Time: 17:33
 */
public class GuiWindow extends GuiContainer {

    private final World world;
    private final int x, y, z;
    private final EntityPlayer entity;
    private final GuiContainerMod guiContainerMod;

    private final int guiWidth = 176;
    private final int guiHeight = 156;

    public GuiWindow(World world, int x, int y, int z, EntityPlayer entity, GuiContainerMod guiContainerMod) {
        super(guiContainerMod);
        this.guiContainerMod = guiContainerMod;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.entity = entity;
        this.xSize = guiWidth;
        this.ySize = guiHeight;
    }

    private static final ResourceLocation texture = new ResourceLocation("bannermod", "textures/testgui.png");

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        zLevel = 100.0F;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        this.fontRenderer.drawString("Banner Gui", 7, 6, -16751104);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseX < getGuiLeft() || mouseX > getGuiLeft() + guiWidth || mouseY < getGuiTop() || mouseY > getGuiTop() + guiHeight) {
            return;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        if (slotIn != null) {
            BannerModInventoryTypes inventoryType = guiContainerMod.getCurrentInventoryType();
            if (inventoryType == BannerModInventoryTypes.DEFAULT) {
                if (slotId == GuiContainerMod.textColorSlot) {
                    guiContainerMod.loadInventory(BannerModInventoryTypes.TEXT_COLOR);
                } else if (slotId == GuiContainerMod.baseColorSlot) {
                    guiContainerMod.loadInventory(BannerModInventoryTypes.BASE_COLOR);
                } else if (slotId < 36) {
                    guiContainerMod.giveBannerToPlayer(slotId);
                }
            } else if (inventoryType == BannerModInventoryTypes.TEXT_COLOR || inventoryType == BannerModInventoryTypes.BASE_COLOR) {
                if (slotId < 16) {
                    if (inventoryType == BannerModInventoryTypes.TEXT_COLOR) {
                        guiContainerMod.setTextColorForPlayer(slotIn.getStack().getMetadata());
                    } else {
                        guiContainerMod.setBaseColorForPlayer(slotIn.getStack().getMetadata());
                    }
                }
            } else {
                BannerMod.logger.error("Unknown InventoryType");
            }
        } else {
            BannerMod.logger.info("Slot is null");
        }
        super.handleMouseClick(slotIn, slotId, mouseButton, type);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Minecraft.getMinecraft().gameSettings.keyBindDrop.getKeyCode()) {
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
}
