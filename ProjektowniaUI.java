import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.concurrent.Semaphore;
import java.util.Random;
import java.util.LinkedList;
import java.util.Iterator;

class ProjektowniaUI extends JFrame
{
private Projektownia company = new Projektownia();
private JPanel controlPanel = new JPanel();
private JPanel eventsPanel = new JPanel();
private JPanel innerEventsPanel = new JPanel();
private JPanel innerControlPanel = new JPanel();
private JPanel queueInfoPanel = new JPanel();
private JPanel emploeeNorthPanel = new JPanel();
private JPanel emploeeSouthPanel = new JPanel();
private JPanel projectNorthPanel = new JPanel();
private JPanel projectSouthPanel = new JPanel();
private JTextField newEmploeeTextField = new JTextField("0");
private JTextField newProjectSizeTextField = new JTextField("0");
private JTextField newProjectTimeTextField = new JTextField("0");
private JLabel newEmploeeLabel = new JLabel("# of people:");
private JLabel newProjectSizeLabel = new JLabel("# of people needed:");
private JLabel newProjectTimeLabel = new JLabel("Required time(in s):");
private JLabel oldEmployeesCountLabel = new JLabel("Old employees waiting for a new project");
private JLabel oldEmployeesCountValueLabel = new JLabel("0");
private JLabel candidateCountLabel = new JLabel("New candidates waiting for a job");
private JLabel candidateCountValueLabel = new JLabel("0");
private JButton newEmploeeButton = new JButton("Spawn");
private JButton newProjectButton = new JButton("Construct project");
private JButton clearCompletedButton = new JButton("Clear completed");
private LinkedList<ProjectPanel> projectPanelList = new LinkedList<ProjectPanel>();

ProjektowniaUI()
	{
	super("Projektownia");
	setSize(500, 640);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setLayout(new BorderLayout());
	add(eventsPanel, BorderLayout.CENTER);
	add(controlPanel, BorderLayout.SOUTH);
	eventsPanel.setLayout(new BorderLayout());
	eventsPanel.add(queueInfoPanel, BorderLayout.SOUTH);
	eventsPanel.add(innerEventsPanel, BorderLayout.CENTER);
	queueInfoPanel.setLayout(new GridLayout(2,2));
	queueInfoPanel.add(oldEmployeesCountValueLabel);
	oldEmployeesCountValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
	queueInfoPanel.add(candidateCountValueLabel);
	candidateCountValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
	queueInfoPanel.add(oldEmployeesCountLabel);
	oldEmployeesCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
	queueInfoPanel.add(candidateCountLabel);
	candidateCountLabel.setHorizontalAlignment(SwingConstants.CENTER);	
	innerEventsPanel.setLayout(new GridLayout(4,2));
	controlPanel.setLayout(new BorderLayout());
	controlPanel.add(innerControlPanel, BorderLayout.NORTH);
	controlPanel.add(clearCompletedButton, BorderLayout.SOUTH);
	clearCompletedButton.addActionListener(new ClearCompletedHandler());
	innerControlPanel.setLayout(new GridLayout(2,2));
	innerControlPanel.add(emploeeNorthPanel);
	innerControlPanel.add(projectNorthPanel);
	innerControlPanel.add(emploeeSouthPanel);
	innerControlPanel.add(projectSouthPanel);
	emploeeNorthPanel.setLayout(new GridLayout(2,2));
	projectNorthPanel.setLayout(new GridLayout(2,2));
	emploeeSouthPanel.setLayout(new GridLayout(1,1));
	projectSouthPanel.setLayout(new GridLayout(1,1));
	emploeeNorthPanel.add(new JLabel(""));
	emploeeNorthPanel.add(new JLabel(""));
	newEmploeeLabel.setHorizontalAlignment(SwingConstants.CENTER);
	emploeeNorthPanel.add(newEmploeeLabel);
	emploeeNorthPanel.add(newEmploeeTextField);
	emploeeSouthPanel.add(newEmploeeButton);
	newEmploeeButton.addActionListener(new ConjurePeople());
	projectNorthPanel.add(newProjectSizeLabel);
	projectNorthPanel.add(newProjectSizeTextField);
	projectNorthPanel.add(newProjectTimeLabel);
	projectNorthPanel.add(newProjectTimeTextField);
	projectSouthPanel.add(newProjectButton);
	newProjectButton.addActionListener(new ConjureProject());
	}

class ConjurePeople implements ActionListener
	{
	private short peopleCount = 0;
	
	public void actionPerformed(ActionEvent event)
		{
		if(!newEmploeeTextField.getText().matches("^[0-9]{1,3}$")) //input consists of 1-3 digits
			{
			newEmploeeTextField.setText("0");
			return;
			}
		short counter = Short.parseShort(newEmploeeTextField.getText());
		for(;counter > 0;counter--)
			{
			peopleCount++;
			new Person("Person " + peopleCount, company, company.getCandidateQueue()).start();
			}
		}
	}

class ConjureProject implements ActionListener
	{
	private short projectCount = 0;
	
	public void actionPerformed(ActionEvent event)
		{
		if(!newProjectSizeTextField.getText().matches("^[0-9]{1,3}$")) //input consists of 1-3 digits
			{
			newProjectSizeTextField.setText("0");
			return;
			}
		if(!newProjectTimeTextField.getText().matches("^[0-9]{1,3}$")) //input consists of 1-3 digits
			{
			newProjectTimeTextField.setText("0");
			return;
			}
		projectCount++;
		Project project = new Project("Project " + projectCount, Short.parseShort(newProjectTimeTextField.getText()), Short.parseShort(newProjectSizeTextField.getText()), company);
		project.start();
		ProjectPanel panel = new ProjectPanel(project);
		innerEventsPanel.add(panel.getMainPanel());
		innerEventsPanel.validate();
		innerEventsPanel.repaint();
		projectPanelList.addLast(panel);
		}
	}

class ClearCompletedHandler implements ActionListener
	{
	public void actionPerformed(ActionEvent event)
		{
		for(Iterator<ProjectPanel> it = projectPanelList.iterator(); it.hasNext();)
			{
			ProjectPanel current = it.next();
			if(current.project.isCompleted())
				{
				innerEventsPanel.remove(current.getMainPanel());
				it.remove();
				}
			}
		innerEventsPanel.validate();
		innerEventsPanel.repaint();
		}
	}

class ProjectPanel
	{
	private Project project;
	private String projectName;
	private short peopleRequired;
	private short timeRequired;
	private JPanel mainPanel = new JPanel();
	private JLabel nameLabel;
	private JLabel timeProgressBarLabel = new JLabel("Work Progress");
	private JLabel peopleProgressBarLabel = new JLabel("Team Status");
	private JLabel peopleResignedLabel = new JLabel("People resigned");
	private JLabel peopleResignedLabelValue;
	private JProgressBar peopleStatus = new JProgressBar();
	private JProgressBar timeStatus = new JProgressBar();
	
	ProjectPanel(Project project)
		{
		this.project = project;
		projectName = project.fetchName();
		peopleRequired = project.getPeopleRequired();
		timeRequired = project.getTimeRequired();
		nameLabel = new JLabel(projectName);
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timeProgressBarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timeStatus.setMinimum(0);
		timeStatus.setMaximum(timeRequired);
		timeStatus.setValue(0);
		timeStatus.setStringPainted(true);
		peopleStatus.setMinimum(0);
		peopleStatus.setMaximum(peopleRequired);
		short peopleAvailable = project.getPeopleAvailable();
		peopleStatus.setValue(peopleAvailable);
		peopleStatus.setString(peopleAvailable + "/" + peopleRequired);
		peopleStatus.setStringPainted(true);
		peopleProgressBarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		peopleResignedLabelValue = new JLabel("" + project.getPeopleResigned());
		peopleResignedLabelValue.setHorizontalAlignment(SwingConstants.CENTER);
		peopleResignedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		mainPanel.setLayout(new GridLayout(7,1));
		mainPanel.add(nameLabel);
		mainPanel.add(timeStatus);
		mainPanel.add(timeProgressBarLabel);
		mainPanel.add(peopleStatus);
		mainPanel.add(peopleProgressBarLabel);
		mainPanel.add(peopleResignedLabelValue);
		mainPanel.add(peopleResignedLabel);
		}

	JPanel getMainPanel()
		{
		return mainPanel;
		}

	void updatePeopleResignedLabelValue()
		{
		peopleResignedLabelValue.setText("" + project.getPeopleResigned());
		}
	
	void updatePeopleStatusBar()
		{
		short peopleAvailable = project.getPeopleAvailable();
		peopleStatus.setValue(peopleAvailable);
		peopleStatus.setString(peopleAvailable + "/" + peopleRequired);
		}

	void updateTimeStatusBar()
		{
		timeStatus.setValue(timeRequired - project.getTimeRemaining());
		}
	}

void updateUI()
	{
	while(true)
		{
		SwingUtilities.invokeLater
			(
			new Runnable()
				{
				public void run()
					{
					oldEmployeesCountValueLabel.setText(Integer.toString(company.getOldEmploeeQueue().getQueueLength()));
					candidateCountValueLabel.setText(Integer.toString(company.getCandidateQueue().getQueueLength()));
					for(Iterator<ProjectPanel> it = projectPanelList.iterator(); it.hasNext();)
						{
						ProjectPanel current = it.next();
						current.updatePeopleStatusBar();
						current.updateTimeStatusBar();
						current.updatePeopleResignedLabelValue();
						}
					}
				}
			);
		try
			{
			Thread.sleep(500);
			}
		catch(InterruptedException e){}
		}
	}

public static void main(String[] args)
	{
	ProjektowniaUI mainWindow = new ProjektowniaUI();
	mainWindow.setVisible(true);
	mainWindow.updateUI();
	}
}

class Projektownia
{
private Semaphore oldEmploeeApplications  = new Semaphore(0, true);
private Semaphore candidateApplications = new Semaphore(0, true);
private LinkedList<Person> availableEmployees = new LinkedList<Person>();

Semaphore getOldEmploeeQueue()
	{
	return oldEmploeeApplications;
	}

Semaphore getCandidateQueue()
	{
	return candidateApplications;
	}

LinkedList<Person> getAvailableEmployeesList()
	{
	return availableEmployees;
	}
}

class Person extends Thread
{
private String name;
private Projektownia employer;
private Semaphore queue;
Boolean assigned = false;

Person(String id, Projektownia company, Semaphore offer)
	{
	name = id;
	employer = company;
	queue = offer;
	}

public void run()
	{
	while(true)
		{
		try
			{
			queue.acquire(); //apply for the job
			}
		catch(InterruptedException e)
			{
			return;
			}
		employer.getAvailableEmployeesList().addLast(this); //announce you are ready
		while(!assigned)
			{
			try
				{
				sleep(100); //wait for assignment
				}
			catch(InterruptedException e){}
			}
		Random rand = new Random();
		while(assigned)
			{
			try
				{
				sleep(900); //wait to complete assignment
				}
			catch(InterruptedException e){}
			if(rand.nextInt(10000) < 11) //one tenth percent chance to resign during project for every iteration
				{
				assigned = false;
				return;
				}
			}
		if(rand.nextInt(100) < 50) //half stays for another project
			{
			return;
			}
		queue = employer.getOldEmploeeQueue(); //look for a new project
		}
	}

String fetchName()
	{
	return name;
	}
}

class Project extends Thread
{
private String name;
private short timeRequired;
private short peopleRequired;
private short timeRemaining;
private short hiredTotal;
private Projektownia company;
private LinkedList<Person> team = new LinkedList<Person>();

Project(String id, short time, short people, Projektownia owner)
	{
	name = id;
	timeRequired = time;
	timeRemaining = timeRequired;
	peopleRequired = people;
	company = owner;
	}

short getTimeRemaining()
	{
	return timeRemaining;
	}

short getTimeRequired()
	{
	return timeRequired;
	}

short getPeopleRequired()
	{
	return peopleRequired;
	}

short getPeopleAvailable()
	{
	return (short)team.size();
	}

short getPeopleResigned()
	{
	return (short)(hiredTotal - getPeopleAvailable());
	}

Boolean isCompleted()
	{
	return timeRemaining == 0;
	}

String fetchName()
	{
	return name;
	}

public void run()
	{
	Semaphore oldEmploeeList = company.getOldEmploeeQueue();
	Semaphore candidateList = company.getCandidateQueue();
	hiredTotal = 0;
	while(timeRemaining > 0)
		{
		if(team.size() == peopleRequired)
			{
			for(Iterator<Person> it = team.iterator(); it.hasNext();)
				{
				Person current = it.next();
				try
					{
					if(!current.assigned)
						{
						it.remove();
						}
					}
				catch(NullPointerException e)
					{
					it.remove();
					}
				}
			}
		while(team.size() < peopleRequired)
			{
			if(oldEmploeeList.hasQueuedThreads())
				{
				oldEmploeeList.release();
				try
					{
					sleep(5);
					}
				catch(InterruptedException e){}
				Person current = company.getAvailableEmployeesList().pollFirst();
				try
					{
					current.assigned = true;
					}
				catch(NullPointerException e)
					{
					continue;
					}
				team.addLast(current);
				hiredTotal++;
				continue;
				}
			if(candidateList.hasQueuedThreads())
				{
				candidateList.release();
				try
					{
					sleep(5);
					}
				catch(InterruptedException e){}
				Person current = company.getAvailableEmployeesList().pollFirst();
				try
					{
					current.assigned = true;
					}
				catch(NullPointerException e)
					{
					continue;
					}
				team.addLast(current);
				hiredTotal++;
				continue;
				}
			else
				{
				try
					{
					sleep(500);
					}
				catch(InterruptedException e){}
				}
			}
		try
			{
			sleep(1000);
			}
		catch(InterruptedException e){}
		timeRemaining--;
		}
	for(Iterator<Person> it = team.iterator(); it.hasNext();)
		{
		Person current = it.next();
		if(current == null) continue;
		current.assigned = false;
		}
	}
}