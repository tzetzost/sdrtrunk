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

package io.github.dsheirer.audio.codec.mbe;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dsheirer.module.decode.p25.audio.VoiceFrame;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reader for MBE call sequence recordings
 */
public class MBECallSequenceReader
{
    public static List<String> getAudioFrames(Path path) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        Object obj = mapper.readValue(path.toFile(), MBECallSequence.class);

        if(obj instanceof MBECallSequence)
        {
            MBECallSequence sequence = (MBECallSequence)obj;

            List<String> audioFrames = new ArrayList<>();

            for(VoiceFrame voiceFrame: sequence.getVoiceFrames())
            {
                audioFrames.add(voiceFrame.getFrame());
            }

            return audioFrames;
        }

        return Collections.emptyList();
    }

    public static void main(String[] args)
    {
//        Path directory = Path.of("/home/denny/Documents/TMR/APCO25/AMBE Codec/MBE Dongle Recordings");
        Path directory = Path.of("/home/denny/Documents/TMR/APCO25/IMBE Codec/MBE Dongle Generated Recordings");

        if(Files.isDirectory(directory))
        {
            File[] files = directory.toFile().listFiles(new FileFilter()
            {
                @Override
                public boolean accept(File pathname)
                {
                    return pathname.toString().endsWith(".mbe");
                }
            });

            for(File file: files)
            {
                System.out.println("Converting file: " + file.toString());
                try
                {
                    List<String> frames = MBECallSequenceReader.getAudioFrames(file.toPath());

                    StringBuilder sb = new StringBuilder();

                    for(String frame: frames)
                    {
                        sb.append("\"").append(frame).append("\",");
                    }

                    Path output = Path.of(file.toString().replace(".mbe", "_frames.txt"));
                    Files.writeString(output, sb.toString());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
