package com.kfelipe.db.dao;

import com.kfelipe.db.Project;

import java.util.List;

public interface ProjectDao {
    Long save(Project project);
    Project getById(Long id);
    List<Project> getAllProjects();
}
