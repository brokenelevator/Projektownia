import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.concurrent.Semaphore;
import java.util.Random;
import java.util.LinkedList;

class ProjektowniaUI extends JFrame
{
private Projektownia company = new Projektownia();
private JPanel controlPanel = new JPanel();
private JPanel eventsPanel = new JPanel();
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
private JButton newEmploeeButton = new JButton("Spawn");
private JButton newProjectButton = new JButton("Construct project");

ProjektowniaUI()
	{
	super("Projektownia");
	setSize(500, 640);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setLayout(new BorderLayout());
	add(eventsPanel, BorderLayout.CENTER);
	add(controlPanel, BorderLayout.SOUTH);
	eventsPanel.setLayout(new GridLayout(4,2));
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
	}

class ConjurePeople implements ActionListener
	{
	short peopleCount = 0;
	
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

public static void main(String[] args)
	{
	ProjektowniaUI mainWindow = new ProjektowniaUI();
	mainWindow.setVisible(true);
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
String name;
Projektownia employer;
Semaphore queue;
Boolean assigned = false;

Person(String id, Projektownia company, Semaphore offer)
	{
	name = id;
	employer = company;
	queue = offer;
	}

public void run()
	{
	System.out.println(name + ". Ready for action.");
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
			if(rand.nextInt(100) < 11) //one in ten resigns during project
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