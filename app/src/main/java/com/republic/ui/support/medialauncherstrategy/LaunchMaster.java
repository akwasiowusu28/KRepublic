package com.republic.ui.support.medialauncherstrategy;

/** *
 * Created by Akwasi Owusu on 7/16/15.
 */
public class LaunchMaster {

    private Launcher launcher;

    public LaunchMaster(Launcher launcher){
        super();
        this.launcher = launcher;
    }

    public String launch(){
       return launcher.launch();
    }
}
