package com.momega.spacesimulator.swing;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.momega.spacesimulator.context.Application;
import com.momega.spacesimulator.context.ModelHolder;
import com.momega.spacesimulator.model.HistoryPoint;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.RunStep;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.opengl.DefaultWindow;
import com.momega.spacesimulator.renderer.RendererModel;
import com.momega.spacesimulator.service.HistoryPointListener;
import com.momega.spacesimulator.service.HistoryPointService;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * Time dialog is used to stop the animation and run the simulation to the given time
 * Created by martin on 10/3/14.
 */
public class TimeDialog extends JDialog {

	private static final long serialVersionUID = -747554595762069797L;
	private static final Logger logger = LoggerFactory.getLogger(TimeDialog.class);
    private final DateTimeModel model;
    private final JButton goButton;
    private final JButton cancelButton;
    private final JProgressBar progressBar;
	private JButton closeButton;
	private JList<HistoryPoint> historyEvents;
	private DefaultListModel<HistoryPoint> eventModel;
	private TimeWorker worker;

    /**
     * Constructor
     * @param window the main window of the application
     * @param initTime the time initially displayed in the dialog
     */
    public TimeDialog(final DefaultWindow window, Timestamp initTime) {
        setTitle("Time");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModalityType(ModalityType.APPLICATION_MODAL);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel timeLabel = new JLabel("Wait until:");
        JLabel progressLabel = new JLabel("Progress until:");
        JLabel lblEvents = new JLabel("Events:");

        goButton = new JButton("Go to");
        goButton.setIcon(SwingUtils.createImageIcon("/images/control_fastforward.png"));
        
        cancelButton = new JButton("Cancel");
        cancelButton.setIcon(SwingUtils.createImageIcon("/images/cancel.png"));
        
        closeButton = new JButton("Close");
        closeButton.setIcon(SwingUtils.createImageIcon("/images/accept.png"));
        
        eventModel = new DefaultListModel<>();
        historyEvents = new JList<>(eventModel);
        historyEvents.setCellRenderer(new HistoryPointListRenderer());
        historyEvents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroller = new JScrollPane(historyEvents);
        listScroller.setPreferredSize(new Dimension(250, 100));
        
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    logger.info("Run worker thread");
                    start();
                    goButton.setEnabled(false);
                    cancelButton.setEnabled(true);
                    closeButton.setEnabled(false);
                } catch (Exception ex) {
                    logger.error("time worker thread failed", ex);
                }
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();			
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
                model.addTime(-60*60);
            }
        });
        quickTimeButtons.add(minus10Minutes);
        minus10Minutes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addTime(-10*60);
            }
        });
        quickTimeButtons.add(plus10Minutes);
        plus10Minutes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addTime(10*60);
            }
        });
        quickTimeButtons.add(plusOneHour);
        plusOneHour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addTime(60*60);
            }
        });

        model = new DateTimeModel(TimeUtils.toCalendar(initTime));
        TimePanel timePanel = new TimePanel();
        timePanel.setModel(model);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString(TimeUtils.timeAsString(ModelHolder.getModel().getTime()));

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(timeLabel)
                    .addComponent(progressLabel)
                    .addComponent(lblEvents)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(timePanel)
                    .addComponent(progressBar)
                    .addComponent(quickTimeButtons)
                    .addComponent(listScroller)
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
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
    				.addComponent(lblEvents)
                    .addComponent(listScroller)
                )
        );

        setResizable(false);
        pack();

        window.pauseAnimator();
        logger.info("Animation paused");
        
        final HistoryPointService historyPointService = Application.getInstance().getService(HistoryPointService.class);
        final HistoryPointListener historyPointListener = new HistoryPointListener() {
			@Override
			public void historyPointCreated(final HistoryPoint historyPoint) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						eventModel.addElement(historyPoint);
                        historyEvents.ensureIndexIsVisible(eventModel.getSize()-1);
					}
				});
			}
			
			@Override
			public boolean supports(HistoryPoint historyPoint) {
				return true;
			}
		};

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
            	logger.info("resume animator");
            	historyPointService.removedHistoryPointListener(historyPointListener);
                window.resumeAnimator();
            }
        });
        
        historyPointService.addHistoryPointListener(historyPointListener);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        
        model.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateButtons();
			}
		});
    }
    
	protected void updateButtons() {
		Timestamp modelTimestamp = ModelHolder.getModel().getTime();
		Timestamp timestamp = TimeUtils.fromCalendar(model.getCalendar());
		boolean timeInFuture = (modelTimestamp.compareTo(timestamp)<=0);
		goButton.setEnabled(timeInFuture);
	}   
	
	public void start() {
		Timestamp timestamp = TimeUtils.fromCalendar(model.getCalendar());
		worker = new TimeWorker(timestamp);
		worker.execute();
	}
	
	public void stop() {
		if (worker != null) {
			worker.cancel(true);
		}
	}

    class TimeWorker extends SwingWorker<Void, Timestamp> {

        private final Timestamp endTime;

        public TimeWorker(Timestamp endTime) {
            this.endTime = endTime;
            progressBar.setMinimum(ModelHolder.getModel().getTime().toInteger());
            progressBar.setMaximum(endTime.toInteger());
        }
        
        @Override
        protected Void doInBackground() throws Exception {
        	double warpFactor = RendererModel.getInstance().getWarpFactor();
        	Model model = ModelHolder.getModel();
        	RunStep runStep = RunStep.create(model.getTime(), warpFactor, true);
            while(model.getTime().compareTo(endTime)<=0) {
                Application.getInstance().next(model, runStep);
                publish(model.getTime());
                if (Thread.interrupted()) {
                	return null;
                }
                runStep.next();
            }
            return null;
        }

        @Override
        protected void process(List<Timestamp> chunks) {
            if (!CollectionUtils.isEmpty(chunks)) {
                Timestamp val = chunks.get(chunks.size()-1);
                progressBar.setValue(val.toInteger());
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
