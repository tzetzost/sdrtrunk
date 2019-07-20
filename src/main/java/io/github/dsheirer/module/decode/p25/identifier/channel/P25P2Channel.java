/*
 *
 *  * ******************************************************************************
 *  * Copyright (C) 2014-2019 Dennis Sheirer
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *  * *****************************************************************************
 *
 *
 */

package io.github.dsheirer.module.decode.p25.identifier.channel;

/**
 * P25 Phase II Channel.  Note: this channel is hard-coded for 2 timeslots, independent of the band identifier.
 */
public class P25P2Channel extends P25Channel
{
    public P25P2Channel(int bandIdentifier, int channelNumber)
    {
        super(bandIdentifier, channelNumber);
    }

    @Override
    public int getTimeslotCount()
    {
        return 2;
    }

    @Override
    public boolean isTDMAChannel()
    {
        return true;
    }
}
