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
 * Waypoint.java
 *
 * Created on March 30, 2006, 5:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io.github.dsheirer.map.swingx.mapviewer;

import io.github.dsheirer.map.beans.AbstractBean;

/**
 * @author joshy
 */
public class DefaultWaypoint extends AbstractBean implements Waypoint 
{
	private GeoPosition position;

	/** 
	 * Creates a new instance of Waypoint 
	 */
	public DefaultWaypoint()
	{
		this(new GeoPosition(0, 0));
	}

	/**
	 * @param latitude the latitude
	 * @param longitude the longitude
	 */
	public DefaultWaypoint(double latitude, double longitude)
	{
		this(new GeoPosition(latitude, longitude));
	}

	/**
	 * @param coord the geo coordinate
	 */
	public DefaultWaypoint(GeoPosition coord)
	{
		this.position = coord;
	}

	@Override
	public GeoPosition getPosition()
	{
		return position;
	}

	/**
	 * Set a new GeoPosition for this Waypoint
	 * @param coordinate a new position
	 */
	public void setPosition(GeoPosition coordinate)
	{
		GeoPosition old = getPosition();
		this.position = coordinate;
		firePropertyChange("position", old, getPosition());
	}

}
