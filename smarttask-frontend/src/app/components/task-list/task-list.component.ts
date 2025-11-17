import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { TaskService } from '../../services/task.service';
import { AuthService } from '../../services/auth.service';
import { Task } from '../../models/models';
import { FilterPipe } from '../../pipes/filter.pipe';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FilterPipe],
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];
  filteredTasks: Task[] = [];
  taskForm: FormGroup;
  editingTask: Task | null = null;
  showForm: boolean = false;
  loading: boolean = false;
  filter: string = 'all';
  categoryFilter: string = '';

  constructor(
    private fb: FormBuilder,
    private taskService: TaskService,
    private authService: AuthService,
    private router: Router
  ) {
    this.taskForm = this.fb.group({
      title: ['', [Validators.required, Validators.maxLength(200)]],
      description: ['', Validators.maxLength(1000)],
      dueDate: [''],
      category: [''],
      completed: [false]
    });
  }

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(): void {
    this.loading = true;
    this.taskService.getAllTasks().subscribe({
      next: (tasks) => {
        this.tasks = tasks;
        this.applyFilters();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading tasks:', err);
        this.loading = false;
      }
    });
  }

  applyFilters(): void {
    let filtered = [...this.tasks];

    if (this.filter === 'completed') {
      filtered = filtered.filter(task => task.completed);
    } else if (this.filter === 'pending') {
      filtered = filtered.filter(task => !task.completed);
    }

    if (this.categoryFilter) {
      filtered = filtered.filter(task => task.category === this.categoryFilter);
    }

    this.filteredTasks = filtered;
  }

  onFilterChange(filter: string): void {
    this.filter = filter;
    this.applyFilters();
  }

  onCategoryFilterChange(event: Event): void {
    const target = event.target as HTMLSelectElement;
    this.categoryFilter = target.value;
    this.applyFilters();
  }

  getUniqueCategories(): string[] {
    const categories = this.tasks
      .map(task => task.category)
      .filter((category): category is string => !!category);
    return Array.from(new Set(categories));
  }

  showCreateForm(): void {
    this.editingTask = null;
    this.taskForm.reset({ completed: false });
    this.showForm = true;
  }

  showEditForm(task: Task): void {
    this.editingTask = task;
    this.taskForm.patchValue({
      title: task.title,
      description: task.description,
      dueDate: task.dueDate,
      category: task.category,
      completed: task.completed
    });
    this.showForm = true;
  }

  cancelForm(): void {
    this.showForm = false;
    this.editingTask = null;
    this.taskForm.reset();
  }

  onSubmit(): void {
    if (this.taskForm.valid) {
      const taskData = this.taskForm.value;

      if (this.editingTask && this.editingTask.id) {
        this.taskService.updateTask(this.editingTask.id, taskData).subscribe({
          next: () => {
            this.loadTasks();
            this.cancelForm();
          },
          error: (err) => console.error('Error updating task:', err)
        });
      } else {
        this.taskService.createTask(taskData).subscribe({
          next: () => {
            this.loadTasks();
            this.cancelForm();
          },
          error: (err) => console.error('Error creating task:', err)
        });
      }
    }
  }

  toggleComplete(task: Task): void {
    if (task.id) {
      const updatedTask = { ...task, completed: !task.completed };
      this.taskService.updateTask(task.id, updatedTask).subscribe({
        next: () => this.loadTasks(),
        error: (err) => console.error('Error updating task:', err)
      });
    }
  }

  deleteTask(task: Task): void {
    if (task.id && confirm('Are you sure you want to delete this task?')) {
      this.taskService.deleteTask(task.id).subscribe({
        next: () => this.loadTasks(),
        error: (err) => console.error('Error deleting task:', err)
      });
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  get currentUser() {
    return this.authService.getCurrentUser();
  }
}
