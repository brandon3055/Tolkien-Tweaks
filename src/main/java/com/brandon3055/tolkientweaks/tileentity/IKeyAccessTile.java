package com.brandon3055.tolkientweaks.tileentity;

import com.brandon3055.brandonscore.blocks.TileBCBase;

/**
 * Created by brandon3055 on 16/04/2017.
 */
public interface IKeyAccessTile {

    TileBCBase getTile();

    boolean hasCK();

    boolean consumeKey();

    boolean hasMode();

    TileKeyStone.Mode mode();

    String getCode();

    boolean hasDelay();

    int getDelay();
}
