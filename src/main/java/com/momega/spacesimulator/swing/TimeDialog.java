package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.opengl.DefaultWindow;
import com.momega.spacesimulator.utils.TimeUtils;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.DateFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by martin on 10/3/14.
 */
public class TimeDialog extends JDialog {

    private static final Logger logger = LoggerFactory.getLogger(TimeDialog.class);
    private final DefaultWindow window;

    public TimeDialog(final DefaultWindow window) {
        super();
        this.window = window;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModalityType(ModalityType.APPLICATION_MODAL);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        Timestamp timestamp = ModelHolder.getModel().getTime();
        Calendar calendar = TimeUtils.getCalendar(timestamp);
        final UtilCalendarModel dateModel = new UtilCalendarModel(calendar);

        JDatePicker picker = new JDateComponentFactory().createJDatePicker(dateModel);
        picker.setTextEditable(true);
        picker.setShowYearButtons(false);

        JLabel timeLabel = new JLabel("Wait until:");

        final SpinnerDateModel timeModel = new SpinnerDateModel();
        timeModel.setValue(calendar.getTime());

        JSpinner spinner = new JSpinner(timeModel);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "HH:mm:ss");
        DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false); // this makes what you want
        formatter.setOverwriteMode(true);

        spinner.setEditor(editor);

        JButton goButton = new JButton("Go to");
        goButton.setIcon(SwingUtils.createImageIcon("/images/control_fastforward.png"));
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar cal = dateModel.getValue();
                Date date2 = (Date) timeModel.getValue();
                logger.info("date = {}, {}", cal.getTime(), date2);
                window.pauseAnimator();
            }
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());

        JButton closeButton = new JButton("Close");
        closeButton.setIcon(SwingUtils.createImageIcon("/images/accept.png"));
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.resumeAnimator();
                dispose();
            }
        });
        buttonsPanel.add(closeButton);

        JPanel timePanel = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.TRAILING, 0, 0);
        timePanel.setLayout(fl);
        timePanel.add((Component) picker);
        timePanel.add(spinner);

        JProgressBar progressBar = new JProgressBar();

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(timeLabel)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(timePanel)
                                .addComponent(progressBar)
                                .addComponent(buttonsPanel))
                        .addComponent(goButton)
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(timeLabel)
                                .addComponent(timePanel)
                                .addComponent(goButton))
                        .addComponent(progressBar)
                        .addComponent(buttonsPanel)
        );

        setLocationRelativeTo(null);
        pack();
    }
}
