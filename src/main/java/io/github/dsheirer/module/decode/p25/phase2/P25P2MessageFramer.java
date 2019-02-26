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
package io.github.dsheirer.module.decode.p25.phase2;

import io.github.dsheirer.bits.BitSetFullException;
import io.github.dsheirer.bits.CorrectedBinaryMessage;
import io.github.dsheirer.dsp.psk.pll.IPhaseLockedLoop;
import io.github.dsheirer.dsp.symbol.Dibit;
import io.github.dsheirer.dsp.symbol.ISyncDetectListener;
import io.github.dsheirer.message.Message;
import io.github.dsheirer.message.SyncLossMessage;
import io.github.dsheirer.module.decode.DecoderType;
import io.github.dsheirer.module.decode.p25.phase1.message.pdu.PDUSequence;
import io.github.dsheirer.module.decode.p25.phase2.enumeration.DataUnitID;
import io.github.dsheirer.protocol.Protocol;
import io.github.dsheirer.record.binary.BinaryReader;
import io.github.dsheirer.sample.Listener;
import io.github.dsheirer.sample.buffer.ReusableByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * P25 Sync Detector and Message Framer.  Includes capability to detect PLL out-of-phase lock errors
 * and issue phase corrections.
 */
public class P25P2MessageFramer implements Listener<Dibit>, IP25P2DataUnitDetectListener
{
    private final static Logger mLog = LoggerFactory.getLogger(P25P2MessageFramer.class);

    private P25P2DataUnitDetector mDataUnitDetector;
    private Listener<Message> mMessageListener;

    private boolean mAssemblingMessage = false;
    private CorrectedBinaryMessage mBinaryMessage;
    private DataUnitID mDataUnitID;
    private PDUSequence mPDUSequence;
    private int[] mCorrectedNID;
    private int mNAC;
    private int mStatusSymbolDibitCounter = 0;
    private int mTrailingDibitsToSuppress = 0;
    private double mBitRate;
    private long mCurrentTime = System.currentTimeMillis();
    private ISyncDetectListener mSyncDetectListener;

    public P25P2MessageFramer(IPhaseLockedLoop phaseLockedLoop, int bitRate)
    {
        mDataUnitDetector = new P25P2DataUnitDetector(this, phaseLockedLoop);
        mBitRate = bitRate;
    }

    public P25P2MessageFramer(int bitRate)
    {
        this(null, bitRate);
    }

    /**
     * Sets the sample rate for the sync detector
     */
    public void setSampleRate(double sampleRate)
    {
        mDataUnitDetector.setSampleRate(sampleRate);
    }

    /**
     * Registers a sync detect listener to be notified each time a sync pattern and NID are detected.
     */
    public void setSyncDetectListener(ISyncDetectListener syncDetectListener)
    {
        mSyncDetectListener = syncDetectListener;
    }

    /**
     * Current timestamp or timestamp of incoming message buffers that is continuously updated to as
     * close as possible to the bits processed for the expected baud rate.
     *
     * @return
     */
    private long getTimestamp()
    {
        return mCurrentTime;
    }

    /**
     * Sets the current time.  This should be invoked by an incoming message buffer stream.
     *
     * @param currentTime
     */
    public void setCurrentTime(long currentTime)
    {
        mCurrentTime = currentTime;
    }

    /**
     * Updates the current timestamp based on the number of bits processed versus the bit rate per second
     * in order to keep an accurate running timestamp to use for timestamped message creation.
     *
     * @param bitsProcessed thus far
     */
    private void updateBitsProcessed(int bitsProcessed)
    {
        if(bitsProcessed > 0)
        {
            mCurrentTime += (long)((double)bitsProcessed / mBitRate * 1000.0);
        }
    }

    /**
     * Registers the listener for messages produced by this message framer
     *
     * @param messageListener to receive framed and decoded messages
     */
    public void setListener(Listener<Message> messageListener)
    {
        mMessageListener = messageListener;
    }

    public P25P2DataUnitDetector getDataUnitDetector()
    {
        return mDataUnitDetector;
    }

    /**
     * Primary method for streaming decoded symbol dibits for message framing.
     *
     * @param dibit to process
     */
    @Override
    public void receive(Dibit dibit)
    {
        if(mAssemblingMessage)
        {
            //Strip out the status symbol dibit after every 70 bits or 35 dibits
            if(mStatusSymbolDibitCounter == 35)
            {
                if(mAssemblingMessage)
                {
                    //Send status dibit to channel status processor to identify ISP or OSP channel
//                    mChannelStatusProcessor.receive(dibit);
                }
                mStatusSymbolDibitCounter = 0;

                return;
            }

            mStatusSymbolDibitCounter++;

            try
            {
                mBinaryMessage.add(dibit.getBit1());
                mBinaryMessage.add(dibit.getBit2());
            }
            catch(BitSetFullException bsfe)
            {
//                mLog.debug("Message full exception - unexpected");

                //Reset so that we can start over again
                reset(0);
            }

            if(mBinaryMessage.isFull())
            {
                //TDU's have a trailing status symbol that has to be removed -- set flag to true to suppress it.
                if(mDataUnitID.hasTrailingStatusDibit())
                {
                    mTrailingDibitsToSuppress = 1;
                }

                dispatchMessage();
            }
        }
        else
        {
            //Suppress any trailing nulls or status dibits that follow certain DUID sequences
            if(mTrailingDibitsToSuppress > 0)
            {
                mTrailingDibitsToSuppress--;
                updateBitsProcessed(2);
                return;
            }

            mDataUnitDetector.receive(dibit);
        }
    }

    private void dispatchMessage()
    {
        if(mMessageListener != null)
        {
            switch(mDataUnitID)
            {
                default:
//                    P25Message message = P25MessageFactory.create(mDataUnitID, mNAC, getTimestamp(), mBinaryMessage);
//                    mMessageListener.receive(message);
//                    reset(mDataUnitID.getMessageLength());
                    break;
            }
        }
        else
        {
            reset(0);
        }
    }

    private void reset(int bitsProcessed)
    {
        updateBitsProcessed(bitsProcessed);
        mPDUSequence = null;
        mBinaryMessage = null;
        mAssemblingMessage = false;
        mDataUnitID = null;
        mNAC = 0;
        mDataUnitDetector.reset();
        mStatusSymbolDibitCounter = 0;
    }

    /**
     * Primary method for streaming decoded symbol byte arrays.
     *
     * @param buffer to process into a stream of dibits for processing.
     */
    public void receive(ReusableByteBuffer buffer)
    {
        //Updates current timestamp to the timestamp from the incoming buffer
        setCurrentTime(buffer.getTimestamp());

        for(byte value : buffer.getBytes())
        {
            for(int x = 0; x <= 3; x++)
            {
                receive(Dibit.parse(value, x));
            }
        }

        buffer.decrementUserCount();
    }

    @Override
    public void dataUnitDetected(DataUnitID dataUnitID, int nac, int bitErrors, int discardedDibits, int[] correctedNid)
    {
        if(discardedDibits > 0)
        {
            dispatchSyncLoss(discardedDibits * 2);
        }

        if(dataUnitID.getMessageLength() < 0)
        {
            dispatchSyncLoss(112); //Sync (48) and Nid (64)
            return;
        }

        if(mSyncDetectListener != null)
        {
            mSyncDetectListener.syncDetected(bitErrors);
        }

        mDataUnitID = dataUnitID;
        mNAC = nac;
        mCorrectedNID = correctedNid;
        mBinaryMessage = new CorrectedBinaryMessage(dataUnitID.getMessageLength());
        mBinaryMessage.incrementCorrectedBitCount(bitErrors);

        mAssemblingMessage = true;
        mStatusSymbolDibitCounter = 21;
    }

    @Override
    public void syncLost(int bitsProcessed)
    {
        dispatchSyncLoss(bitsProcessed);

        if(mSyncDetectListener != null)
        {
            mSyncDetectListener.syncLost();
        }
    }

    private void dispatchSyncLoss(int bitsProcessed)
    {
        //Updates current timestamp according to the number of bits procesed
        updateBitsProcessed(bitsProcessed);

        if(bitsProcessed > 0 && mMessageListener != null)
        {
            mMessageListener.receive(new SyncLossMessage(getTimestamp(), bitsProcessed, Protocol.APCO25));
        }
    }

    public static void main(String[] args)
    {
        P25P2MessageFramer messageFramer = new P25P2MessageFramer(null, DecoderType.P25_PHASE1.getProtocol().getBitRate());
        messageFramer.setSyncDetectListener(new ISyncDetectListener()
        {
            @Override
            public void syncDetected(int bitErrors)
            {
                mLog.debug("Sync Detected!");
            }

            @Override
            public void syncLost()
            {

            }
        });
        messageFramer.setListener(new Listener<Message>()
        {
            @Override
            public void receive(Message message)
            {
                mLog.debug(message.toString());
            }
        });

        Path path = Paths.get("/media/denny/500G1EXT4/RadioRecordings/20190224_101332_12000BPS_APCO25PHASE2_DFWAirport_Site_857_3875_baseband_20181213_223136.bits");

        try(BinaryReader reader = new BinaryReader(path, 200))
        {
            while(reader.hasNext())
            {
                messageFramer.receive(reader.next());
            }
        }
        catch(Exception ioe)
        {
            ioe.printStackTrace();
        }
    }
}
