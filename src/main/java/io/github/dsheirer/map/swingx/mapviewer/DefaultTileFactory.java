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
 * DefaultTileFactory.java
 *
 * Created on June 27, 2006, 2:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io.github.dsheirer.map.swingx.mapviewer;

/**
 * A tile factory which configures itself using a TileFactoryInfo object and uses a Google Maps like mercator
 * projection.
 * @author joshy
 */
public class DefaultTileFactory extends AbstractTileFactory
{
	/**
	 * Creates a new instance of DefaultTileFactory using the spcified TileFactoryInfo
	 * @param info a TileFactoryInfo to configure this TileFactory
	 */
	public DefaultTileFactory(TileFactoryInfo info)
	{
		super(info);
	}

}
