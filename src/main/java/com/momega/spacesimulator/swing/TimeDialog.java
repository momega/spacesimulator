package com.momega.spacesimulator.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DateFormatter;

import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.opengl.DefaultWindow;
import com.momega.spacesimulator.utils.TimeUtils;

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
    private final JButton cancelButton;
    private final SpinnerDateModel timeModel;
    private final JProgressBar progressBar;
    private final DefaultWindow window;
	private JButton closeButton;

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
        ((Component)picker).setPreferredSize(new Dimension(300, 30));
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
        
        cancelButton = new JButton("Cancel");
        cancelButton.setIcon(SwingUtils.createImageIcon("/images/cancel.png"));
        
        closeButton = new JButton("Close");
        closeButton.setIcon(SwingUtils.createImageIcon("/images/accept.png"));
        
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    logger.info("Run worker thread");
                    model.start();
                    goButton.setEnabled(false);
                    cancelButton.setEnabled(true);
                    closeButton.setEnabled(false);
                } catch (Exception ex) {
                    logger.error("time worker thread failed", ex);
                } finally {

                }
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.stop();			
			}
		});
        cancelButton.setEnabled(false);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        progressBar.setStringPainted(true);
        progressBar.setString(TimeUtils.timeAsString(ModelHolder.getModel().getTime()));

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
                    .addComponent(cancelButton)
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
                                .addComponent(cancelButton)
                )
        );

        setResizable(false);
        pack();

        window.pauseAnimator();
        logger.info("Animation paused");
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
            	logger.info("resume animator");
                window.resumeAnimator();
            }
        });

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }

    class DateTimeModel implements ChangeListener {

        private final Calendar calendar;
        private Timestamp timestamp;
		private TimeWorker worker;

        DateTimeModel(Calendar calendar) {
            this.calendar = calendar;
            timestamp = TimeUtils.fromCalendar(calendar);
        }

        public void stop() {
        	if (worker != null) {
        		worker.cancel(true);
        	}
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
            logger.debug("Calendar = {}", calendar.getTime());
            updateButtons();
        }

        public void addTime(BigDecimal seconds) {
            timestamp = timestamp.add(seconds);
            calendar.setTimeInMillis(TimeUtils.toLinuxTime(timestamp));

            timeModel.setValue(calendar.getTime());
            dateModel.setValue(calendar);
            updateButtons();
        }
        
        protected void updateButtons() {
        	Timestamp modelTimestamp = ModelHolder.getModel().getTime();
        	boolean timeInFuture = (modelTimestamp.compareTo(timestamp)<=0);
        	goButton.setEnabled(timeInFuture);
        }

        public Calendar getCalendar() {
            return calendar;
        }

        public void start() {
            worker = new TimeWorker(timestamp);
            worker.execute();
        }
    }

    class TimeWorker extends SwingWorker<Void, Timestamp> {

        private final Timestamp endTime;

        public TimeWorker(Timestamp endTime) {
            this.endTime = endTime;
            progressBar.setMinimum(ModelHolder.getModel().getTime().getValue().intValue());
            progressBar.setMaximum(endTime.getValue().intValue());
        }
        
        @Override
        protected Void doInBackground() throws Exception {
            while(ModelHolder.getModel().getTime().compareTo(endTime)<=0) {
                Application.getInstance().next(true);
                publish(ModelHolder.getModel().getTime());
                if (Thread.interrupted()) {
                	return null;
                }
            }
            return null;
        }

        @Override
        protected void process(List<Timestamp> chunks) {
            if (!CollectionUtils.isEmpty(chunks)) {
                Timestamp val = chunks.get(chunks.size()-1);
                progressBar.setValue(val.getValue().intValue());
                progressBar.setString(TimeUtils.timeAsString(val));
            }
        }
        
        @Override
        protected void done() {
        	super.done();
        	goButton.setEnabled(true);
        	cancelButton.setEnabled(false);
        	closeButton.setEnabled(true);
        }
    }
}
