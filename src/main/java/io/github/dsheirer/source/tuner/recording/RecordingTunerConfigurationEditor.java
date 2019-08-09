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
package io.github.dsheirer.source.tuner.recording;

import io.github.dsheirer.gui.control.JFrequencyControl;
import io.github.dsheirer.source.tuner.configuration.TunerConfiguration;
import io.github.dsheirer.source.tuner.configuration.TunerConfigurationEditor;
import io.github.dsheirer.source.tuner.configuration.TunerConfigurationEvent;
import io.github.dsheirer.source.tuner.configuration.TunerConfigurationModel;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

public class RecordingTunerConfigurationEditor extends TunerConfigurationEditor
{
    private static final long serialVersionUID = 1L;

    private final static Logger mLog = LoggerFactory.getLogger(RecordingTunerConfigurationEditor.class);

    private JTextField mConfigurationName;
    private JFrequencyControl mFrequencyControl;
    private JFileChooser mRecordingChooserButton;
    private JLabel mRecordingPath;
    private boolean mLoading;

    private RecordingTunerController mController;

    public RecordingTunerConfigurationEditor(TunerConfigurationModel tunerConfigurationModel, RecordingTuner tuner)
    {
        super(tunerConfigurationModel);

        mController = tuner.getTunerController();

        init();
    }

    @Override
    public void setTunerLockState(boolean locked)
    {
        setControlsEnabled(!locked);
    }

    private RecordingTunerConfiguration getConfiguration()
    {
        if(hasItem())
        {
            return (RecordingTunerConfiguration)getItem();
        }

        return null;
    }

    private void init()
    {
        setLayout(new MigLayout("fill,wrap 4", "[right][grow,fill][right][grow,fill]",
            "[][][][][][][grow]"));

        add(new JLabel("Recording Tuner Configuration"), "span,align center");

        mConfigurationName = new JTextField();
        mConfigurationName.setEnabled(false);
        mConfigurationName.addFocusListener(new FocusListener()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                save();
            }

            @Override
            public void focusGained(FocusEvent e)
            {
            }
        });

        add(new JLabel("Name:"));
        add(mConfigurationName, "span 2");

        mFrequencyControl = new JFrequencyControl();
        mFrequencyControl.setEnabled(false);
        //TODO: add a change listener

        mRecordingChooserButton = new JFileChooser("Recording");
        mRecordingChooserButton.setEnabled(false);
        mRecordingChooserButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int returnVal = mRecordingChooserButton.showOpenDialog(RecordingTunerConfigurationEditor.this);

                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    File file = mRecordingChooserButton.getSelectedFile();
                    //TODO: implement change listener and set label
                }
                else
                {

                }
            }
        });
        add(mRecordingChooserButton);

        mRecordingPath = new JLabel();
        add(mRecordingPath);
    }

    /**
     * Sets each of the tuner configuration controls to the enabled argument state
     */
    private void setControlsEnabled(boolean enabled)
    {
        if(mConfigurationName.isEnabled() != enabled)
        {
            mConfigurationName.setEnabled(enabled);
        }

        if(mRecordingChooserButton.isEnabled() != enabled)
        {
            mRecordingChooserButton.setEnabled(enabled);
        }
    }

    @Override
    public void setItem(TunerConfiguration tunerConfiguration)
    {
        super.setItem(tunerConfiguration);

        //Toggle loading so that we don't fire a change event and schedule a settings file save
        mLoading = true;

        if(hasItem())
        {
            RecordingTunerConfiguration config = getConfiguration();

            setControlsEnabled(tunerConfiguration.isAssigned());
            mConfigurationName.setText(config.getName());
            mRecordingPath.setText(config.getPath());
            mFrequencyControl.setFrequency(config.getFrequency(), false);
        }
        else
        {
            setControlsEnabled(false);
            mConfigurationName.setText("");
            mRecordingPath.setText("");
        }

        mLoading = false;
    }

    @Override
    public void save()
    {
        if(hasItem() && !mLoading)
        {
            RecordingTunerConfiguration config = getConfiguration();

            config.setName(mConfigurationName.getText());
            config.setFrequency(mFrequencyControl.getFrequency());

            String path = mRecordingPath.getText();

            if(path != null && !path.isEmpty())
            {
                config.setPath(path);
            }

            getTunerConfigurationModel().broadcast(
                new TunerConfigurationEvent(getConfiguration(), TunerConfigurationEvent.Event.CHANGE));
        }
    }
}