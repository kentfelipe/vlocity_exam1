package com.kfelipe.util;

import com.kfelipe.constant.TextConstant;
import com.kfelipe.db.Project;
import com.kfelipe.db.Task;
import com.kfelipe.db.dao.ProjectDao;
import com.kfelipe.db.dao.TaskDao;
import com.kfelipe.db.dao.impl.ProjectDaoImpl;
import com.kfelipe.db.dao.impl.TaskDaoImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuHelper {

    ProjectDao projectDao = ProjectDaoImpl.getInstance();
    TaskDao taskDao = TaskDaoImpl.getInstance();

    public final void showMainMenu( ) {
        System.out.println(TextConstant.MAIN_MENU);
        System.out.print("\nInput Number: ");
        int choice = getInputNumber(1,3);

        switch (choice) {
            case 1: addProject();
                break;
            case 2: displayAllProject();
                break;
            default:
                System.out.println("System Exit!");
                System.exit(0);
                break;
        }
    }

    public void addProject() {
        System.out.println(TextConstant.BAR);
        System.out.print("Enter a Project name: ");
        Project project = new Project(getInputString());
        projectDao.save(project);
        System.out.println("Project created: " + project);
        showProjectMenu(project);
    }

    public void displayAllProject() {
        System.out.println(TextConstant.BAR);
        projectDao.getAllProjects().forEach(System.out::println);
        System.out.print(TextConstant.SELECT_PROJECT_MENU);
        int id = getInputNumber(0, 99999);

        if (id == 0) {
            showMainMenu();
        } else {
            Project project = projectDao.getById(Long.parseLong("" + id));
            showProjectMenu(project);
        }
    }

    private void showProjectMenu(Project project) {
        if (project == null || project.getId() == null) {
            System.out.println("ERROR! Project not found.");
            System.exit(0);
        }

        System.out.println(TextConstant.BAR);
        System.out.println("Project menu");
        System.out.println(project);
        System.out.print(TextConstant.PROJECT_MENU);
        int choice = getInputNumber(1,3);
        switch (choice) {
            case 1: addTask(project);
                break;
            case 2: displayAllTask(project);
                break;
            case 3: CalendarCalculatorUtil calendarCalculatorUtil = new CalendarCalculatorUtil();
            System.out.print("Enter project start date [" + TextConstant.DATE_FORMAT + "]: " );
            calendarCalculatorUtil.generateSchedule(getInputDate(), project);
            break;
            default:
                System.out.println("System Exit!");
                System.exit(0);
                break;
        }
    }

    private void addTask(Project project) {
        System.out.println(TextConstant.BAR);
        System.out.println("Adding new Task");
        System.out.print("Input name: ");
        String name = getInputString();
        System.out.print("Input number [duration in days]: ");
        int duration = getInputNumber(1, 999999);
        System.out.print("Is a subtask? [1-yes, 0-no]: ");
        int isSubtask = getInputNumber(0, 1);
        Task parent = null;
        if(isSubtask == 1) {

            printTasks(taskDao.getMainTasksByProject(project));
            System.out.print("Select id of parent task [input 0 to set to null]:");
            while (true) {
                int taskId = getInputNumber(0, 999999);
                if (taskId == 0) {
                    break;
                } else {
                    parent = taskDao.getTaskByIdAndProject(Long.parseLong(""+taskId), project);
                    if (parent != null) {
                        break;
                    } else {
                        System.out.print("Task not found. Please enter a valid id: ");
                    }
                }
            }
        }

        Task task = new Task();
        task.setName(name);
        task.setDuration(duration);
        task.setProject(project);
        task.setParentTask(parent);
        taskDao.save(task);
        System.out.println("Task created [" + task.getId() + "]");

        showProjectMenu(project);
    }

    private void displayAllTask(Project project) {
        System.out.println(TextConstant.BAR);
        System.out.println(project);

        printTasks(taskDao.getMainTasksByProject(project));
        showProjectMenu(project);
    }

    public void printTasks(List<Task> tasks) {
        for(Task task : tasks) {
            System.out.println(task);
            if (task.getSubTasks().size() > 0) {
                printTasks(task.getSubTasks());
            }
        }
        if (tasks.size() == 0) {
            System.out.println("No task available");
        }
    }

    public int getInputNumber(int min, int max) {
        Scanner scanner = new Scanner(System.in);
        boolean valid = false;
        int value = 0;
        while (!valid) {
            try {
                value = scanner.nextInt();
                if (value>=min && value<=max) {
                    valid = true;
                } else {
                    System.out.println("**" + value + " -- " + min + "/" + max );
                    throw new InputMismatchException();
                }

            } catch (InputMismatchException e) {
                System.out.println("Input not valid! Enter a number from " + min + "-" + max);
                scanner.nextLine();
            }
        }
        scanner.reset();
        return value;
    }

    public String getInputString() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.reset();
        return input;
    }

    public Date getInputDate() {
        Scanner scanner = new Scanner(System.in);
        boolean valid = false;
        String input = null;
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(TextConstant.DATE_FORMAT);
        while (!valid) {
            try {
                input = scanner.next();
                date = sdf.parse(input);
                valid = true;
            } catch (ParseException e) {
                System.out.println("Input not valid a valid format. Enter date again [" + TextConstant.DATE_FORMAT + "]" );
                scanner.nextLine();

            }
        }
        scanner.reset();
        return date;
    }
}
