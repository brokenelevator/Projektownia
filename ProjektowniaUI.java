import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
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
private JLabel oldEmploeesCountLabel = new JLabel("Old emploees waiting for a new project");
private JLabel oldEmploeesCountValueLabel = new JLabel("0");
private JLabel candidateCountLabel = new JLabel("New candidates waiting for a job");
private JLabel candidateCountValueLabel = new JLabel("0");
private JButton newEmploeeButton = new JButton("Spawn");
private JButton newProjectButton = new JButton("Construct project");
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
	queueInfoPanel.add(oldEmploeesCountValueLabel);
	oldEmploeesCountValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
	queueInfoPanel.add(candidateCountValueLabel);
	candidateCountValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
	queueInfoPanel.add(oldEmploeesCountLabel);
	oldEmploeesCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
	queueInfoPanel.add(candidateCountLabel);
	candidateCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
	innerEventsPanel.setLayout(new GridLayout(4,2));
	controlPanel.setLayout(new GridLayout(2,2));
	controlPanel.add(emploeeNorthPanel);
	controlPanel.add(projectNorthPanel);
	controlPanel.add(emploeeSouthPanel);
	controlPanel.add(projectSouthPanel);
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
		projectPanelList.addLast(panel);
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
	private JLabel progressBarDummy = new JLabel("");
	private JLabel peopleRequiredLabel = new JLabel("People required");
	private JLabel peopleRequiredLabelValue;
	private JLabel peopleAvailableLabel = new JLabel("People available");
	private JLabel peopleAvailableLabelValue;
	private JLabel peopleResignedLabel = new JLabel("People resigned");
	private JLabel peopleResignedLabelValue;
	
	ProjectPanel(Project project)
		{
		this.project = project;
		projectName = project.fetchName();
		peopleRequired = project.getPeopleRequired();
		timeRequired = project.getTimeRequired();
		nameLabel = new JLabel(projectName);
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		peopleRequiredLabelValue = new JLabel("" + peopleRequired);
		peopleRequiredLabelValue.setHorizontalAlignment(SwingConstants.CENTER);
		peopleRequiredLabel.setHorizontalAlignment(SwingConstants.CENTER);
		peopleAvailableLabelValue = new JLabel("" + project.getPeopleAvailable());
		peopleAvailableLabelValue.setHorizontalAlignment(SwingConstants.CENTER);
		peopleAvailableLabel.setHorizontalAlignment(SwingConstants.CENTER);
		peopleResignedLabelValue = new JLabel("" + project.getPeopleResigned());
		peopleResignedLabelValue.setHorizontalAlignment(SwingConstants.CENTER);
		peopleResignedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainPanel.setLayout(new GridLayout(8,1));
		mainPanel.add(nameLabel);
		mainPanel.add(progressBarDummy);
		mainPanel.add(peopleRequiredLabelValue);
		mainPanel.add(peopleRequiredLabel);
		mainPanel.add(peopleAvailableLabelValue);
		mainPanel.add(peopleAvailableLabel);
		mainPanel.add(peopleResignedLabelValue);
		mainPanel.add(peopleResignedLabel);
		}

	JPanel getMainPanel()
		{
		return mainPanel;
		}

	void updatePeopleAvailableLabelValue()
		{
		peopleAvailableLabelValue.setText("" + project.getPeopleAvailable());
		}

	void updatePeopleResignedLabelValue()
		{
		peopleResignedLabelValue.setText("" + project.getPeopleResigned());
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
					oldEmploeesCountValueLabel.setText(Integer.toString(company.getOldEmploeeQueue().getQueueLength()));
					candidateCountValueLabel.setText(Integer.toString(company.getCandidateQueue().getQueueLength()));
					for(Iterator<ProjectPanel> it = projectPanelList.iterator(); it.hasNext();)
						{
						ProjectPanel current = it.next();
						//if time is up throw away the panel
						current.updatePeopleAvailableLabelValue();
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
private LinkedList<Person> availableEmploees = new LinkedList<Person>();

Semaphore getOldEmploeeQueue()
	{
	return oldEmploeeApplications;
	}

Semaphore getCandidateQueue()
	{
	return candidateApplications;
	}

LinkedList<Person> getAvailableEmploeesList()
	{
	return availableEmploees;
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
		employer.getAvailableEmploeesList().addLast(this); //announce you are ready
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
				Person current = company.getAvailableEmploeesList().pollFirst();
				current.assigned = true;
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
				Person current = company.getAvailableEmploeesList().pollFirst();
				current.assigned = true;
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