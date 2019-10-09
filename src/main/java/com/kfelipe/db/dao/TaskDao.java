package com.kfelipe.db.dao;

import com.kfelipe.db.Project;
import com.kfelipe.db.Task;

import java.util.List;

public interface TaskDao {
    Long save(Task task);
    Task getById(Long id);
    List<Task> getMainTasksByProject(Project project);
    Task getTaskByIdAndProject(Long id, Project project);
}
