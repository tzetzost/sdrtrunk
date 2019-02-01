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
 * WMSTileFactory.java
 *
 * Created on October 7, 2006, 6:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io.github.dsheirer.map.swingx.mapviewer.wms;

import io.github.dsheirer.map.swingx.mapviewer.DefaultTileFactory;
import io.github.dsheirer.map.swingx.mapviewer.TileFactoryInfo;

/**
 * A tile factory that uses a WMS service.
 * @author joshy
 */
public class WMSTileFactory extends DefaultTileFactory
{
	/** 
	 * Creates a new instance of WMSTileFactory 
	 * @param wms the WMSService
	 */
	public WMSTileFactory(final WMSService wms)
	{
		// tile size and x/y orientation is r2l & t2b
		super(new TileFactoryInfo(0, 15, 17, 500, true, true, "", "x", "y", "zoom")
		{
			@Override
			public String getTileUrl(int x, int y, int zoom)
			{
				int zz = 17 - zoom;
				int z = 4;
				z = (int) Math.pow(2, (double) zz - 1);
				return wms.toWMSURL(x - z, z - 1 - y, zz, getTileSize(zoom));
			}

		});
	}

}
