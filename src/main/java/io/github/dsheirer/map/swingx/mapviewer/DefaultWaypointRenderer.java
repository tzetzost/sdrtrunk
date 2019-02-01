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
 * WaypointRenderer.java
 *
 * Created on March 30, 2006, 5:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io.github.dsheirer.map.swingx.mapviewer;

import io.github.dsheirer.map.swingx.JXMapViewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * This is a standard waypoint renderer.
 * @author joshy
 */
public class DefaultWaypointRenderer implements WaypointRenderer<Waypoint>
{
	private final static Logger mLog = 
			LoggerFactory.getLogger( DefaultWaypointRenderer.class );

	private BufferedImage img = null;

	/**
	 * Uses a default waypoint image
	 */
	public DefaultWaypointRenderer()
	{
		try
		{
			img = ImageIO.read(getClass().getResource("resources/standard_waypoint.png"));
		}
		catch (Exception ex)
		{
			mLog.error("couldn't read standard_waypoint.png", ex );
		}
	}

	@Override
	public void paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint w)
	{
		if (img == null)
			return;

		Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());
		
		int x = (int)point.getX() -img.getWidth() / 2;
		int y = (int)point.getY() -img.getHeight();
		
		g.drawImage(img, x, y, null);
	}
}
