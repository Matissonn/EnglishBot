package com.MatOs;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static com.MatOs.bot.ProfileHandler.findUser;

public class TaskManager {
    public static String fileName = "src/main/resources/tasks.json";
    public static Gson gson = new Gson();

    public static Task getTask(String userName){
        Task[] tasks;
        try(FileReader fileReader = new FileReader(fileName)){
            tasks = gson.fromJson(fileReader, Task[].class);
            Task task = new Task();

            for(Task t : tasks){
                System.out.println(t);
                if(t.getId() > Integer.parseInt(findUser(userName).body().trim().substring(findUser(userName).body().trim().indexOf("tasks_completed")).replaceAll("[^0-9]", ""))){
                    task = t;
                    break;
                }
            }
            return task;

        } catch (FileNotFoundException e) {
            System.out.println("File not found! " + e);
        } catch (IOException e) {
            System.out.println("Read error!" + e);
        }

        return null;
    }
}