package slothingwaffler;

import java.util.ArrayList;

/**
 * Manages a list of tasks.
 * <p>
 * This class allows adding, removing, and accessing tasks in a list.
 * </p>
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructs a TaskList instance with a specified Storage object.
     *
     * @param storage the Storage object used for saving and loading tasks
     */
    public TaskList(Storage storage) {
        this.tasks = storage.load();
    }

    public TaskList(StorageStub storage) {
        this.tasks = storage.load();
    }

    /**
     * Returns the list of tasks.
     *
     * @return the list of tasks
     */
    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Adds a Todo task to the list.
     *
     * @param split an array containing the task description split by space
     * @return the added Todo task
     * @throws SlothingWafflerException if the task description is empty
     */
    public Task addTodoTask(String[] split) throws SlothingWafflerException {
        if (split.length < 2 || split[1].isBlank()) {
            throw new SlothingWafflerException("HEY!! The description of a Todo Task cannot be empty!");
        }
        Task task = new Todo(split[1]);
        tasks.add(task);
        return task;
    }

    /**
     * Adds an Event task to the list.
     *
     * @param split an array containing the task description and times
     * @return the added Event task
     * @throws SlothingWafflerException if the task description, start time, or end time is missing
     */
    public Task addEventTask(String[] split) throws SlothingWafflerException {
        if (split.length < 2 || split[1].isBlank()) {
            throw new SlothingWafflerException("HEY!! The description of an Event Task cannot be empty.");
        }
        String[] desc = split[1].split(" /from | /to ");
        if (desc.length < 3) {
            throw new SlothingWafflerException("HEY!! An event must have a description, start time, and end time.");
        }
        Task task = new Event(desc[0], desc[1], desc[2]);
        tasks.add(task);
        return task;
    }

    /**
     * Adds a Deadline task to the list.
     *
     * @param split an array containing the task description and due date
     * @return the added Deadline task
     * @throws SlothingWafflerException if the task description or due date is missing
     */
    public Task addDeadlineTask(String[] split) throws SlothingWafflerException {
        if (split.length < 2 || split[1].isBlank()) {
            throw new SlothingWafflerException("HEY!! The description of a Deadline Task cannot be empty.");
        }
        String[] desc = split[1].split(" /by ", 2);
        if (desc.length < 2) {
            throw new SlothingWafflerException("HEY!! The Deadline Task must have a description AND a due date.");
        }
        Task task = new Deadline(desc[0], desc[1]);
        tasks.add(task);
        return task;
    }

    /**
     * Displays the list of tasks to the console.
     */
    public void displayTaskList() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i).toString());
        }
    }

    public void markTask(int taskNum) {
        tasks.get(taskNum).markAsDone();
    }

    public void deleteTask(int taskNum) {
        tasks.remove(taskNum);
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int i) {
        return tasks.get(i);
    }

    /**
     * Finds and displays tasks that match a search term.
     *
     * @param split an array containing the search term
     */
    public void findMatchingTasks(String[] split) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.toString().contains(split[1])) {
                matchingTasks.add(task);
            }
        }
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println((i + 1) + "." + matchingTasks.get(i).toString());
        }
    }

}
