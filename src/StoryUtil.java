package pers.dxw.thread;

public class StoryUtil {
    public static void show(Thread t){
        System.out.println("The thread <" + t.getName() + "> state is " + t.getState());
    }
}
