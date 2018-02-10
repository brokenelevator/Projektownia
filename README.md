# Projektownia
This is a project for Concurrent and Parallel Programming Workshop.

## Project overview
Projektownia is a hub for people running projects. User creates a project, sets duration and number of people needed. Projektownia can support many projects at the same time and is open for job applications at all times. Project team is chosen from one of two groups of people. Those who participated in previously run projects and new hires. The former have a priority in joining new projects. After completing a project every member of the project team chooses if he/she wants to stay and await new project or leave. There is also a possibility of resigning during project. In that event the project is put on hold until the situation is rectified. People and projects are threads. Job applications are handled by two semaphores, one for new hires and one for old. Advancement of a project and team member count is shown with progress bars. There is also information about a number of people that resigned.

## How to use it
Application consists of two parts. Control part(at the bottom) and information part(the rest).
To create a applicants, enter desired number of people to a field at the bottom left side. Press "Spawn". The number will add to already existing applicants number on the right side.
To create project enter required team size and time in seconds to text fields on the right side. Click the button below. It will show a project information above. Name, percentage of completeness, team count and people resigned count. With enough people available the project will start. If someone resigns, new person will be chosen. When there is not enough people to support a project it will be put on hold until replacements arrive. After completing a project number of people that chose to seek new project will be added at the left side.
Information panel will automatically resize project information when adding projects. To regain space, press button at the bottom.

### Note 
Text fields only allow up to 3 digits. Any other input will be ignored.

## How to run it

### Compile
```bash
javac ProjektowniaUI.java
```

### Run
```bash
java ProjektowniaUI
```

or download and run jar file from release section.