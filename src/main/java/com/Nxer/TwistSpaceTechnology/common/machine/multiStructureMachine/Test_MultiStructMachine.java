/*
 * package com.Nxer.TwistSpaceTechnology.common.machine.multiStructureMachine;
 * // spotless:off
public class Test_MultiStructMachine
    extends GT_TileEntity_MultiStructureMachine<Test_MultiStructMachine>{
    public Test_MultiStructMachine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        setShape();
        horizontalOffSet = 1;
        verticalOffSet = 1;
        depthOffSet = 0;
        isMainBlock = true;
    }

    // region Processing Logic

    @Override
    protected boolean isEnablePerfectOverclock(){
        return true;
    }
    @Override
    protected float getSpeedBonus() {
        return 1.0F/16;
    }
    @Override
    protected int getMaxParallelRecipes() {
        return Integer.MAX_VALUE;
    }

    // endregion

    protected int mode = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";


    @Override
    public IStructureDefinition<Test_MultiStructMachine> getStructureDefinition() {
        return StructureDefinition
            .<Test_MultiStructMachine>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
            .addElement(
                'A',
                GT_HatchElementBuilder.<Test_MultiStructMachine>builder()
                    .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Energy.or(ExoticEnergy), Maintenance)
                    .adder(Test_MultiStructMachine::addToMachineList)
                    .casingIndex(176)
                    .dot(1)
                    .buildAndChain(GregTech_API.sBlockCasings8, 0))
            .build();
    }

    @Override
    public void setShape() {
        shape = new String[][] {
            {"AAA","AAA","AAA"},
            {"A~A","AAA","AAA"},
            {"AAA","AAA","AAA"}
        };
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return this.survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            realBudget,
            source,
            actor,
            false,
            true);
    }


    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        if (mode == 0) return GTCMRecipe.instance.IntensifyChemicalDistorterRecipes;
        return GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes;
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (this.mode + 1) % 2;
            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("IntensifyChemicalDistorter.mode." + this.mode));
        }
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }


    @Override
    public StructureDefinition.Builder<Test_MultiStructMachine> additionalStructureDefinition() {
        return null;
    }


    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mode", mode);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mode = aNBT.getInteger("mode");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GTCM_TestMultiMachine(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
                                 int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[1][48] };
    }

    // Tooltips
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(TextLocalization.Tooltip_ICD_MachineType)
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .beginStructureBlock(11, 13, 11, false)
            .addController(textFrontBottom)
            .addCasingInfoRange(textCasing, 8, 26, false)
            .addInputHatch(textAnyCasing, 1)
            .addOutputHatch(textAnyCasing, 1)
            .addInputBus(textAnyCasing, 2)
            .addOutputBus(textAnyCasing, 2)
            .addMaintenanceHatch(textAnyCasing, 2)
            .addEnergyHatch(textAnyCasing, 3)
            .toolTipFinisher(ModName);
        return tt;
    }

}

// spotless:on
 */
