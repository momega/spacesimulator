package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.opengl.DefaultWindow;
import com.momega.spacesimulator.renderer.ModelChangeEvent;
import com.momega.spacesimulator.renderer.ModelChangeListener;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.utils.TimeUtils;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DateFormatter;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

/**
 * Time dialog is used to stop the animation and run the simulation to the given time
 * Created by martin on 10/3/14.
 */
public class TimeDialog extends JDialog {

	private static final long serialVersionUID = -747554595762069797L;
	private static final Logger logger = LoggerFactory.getLogger(TimeDialog.class);
    private final DateTimeModel model;
    private final UtilCalendarModel dateModel;
    private final JButton goButton;
    private final SpinnerDateModel timeModel;
    private final JProgressBar progressBar;
    private final DefaultWindow window;

    /**
     * Constructor
     * @param window the main window of the application
     * @param initTime the time initially displayed in the dialog
     */
    public TimeDialog(final DefaultWindow window, Timestamp initTime) {
        super();
        this.window = window;
        setTitle("Time");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModalityType(ModalityType.APPLICATION_MODAL);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        Timestamp timestamp = initTime;
        model = new DateTimeModel(TimeUtils.toCalendar(timestamp));

        dateModel = new UtilCalendarModel(model.getCalendar());
        JDatePicker picker = JDateComponentFactory.createJDatePicker(dateModel);
        picker.setTextEditable(true);
        picker.setShowYearButtons(false);
        dateModel.addChangeListener(model);

        JLabel timeLabel = new JLabel("Wait until:");
        JLabel progressLabel = new JLabel("Progress until:");

        timeModel = new SpinnerDateModel();
        timeModel.setValue(model.getCalendar().getTime());
        timeModel.addChangeListener(model);

        JSpinner spinner = new JSpinner(timeModel);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "HH:mm:ss");
        DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false); // this makes what you want
        formatter.setOverwriteMode(true);
        spinner.setEditor(editor);
        spinner.getInsets().set(5,5,5,5);

        goButton = new JButton("Go to");
        goButton.setIcon(SwingUtils.createImageIcon("/images/control_fastforward.png"));
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                while (!window.isPaused()) {
                    window.pauseAnimator();
                    logger.info("Animation paused");
                }
                logger.info("Animation paused, run thread");
                goButton.setEnabled(false);
                try {
                    model.start();
                } catch (Exception ex) {
                    logger.error("time worker thread failed", ex);
                } finally {
                    // do nothing
                }
            }
        });

        JPanel buttonsPanel = new JPanel(new FlowLayout());

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

        JPanel quickTimeButtons = new JPanel(new GridLayout(1,4));
        JButton minusOneHour = new JButton("-1h");
        JButton minus10Minutes = new JButton("-10min");
        JButton plus10Minutes = new JButton("+10min");
        JButton plusOneHour = new JButton("+1h");
        quickTimeButtons.add(minusOneHour);
        minusOneHour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addTime(BigDecimal.valueOf(-60*60.0));
            }
        });
        quickTimeButtons.add(minus10Minutes);
        minus10Minutes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addTime(BigDecimal.valueOf(-10*60.0));
            }
        });
        quickTimeButtons.add(plus10Minutes);
        plus10Minutes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addTime(BigDecimal.valueOf(10*60.0));
            }
        });
        quickTimeButtons.add(plusOneHour);
        plusOneHour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addTime(BigDecimal.valueOf(60*60.0));
            }
        });

        JPanel timePanel = new JPanel();
        FlowLayout fl = new FlowLayout(FlowLayout.TRAILING, 5, 0);
        timePanel.setLayout(fl);
        timePanel.add((Component) picker);
        timePanel.add(spinner);

        progressBar = new JProgressBar();

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(timeLabel)
                    .addComponent(progressLabel)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(timePanel)
                    .addComponent(progressBar)
                    .addComponent(quickTimeButtons)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(goButton)
                    .addComponent(closeButton)
                )
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(timeLabel)
                                .addComponent(timePanel)
                                .addComponent(goButton)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(quickTimeButtons)
                    .addComponent(closeButton)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(progressLabel)
                                .addComponent(progressBar)
                )
        );

        setResizable(false);
        pack();

        RendererModel.getInstance().addModelChangeListener(model);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                logger.info("model listener un-registered");
                RendererModel.getInstance().removeModelChangeListener(model);
            }
        });

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }

    class DateTimeModel implements ChangeListener, ModelChangeListener {

        private final Calendar calendar;
        private Timestamp timestamp;
        private boolean timeInFuture;

        DateTimeModel(Calendar calendar) {
            this.calendar = calendar;
            timestamp = TimeUtils.fromCalendar(calendar);
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            calendar.set(Calendar.YEAR, dateModel.getYear());
            calendar.set(Calendar.MONTH, dateModel.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH, dateModel.getDay());

            Calendar c = Calendar.getInstance();
            c.setTime((Date) timeModel.getValue());

            calendar.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, c.get(Calendar.SECOND));

            timestamp = TimeUtils.fromCalendar(calendar);
            logger.info("Calendar = {}", calendar.getTime());
        }

        public void addTime(BigDecimal seconds) {
            timestamp = timestamp.add(seconds);
            calendar.setTimeInMillis(TimeUtils.toLinuxTime(timestamp));

            timeModel.setValue(calendar.getTime());
            dateModel.setValue(calendar);
        }

        public Calendar getCalendar() {
            return calendar;
        }

        public void start() {
            progressBar.setMinimum(ModelHolder.getModel().getTime().getValue().intValue());
            progressBar.setMaximum(timestamp.getValue().intValue());

            TimeWorker worker = new TimeWorker(timestamp);
            worker.execute();
        }

        @Override
        public void modelChanged(ModelChangeEvent event) {
            Timestamp modelTimestamp = event.getTimestamp();
            timeInFuture = (modelTimestamp.compareTo(timestamp)<=0);
            goButton.setEnabled(timeInFuture);
        }
    }

    class TimeWorker extends SwingWorker<Void, Timestamp> {

        private final Timestamp endTime;

        public TimeWorker(Timestamp endTime) {
            this.endTime = endTime;
        }

        @Override
        protected Void doInBackground() throws Exception {
            while(ModelHolder.getModel().getTime().compareTo(endTime)<=0) {
                Application.getInstance().getModelWorker().next();
                publish(ModelHolder.getModel().getTime());
            }
            return null;
        }

        @Override
        protected void process(List<Timestamp> chunks) {
            if (!CollectionUtils.isEmpty(chunks)) {
                Timestamp val = chunks.get(chunks.size()-1);
                progressBar.setValue(val.getValue().intValue());
            }
        }

        @Override
        protected void done() {
            super.done();
            window.resumeAnimator();
        }
    }
}
