package com.example.integrationtests.todo;

import org.springframework.data.jpa.repository.JpaRepository;

interface TodoRepository extends JpaRepository<TodoItem, Long> {
}
