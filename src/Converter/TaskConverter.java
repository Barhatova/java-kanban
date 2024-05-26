package Converter;

import model.*;
import model.TypeTask;


    public class TaskConverter {
        public static TypeTask getType(Task task) {
           return TypeTask.TASK;
        }

        public static String toString(Task task) {
            return task.getId() + "," + task.getType() +"," + task.getName() + "," + task.getDescription() + "," +
                    task.getStatus() + "," + task.getEpicId();
        }
    }
