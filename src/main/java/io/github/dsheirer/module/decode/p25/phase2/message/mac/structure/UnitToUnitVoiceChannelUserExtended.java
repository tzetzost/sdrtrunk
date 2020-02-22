/*
 *
 *  * ******************************************************************************
 *  * Copyright (C) 2014-2020 Dennis Sheirer
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

package io.github.dsheirer.module.decode.p25.phase2.message.mac.structure;

import io.github.dsheirer.bits.CorrectedBinaryMessage;
import io.github.dsheirer.identifier.Identifier;
import io.github.dsheirer.module.decode.p25.identifier.radio.APCO25FullyQualifiedRadioIdentifier;
import io.github.dsheirer.module.decode.p25.identifier.radio.APCO25RadioIdentifier;
import io.github.dsheirer.module.decode.p25.phase2.message.mac.MacStructure;
import io.github.dsheirer.module.decode.p25.reference.VoiceServiceOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit-to-Unit voice channel user - extended format
 */
public class UnitToUnitVoiceChannelUserExtended extends MacStructure
{
    private static final int[] SERVICE_OPTIONS = {8, 9, 10, 11, 12, 13, 14, 15};
    private static final int[] TARGET_ADDRESS = {16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32,
        33, 34, 35, 36, 37, 38, 39};
    private static final int[] SOURCE_ADDRESS = {40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
        58, 59, 60, 61, 62, 63};
    private static final int[] FULLY_QUALIFIED_SOURCE_WACN = {64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77,
        78, 79, 80, 81, 82, 83};
    private static final int[] FULLY_QUALIFIED_SOURCE_SYSTEM = {84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95};
    private static final int[] FULLY_QUALIFIED_SOURCE_ID = {96, 97, 98, 99, 100, 101,
        102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119};

    private List<Identifier> mIdentifiers;
    private VoiceServiceOptions mServiceOptions;
    private Identifier mTargetAddress;
    private Identifier mSourceAddress;
    private APCO25FullyQualifiedRadioIdentifier mSourceSuid;

    /**
     * Constructs the message
     *
     * @param message containing the message bits
     * @param offset into the message for this structure
     */
    public UnitToUnitVoiceChannelUserExtended(CorrectedBinaryMessage message, int offset)
    {
        super(message, offset);
    }

    /**
     * Textual representation of this message
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getOpcode());
        sb.append(" TO:").append(getTargetAddress());
        sb.append(" FM:").append(getSourceAddress());
        sb.append(" SUID:").append(getSourceSuid());
        sb.append(" ").append(getServiceOptions());
        return sb.toString();
    }

    /**
     * Voice channel service options
     */
    public VoiceServiceOptions getServiceOptions()
    {
        if(mServiceOptions == null)
        {
            mServiceOptions = new VoiceServiceOptions(getMessage().getInt(SERVICE_OPTIONS, getOffset()));
        }

        return mServiceOptions;
    }

    /**
     * To Talkgroup
     */
    public Identifier getTargetAddress()
    {
        if(mTargetAddress == null)
        {
            mTargetAddress = APCO25RadioIdentifier.createTo(getMessage().getInt(TARGET_ADDRESS, getOffset()));
        }

        return mTargetAddress;
    }

    /**
     * From Radio Unit
     */
    public Identifier getSourceAddress()
    {
        if(mSourceAddress == null)
        {
            mSourceAddress = APCO25RadioIdentifier.createFrom(getMessage().getInt(SOURCE_ADDRESS, getOffset()));
        }

        return mSourceAddress;
    }

    public APCO25FullyQualifiedRadioIdentifier getSourceSuid()
    {
        if(mSourceSuid == null)
        {
            int wacn = getMessage().getInt(FULLY_QUALIFIED_SOURCE_WACN, getOffset());
            int system = getMessage().getInt(FULLY_QUALIFIED_SOURCE_SYSTEM, getOffset());
            int id = getMessage().getInt(FULLY_QUALIFIED_SOURCE_ID, getOffset());

            mSourceSuid = APCO25FullyQualifiedRadioIdentifier.createFrom(wacn, system, id);
        }

        return mSourceSuid;
    }

    @Override
    public List<Identifier> getIdentifiers()
    {
        if(mIdentifiers == null)
        {
            mIdentifiers = new ArrayList<>();
            mIdentifiers.add(getTargetAddress());
            mIdentifiers.add(getSourceAddress());
            mIdentifiers.add(getSourceSuid());
        }

        return mIdentifiers;
    }
}
