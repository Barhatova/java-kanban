package converter;

import model.*;

    public class TaskConverter {

        public TypeTask getType(Task task) {
           return TypeTask.TASK;
        }

        public static String toString(Task task) {
            return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getDescription() + "," +
                    task.getStatus() + "," + task.getEpicId() + "," + task.getStartTime() + "," + task.getDuration();
        }
    }

