package com.amko0l.ontime.ontime.learning;

/**
 * Created by amko0l on 4/14/2017.
 */

public class NaiveBayes {

    float Pyes = 4/9;
    float Pno = 5/9;
    float Pnoon_no = 3/5;
    float Pmor_no = 1/5;
    float Peven_no = 1/5;
    float Pnoon_yes = 1/4;
    float Pmor_yes = 2/4;
    float Peven_yes = 1/4;
    float Paway_no = 2/5;
    float Phome_no = 3/5;
    float Paway_yes = 2/4;
    float Phome_yes = 2/4;
    float Pclear_no = 3/5;
    float Prainy_no = 2/5;
    float Pclear_yes = 1/4;
    float Prainy_yes = 3/4;

    public boolean isCoofee(int outcast, int location, int time){
        float yes;
        float no;

        //outcast == 0 clear, location == 0 home, time 0,1,2 morning noon, evening
        if(outcast==0){
            if(location==0){
                if(time==0){
                    yes = Pyes*Pclear_yes*Phome_yes*Pmor_yes;
                    no = Pno*Pclear_no*Phome_no*Pmor_no;
                }else if(time==1){
                    yes = Pyes*Pclear_yes*Phome_yes*Pnoon_yes;
                    no = Pno*Pclear_no*Phome_no*Pnoon_no;
                }else{
                    yes = Pyes*Pclear_yes*Phome_yes*Peven_yes;
                    no = Pno*Pclear_no*Phome_no*Peven_no;
                }
            }else{
                if(time==0){
                    yes = Pyes*Pclear_yes*Paway_yes*Pmor_yes;
                    no = Pno*Pclear_no*Paway_no*Pmor_no;
                }else if(time==1){
                    yes = Pyes*Pclear_yes*Paway_yes*Pnoon_yes;
                    no = Pno*Pclear_no*Paway_no*Pnoon_no;
                }else{
                    yes = Pyes*Pclear_yes*Paway_yes*Peven_yes;
                    no = Pno*Pclear_no*Paway_no*Peven_no;
                }
            }
        }else{
            if(location==0){
                if(time==0){
                    yes = Pyes*Prainy_yes*Phome_yes*Pmor_yes;
                    no = Pno*Prainy_no*Phome_no*Pmor_no;
                }else if(time==1){
                    yes = Pyes*Prainy_yes*Phome_yes*Pnoon_yes;
                    no = Pno*Prainy_no*Phome_no*Pnoon_no;
                }else{
                    yes = Pyes*Prainy_yes*Phome_yes*Peven_yes;
                    no = Pno*Prainy_no*Phome_no*Peven_no;
                }
            }else{
                if(time==0){
                    yes = Pyes*Prainy_yes*Paway_yes*Pmor_yes;
                    no = Pno*Prainy_no*Paway_no*Pmor_no;
                }else if(time==1){
                    yes = Pyes*Prainy_yes*Paway_yes*Pnoon_yes;
                    no = Pno*Prainy_no*Paway_no*Pnoon_no;
                }else{
                    yes = Pyes*Prainy_yes*Paway_yes*Peven_yes;
                    no = Pno*Prainy_no*Paway_no*Peven_no;
                }
            }
        }

        return yes>=no?true:false;
    }
}
