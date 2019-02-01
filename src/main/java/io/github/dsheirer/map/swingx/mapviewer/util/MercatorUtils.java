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
 * MercatorUtils.java
 *
 * Created on October 7, 2006, 6:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io.github.dsheirer.map.swingx.mapviewer.util;

/**
 * A utility class of methods that help when 
 * dealing with standard Mercator projections.
 * @author joshua.marinacci@sun.com
 */
public final class MercatorUtils
{

	/** 
	 * Creates a new instance of MercatorUtils 
	 */
	private MercatorUtils()
	{
	}

	/**
	 * @param longitudeDegrees the longitude in degrees
	 * @param radius the world radius in pixels
	 * @return the x value
	 */
	public static int longToX(double longitudeDegrees, double radius)
	{
		double longitude = Math.toRadians(longitudeDegrees);
		return (int) (radius * longitude);
	}

	/**
	 * @param latitudeDegrees the latitude in degrees
	 * @param radius the world radius in pixels
	 * @return the y value
	 */
	public static int latToY(double latitudeDegrees, double radius)
	{
		double latitude = Math.toRadians(latitudeDegrees);
		double y = radius / 2.0 * Math.log((1.0 + Math.sin(latitude)) / (1.0 - Math.sin(latitude)));
		return (int) y;
	}

	/**
	 * @param x the x value
	 * @param radius the world radius in pixels
	 * @return the longitude in degrees
	 */
	public static double xToLong(int x, double radius)
	{
		double longRadians = x / radius;
		double longDegrees = Math.toDegrees(longRadians);
		/*
		 * The user could have panned around the world a lot of times. Lat long goes from -180 to 180. So every time a
		 * user gets to 181 we want to subtract 360 degrees. Every time a user gets to -181 we want to add 360 degrees.
		 */
		int rotations = (int) Math.floor((longDegrees + 180) / 360);
		double longitude = longDegrees - (rotations * 360);
		return longitude;
	}

	/**
	 * @param y the y value
	 * @param radius the world radius in pixels
	 * @return the latitude in degrees
	 */
	public static double yToLat(int y, double radius)
	{
		double latitude = (Math.PI / 2) - (2 * Math.atan(Math.exp(-1.0 * y / radius)));
		return Math.toDegrees(latitude);
	}
}
