import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Storage {

    private final File file;

    public Storage(String fileName) {
        String directoryPath = "./data";
        String filePath = directoryPath + "/" + fileName;
        this.file = new File(filePath);
    }

    public ArrayList<Task> load() {
        ensureFileExists();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            ArrayList<Task> taskList = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = convertStringToTask(line);
                if (task != null) {
                    taskList.add(task);
                }
            }
            return taskList;
        } catch (FileNotFoundException e) {
            System.out.println("No data file found");
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public void save(ArrayList<Task> taskList) {
        ensureFileExists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : taskList) {
                writer.write(task.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }

    private void ensureFileExists() {
        try {
            if (!file.exists()) {
                if (!this.file.getParentFile().mkdirs()) {
                    throw new IOException("Failed to create the necessary directories for the file");
                }
                if (!this.file.createNewFile()) {
                    throw new IOException("Failed to create file");
                }
            }
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage());
        }
    }

    private Task convertStringToTask(String line) {
        try {
            String[] parts = line.split(" \\| ");
            if (parts.length < 3) {
                throw new SlothingWafflerException("Missing data in:" + line);
            }

            String taskType = parts[0];
            boolean isDone = parts[1].equals("1");
            String description = parts[2];
            Task task = null;

            switch (taskType) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if (parts.length < 4) {
                    throw new SlothingWafflerException("Deadline missing for: " + line);
                }
                String deadline = parts[3];
                if (deadline != null) {
                    task = new Deadline(description, deadline);
                }
                break;
            case "E":
                if (parts.length < 4) {
                    throw new SlothingWafflerException("Event date missing for: " + line);
                }

                String date = parts[3];
                String[] dateParts = date.split("-");
                if (dateParts.length != 2) {
                    throw new SlothingWafflerException("Invalid date format for: " + line);
                }

                String startDate = dateParts[0];
                String endDate = dateParts[1];
                if (startDate != null && endDate != null) {
                    task = new Event(description, startDate, endDate);
                }
                break;
            default:
                throw new SlothingWafflerException("Invalid task type for: " + line);
            }
            if (task != null) {
                task.markAsDone();
            }
            return task;
        } catch (SlothingWafflerException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
