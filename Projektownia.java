import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;

class Projektownia extends JFrame
{
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

Projektownia()
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
	projectNorthPanel.add(newProjectSizeLabel);
	projectNorthPanel.add(newProjectSizeTextField);
	projectNorthPanel.add(newProjectTimeLabel);
	projectNorthPanel.add(newProjectTimeTextField);
	projectSouthPanel.add(newProjectButton);
	}

public static void main(String[] args)
	{
	Projektownia mainWindow = new Projektownia();
	mainWindow.setVisible(true);
	}
}