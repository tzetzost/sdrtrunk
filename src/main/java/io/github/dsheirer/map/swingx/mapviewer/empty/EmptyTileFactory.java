/*
 * ******************************************************************************
 * sdrtrunk
 * Copyright (C) 2014-2019 Dennis Sheirer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 * *****************************************************************************
 */

/*
 * EmptyTileFactory.java
 *
 * Created on June 7, 2006, 4:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io.github.dsheirer.map.swingx.mapviewer.empty;

import io.github.dsheirer.map.swingx.mapviewer.Tile;
import io.github.dsheirer.map.swingx.mapviewer.TileFactory;
import io.github.dsheirer.map.swingx.mapviewer.TileFactoryInfo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * A null implementation of TileFactory. Draws empty areas.
 * @author joshy
 */
public class EmptyTileFactory extends TileFactory
{
	/** 
	 * The empty tile image. 
	 */
	private BufferedImage emptyTile;

	/** 
	 * Creates a new instance of EmptyTileFactory 
	 */
	public EmptyTileFactory()
	{
		this(new TileFactoryInfo("EmptyTileFactory 256x256", 1, 15, 17, 256, true, true, "", "x", "y", "z"));
	}

	/** 
	 * Creates a new instance of EmptyTileFactory using the specified info. 
	 * @param info the tile factory info
	 */
	public EmptyTileFactory(TileFactoryInfo info)
	{
		super(info);
		int tileSize = info.getTileSize(info.getMinimumZoomLevel());
		emptyTile = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = emptyTile.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, tileSize, tileSize);
		g.setColor(Color.WHITE);
		g.drawOval(10, 10, tileSize - 20, tileSize - 20);
		g.fillOval(70, 50, 20, 20);
		g.fillOval(tileSize - 90, 50, 20, 20);
		g.fillOval(tileSize / 2 - 10, tileSize / 2 - 10, 20, 20);
		g.dispose();
	}

	/**
	 * Gets an instance of an empty tile for the given tile position and zoom on the world map.
	 * @param x The tile's x position on the world map.
	 * @param y The tile's y position on the world map.
	 * @param zoom The current zoom level.
	 */
	@Override
	public Tile getTile(int x, int y, int zoom)
	{
		return new Tile(x, y, zoom)
		{
			@Override
			public synchronized boolean isLoaded()
			{
				return true;
			}

			@Override
			public BufferedImage getImage()
			{
				return emptyTile;
			}

		};
	}

	@Override
	public void dispose()
	{
		// noop
	}

	/**
	 * Override this method to load the tile using, for example, an <code>ExecutorService</code>.
	 * @param tile The tile to load.
	 */
	@Override
	protected void startLoading(Tile tile)
	{
		// noop
	}

}
