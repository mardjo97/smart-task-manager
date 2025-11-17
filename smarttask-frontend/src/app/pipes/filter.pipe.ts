import { Pipe, PipeTransform } from '@angular/core';
import { Task } from '../models/models';

@Pipe({
  name: 'filter',
  standalone: true
})
export class FilterPipe implements PipeTransform {
  transform(tasks: Task[], filter: string): Task[] {
    if (!tasks || !filter) {
      return tasks;
    }

    if (filter === 'completed') {
      return tasks.filter(task => task.completed);
    } else if (filter === 'pending') {
      return tasks.filter(task => !task.completed);
    }

    return tasks;
  }
}
