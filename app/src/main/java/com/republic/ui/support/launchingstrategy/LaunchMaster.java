package com.republic.ui.support.launchingstrategy;

/** *
 * Created by Akwasi Owusu on 7/16/15.
 */
public class LaunchMaster<T> {

    private Launcher launcher;

    public LaunchMaster(Launcher launcher){
        super();
        this.launcher = launcher;
    }

    public T launch(){
       return (T)launcher.launch();
    }
}
