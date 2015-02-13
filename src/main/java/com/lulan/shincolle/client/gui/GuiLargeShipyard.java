package com.lulan.shincolle.client.gui;

import org.lwjgl.opengl.GL11;

import com.lulan.shincolle.client.inventory.ContainerLargeShipyard;
import com.lulan.shincolle.network.CreatePacketC2S;
import com.lulan.shincolle.reference.AttrID;
import com.lulan.shincolle.reference.GUIs;
import com.lulan.shincolle.reference.Reference;
import com.lulan.shincolle.tileentity.TileMultiGrudgeHeavy;
import com.lulan.shincolle.utility.GuiHelper;
import com.lulan.shincolle.utility.LogHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

/**SLOT POSITION
 * output(168,51) fuel bar(9,83 height=63) fuel color bar(208,64)
 * ship button(157,24) equip button(177,24) inv(25,116)
 * player inv(25,141) action bar(25,199)
 */
public class GuiLargeShipyard extends GuiContainer {

	private static final ResourceLocation TEXTURE_BG = new ResourceLocation(Reference.TEXTURES_GUI+"GuiLargeShipyard.png");
	private TileMultiGrudgeHeavy tile;
	private int xClick, yClick, selectMat, buildType, invMode;
	private String name, time, errorMsg, matBuild0, matBuild1, matBuild2, matBuild3, matStock0, matStock1, matStock2, matStock3;
	
	public GuiLargeShipyard(InventoryPlayer par1, TileMultiGrudgeHeavy par2) {
		super(new ContainerLargeShipyard(par1, par2));
		this.tile = par2;
		this.xSize = 208;
		this.ySize = 223;
	}
	
	//GUI前景: 文字 
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		//取得gui顯示名稱
		name = I18n.format("container.shincolle:LargeShipyard");
		time = this.tile.getBuildTimeString();
		matBuild0 = String.valueOf(this.tile.getMatBuild(0));
		matBuild1 = String.valueOf(this.tile.getMatBuild(1));
		matBuild2 = String.valueOf(this.tile.getMatBuild(2));
		matBuild3 = String.valueOf(this.tile.getMatBuild(3));
		matStock0 = String.valueOf(this.tile.getMatStock(0));
		matStock1 = String.valueOf(this.tile.getMatStock(1));
		matStock2 = String.valueOf(this.tile.getMatStock(2));
		matStock3 = String.valueOf(this.tile.getMatStock(3));
		
		//畫出字串 parm: string, x, y, color, (是否dropShadow)
		//畫出該方塊名稱, 位置: x=gui寬度的一半扣掉字串長度一半, y=6, 顏色為4210752
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		//畫出倒數時間
		this.fontRendererObj.drawString(time, 176 - this.fontRendererObj.getStringWidth(time) / 2, 77, 4210752);
		//畫出提示訊息
		if(tile.getPowerGoal() <= 0 && tile.getBuildType() != 0) {
			errorMsg = I18n.format("gui.shincolle:nomaterial");
			this.fontRendererObj.drawString(errorMsg, 105 - this.fontRendererObj.getStringWidth(errorMsg) / 2, 99, 16724787);
		}
		else if(!tile.hasPowerRemained()) {
			errorMsg = I18n.format("gui.shincolle:nofuel");
			this.fontRendererObj.drawString(errorMsg, 105 - this.fontRendererObj.getStringWidth(errorMsg) / 2, 99, 16724787);
		}
		//畫出數字
		this.fontRendererObj.drawString(matBuild0, 73 - this.fontRendererObj.getStringWidth(matBuild0) / 2, 20, 16777215);
		this.fontRendererObj.drawString(matBuild1, 73 - this.fontRendererObj.getStringWidth(matBuild1) / 2, 39, 16777215);
		this.fontRendererObj.drawString(matBuild2, 73 - this.fontRendererObj.getStringWidth(matBuild2) / 2, 58, 16777215);
		this.fontRendererObj.drawString(matBuild3, 73 - this.fontRendererObj.getStringWidth(matBuild3) / 2, 77, 16777215);
		this.fontRendererObj.drawString(matStock0, 125 - this.fontRendererObj.getStringWidth(matStock0) / 2, 20, 16776960);
		this.fontRendererObj.drawString(matStock1, 125 - this.fontRendererObj.getStringWidth(matStock1) / 2, 39, 16776960);
		this.fontRendererObj.drawString(matStock2, 125 - this.fontRendererObj.getStringWidth(matStock2) / 2, 58, 16776960);
		this.fontRendererObj.drawString(matStock3, 125 - this.fontRendererObj.getStringWidth(matStock3) / 2, 77, 16776960);
	}

	//GUI背景: 背景圖片
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1,int par2, int par3) {
//		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);	//RGBA
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_BG); //GUI圖檔
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);	//GUI大小設定
       
        //畫出fuel存量條
        int scaleBar; 
        if(tile.hasPowerRemained()) {
            scaleBar = tile.getPowerRemainingScaled(64);	//彩色進度條長度64	
            drawTexturedModalRect(guiLeft+9, guiTop+83-scaleBar, 208, 64-scaleBar, 12, scaleBar);
        }
        
        //畫出type選擇框 (157,24)
        if(tile.getBuildType() != 0) {
        	drawTexturedModalRect(guiLeft+137+tile.getBuildType()*20, guiTop+24, 208, 64, 18, 18);
        }
        
        //畫出資材選擇框 (27,14)
        drawTexturedModalRect(guiLeft+27, guiTop+14+tile.getSelectMat()*19, 208, 64, 18, 18);
        
        //畫出資材數量按鈕 (50,8)
        drawTexturedModalRect(guiLeft+50, guiTop+8+tile.getSelectMat()*19, 0, 223, 48, 30);
	
        //畫出inventory mode按鈕 (23,92)
        if(tile.getInvMode() == 1) {	//iutput mode
        	drawTexturedModalRect(guiLeft+23, guiTop+92, 208, 82, 25, 20);
        }   
	}
	
	//handle mouse click, @parm posX, posY, mouseKey (0:left 1:right 2:middle 3:...etc)
	@Override
	protected void mouseClicked(int posX, int posY, int mouseKey) {
        super.mouseClicked(posX, posY, mouseKey);
            
        //get click position
        xClick = posX - this.guiLeft;
        yClick = posY - this.guiTop;
        
        //build type button
        buildType = this.tile.getBuildType();
        invMode = this.tile.getInvMode();
        selectMat = this.tile.getSelectMat(); 
        
        //page 0 button
        int buttonClicked = GuiHelper.getButton(GUIs.LARGESHIPYARD, 0, xClick, yClick);
        switch(buttonClicked) {
        case 0:	//build ship
        	if(buildType == 1) {
        		buildType = 0;	//原本點ship 又點一次 則歸0
        	}
        	else {
        		buildType = 1;	//原本點其他按鈕, 則設成ship
        	}
        	LogHelper.info("DEBUG : GUI click: build large ship: ship "+buildType);
    		CreatePacketC2S.sendC2SGUIShipyardClick(this.tile, AttrID.B_Shipyard_Type, buildType, 0);
        	break;
        case 1:	//build equip
        	if(buildType == 2) {
        		buildType = 0;	//原本點equip 又點一次 則歸0
        	}
        	else {
        		buildType = 2;	//原本點其他按鈕, 則設成equip
        	}
        	LogHelper.info("DEBUG : GUI click: build large ship: equip "+buildType);
    		CreatePacketC2S.sendC2SGUIShipyardClick(this.tile, AttrID.B_Shipyard_Type, buildType, 0);
        	break;
        case 2:	//inventory mode
        	if(invMode == 0) {
        		invMode = 1;	//原本為input mode, 改為output mode
        	}
        	else {
        		invMode = 0;
        	}
        	LogHelper.info("DEBUG : GUI click: build large ship: invMode "+invMode);
    		CreatePacketC2S.sendC2SGUIShipyardClick(this.tile, AttrID.B_Shipyard_InvMode, invMode, 0);
        	break;
        case 3:	//select material grudge
        case 4: //abyssium
        case 5: //ammo
        case 6: //polymetal
        	selectMat = buttonClicked - 3;
        	LogHelper.info("DEBUG : GUI click: build large ship: select mats "+selectMat);
    		CreatePacketC2S.sendC2SGUIShipyardClick(this.tile, AttrID.B_Shipyard_SelectMat, selectMat, 0);
        	break;
        }//end page 0 button switch
        
        //other page button
        buttonClicked = GuiHelper.getButton(GUIs.LARGESHIPYARD, selectMat+1, xClick, yClick);
        switch(buttonClicked) {
        case 0:	//build mat +1000
        case 1:	//build mat +100
        case 2:	//build mat +10
        case 3:	//build mat +1
        case 4:	//build mat -1000
        case 5:	//build mat -100
        case 6:	//build mat -10
        case 7:	//build mat -1
        	LogHelper.info("DEBUG : GUI click: build large ship: inc/dec build materials "+(selectMat+1)+" "+buttonClicked);
    		CreatePacketC2S.sendC2SGUIShipyardClick(this.tile, AttrID.B_Shipyard_INCDEC, selectMat, buttonClicked);
        	break;	
        }//end other page button switch
        
        
	}

}

