package com.kfelipe.util;

import com.kfelipe.constant.TextConstant;
import com.kfelipe.db.Project;
import com.kfelipe.db.Task;
import com.kfelipe.db.dao.TaskDao;
import com.kfelipe.db.dao.impl.TaskDaoImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarCalculatorUtil {
    SimpleDateFormat sdf = new SimpleDateFormat(TextConstant.DATE_FORMAT);
    TaskDao taskDao = TaskDaoImpl.getInstance();
    int totalDuration = 0;

    public void generateSchedule(Date startDate, Project project) {
        System.out.println(TextConstant.BAR);
        System.out.println("Generating proposed schedule plan for project: " + project.getName());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        List<Task> taskList = taskDao.getMainTasksByProject(project);
        printTaskSchedule(taskList, calendar);

        System.out.println(TextConstant.BAR);
        System.out.println(project.getName() + " | Start Date: " + sdf.format(startDate) +
                " | End Date: " +  sdf.format(calendar.getTime()) +
                " | Total Project Duration: " + totalDuration + " days");
        System.out.println(TextConstant.BAR);
    }

    private void printTaskSchedule(List<Task> taskList, Calendar calendar) {
        for (Task task : taskList) {
            String start = sdf.format(calendar.getTime());
            calendar.add(Calendar.DATE, task.getDuration());
            String end = sdf.format(calendar.getTime());
            totalDuration += task.getDuration();

            System.out.println("Task: " + task.getName() +
                    " | Duration: " + task.getDuration() +
                    " day | Start Date: " + start +
                    " | End Date: " + end);

            if (task.getSubTasks().size() > 0) {
                printTaskSchedule(task.getSubTasks(), calendar);
            }

        }
    }
}
